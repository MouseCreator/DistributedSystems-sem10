package mouse.univ.algorithm.lcr;

import mouse.univ.algorithm.AlgorithmMetadata;
import mouse.univ.algorithm.Status;

import java.util.ArrayList;
import java.util.List;

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
        if (controller.finished()) {
            LcrState state = states.getLast();
            if (state.getStatus() == Status.LEADER) {
                AlgorithmMetadata metadata = controller.getMetadata();
                output.write("Process " + state.getUid() + " is elected a leader.");
                output.write("Total processes: " + metadata.getProcesses());
                output.write("Total rounds: " + metadata.getRounds());
                output.write("Total messages: " + metadata.getMessages());
            }
            return;
        }
        controller.await();
        sendMessage();
        controller.await();
        receiveMessage();
        controller.await();
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
