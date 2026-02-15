package mouse.univ.algorithm.lcr;

import lombok.extern.log4j.Log4j2;
import mouse.univ.algorithm.AlgorithmMetadata;
import mouse.univ.algorithm.Status;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class LcrProcess extends Thread {
    private final List<LcrState> states;
    private final LcrController controller;
    private final Output output;
    private final Long neighborUid;

    public LcrProcess(LcrState state, Long neighborUid, LcrController controller, Output output) {
        states = new ArrayList<>();
        states.add(state);
        this.controller = controller;
        this.output = output;
        this.neighborUid = neighborUid;
    }

    public void run() {
        LcrState state = states.getLast();
        log.info("{} started!", state.getUid());
        while (true) {
            controller.startRound();
            if (controller.finished()) {
                state = states.getLast();
                if (state.getStatus() == Status.LEADER) {
                    AlgorithmMetadata metadata = controller.getMetadata();
                    output.write("Process " + state.getUid() + " is elected a leader.");
                    output.write("Total processes: " + metadata.getProcesses());
                    output.write("Total rounds: " + metadata.getRounds());
                    output.write("Total messages: " + metadata.getMessages());
                }
                return;
            }
            log.info("{} awaits to send", state.getUid());
            controller.await();
            sendMessage();
            log.info("{} awaits to receive", state.getUid());
            controller.await();
            receiveMessage();
            log.info("{} awaits to check", state.getUid());
            controller.await();
        }
    }

    private void sendMessage() {
        LcrState current = states.getLast();
        if (current.getSend() == null) {
            return;
        }
        LcrMessage lcrMessage = new LcrMessage(current.getSend());
        controller.send(neighborUid, lcrMessage);
    }

    private void receiveMessage() {
        LcrState current = states.getLast();
        Long uid = current.getUid();
        Status status = current.getStatus();

        List<LcrMessage> lcrMessages = controller.receiveMessages(uid);

        if (lcrMessages.isEmpty()) {
            return;
        }
        if (lcrMessages.size() > 1) {
            throw new IllegalArgumentException("Unexpected number of messages in process " + uid + ": " + lcrMessages.size());
        }
        LcrMessage lcrMessage = lcrMessages.getFirst();

        Long send = null;
        Long v = lcrMessage.getUid();
        if (v > uid) {
            send = v;
        } else if (v.equals(uid)) {
            status = Status.LEADER;
            controller.notifyLeaderFound(uid);
        }

        states.addLast(new LcrState(uid, send, status));
    }

}
