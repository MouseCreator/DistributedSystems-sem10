package mouse.univ.algorithm.hs;

import lombok.extern.log4j.Log4j2;
import mouse.univ.algorithm.AlgorithmMetadata;
import mouse.univ.algorithm.Direction;
import mouse.univ.algorithm.Output;
import mouse.univ.algorithm.Status;

import java.util.List;
import java.util.Objects;

@Log4j2
public class HSProcess extends Thread{

    private HSState state;
    private final HSController controller;
    private final Output output;
    private final Long neighborUidPlus;
    private final Long neighborUidMinus;

    public HSProcess(HSState state, Long neighborUidMinus, Long neighborUidPlus, HSController controller, Output output) {
        this.state = state;
        this.controller = controller;
        this.output = output;
        this.neighborUidPlus = neighborUidPlus;
        this.neighborUidMinus = neighborUidMinus;
    }

    @Override
    public void run() {
        log.info("{} started!", state.getUid());
        while (true) {
            controller.startRound();
            if (controller.finished()) {
                notifyFinished();
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

    private void notifyFinished() {
        if (state.getStatus() == Status.LEADER) {
            AlgorithmMetadata metadata = controller.getMetadata();
            output.write("Process " + state.getUid() + " is elected a leader.");
            output.write("Total processes: " + metadata.getProcesses());
            output.write("Total rounds: " + metadata.getRounds());
            output.write("Total messages: " + metadata.getMessages());
        }
    }


    private void sendMessage() {
        if (state.getSendMinus() != null) {
            controller.send(neighborUidMinus, state.getSendMinus());
        }
        if (state.getSendPlus() != null) {
            controller.send(neighborUidPlus, state.getSendPlus());
        }
    }

    private void receiveMessage() {
        List<HSMessage> hsMessages = controller.receiveMessages(state.getUid());
        if (hsMessages.size() > 2) {
            throw new IllegalArgumentException("Unexpected number of messages received: " + hsMessages);
        }

        HSMessage messageFromPrev = getMessageFrom(hsMessages, neighborUidMinus);
        HSMessage messageFromNext = getMessageFrom(hsMessages, neighborUidPlus);

        HSMessage sendPlus = null;
        HSMessage sendMinus = null;
        Long uid = state.getUid();
        Status status = state.getStatus();
        int phase = state.getPhase();

        if (hsMessages.isEmpty()) {
            state = new HSState(uid, status, phase, null, null);
            return;
        }

        // Outgoing token from prev
        if (messageFromPrev != null && messageFromPrev.getDirection() == Direction.OUT) {
            if (messageFromPrev.getUid() > uid && messageFromPrev.getHopCount() > 1) {
                sendPlus = new HSMessage(uid, messageFromPrev.getUid(), Direction.OUT ,messageFromPrev.getHopCount() - 1);
            }
            else if (messageFromPrev.getUid() > uid && messageFromPrev.getHopCount() == 1) {
                sendMinus = new HSMessage(uid, messageFromPrev.getUid(), Direction.IN ,1);
            }
            else if (Objects.equals(messageFromPrev.getUid(), uid)) {
                status = Status.LEADER;
                controller.notifyLeaderFound(uid);
            }
        }

        // Outgoing token from next
        if (messageFromNext != null && messageFromNext.getDirection() == Direction.OUT) {
            if (messageFromNext.getUid() > uid && messageFromNext.getHopCount() > 1) {
                sendMinus = new HSMessage(uid, messageFromNext.getUid(), Direction.OUT ,messageFromNext.getHopCount() - 1);
            }
            else if (messageFromNext.getUid() > uid && messageFromNext.getHopCount() == 1) {
                sendPlus = new HSMessage(uid, messageFromNext.getUid(), Direction.IN ,1);
            }
            else if (messageFromNext.getUid().equals(uid)) {
                status = Status.LEADER;
                controller.notifyLeaderFound(uid);
            }
        }

        // Message is getting back
        if (messageFromPrev != null
                && messageFromPrev.getDirection() == Direction.IN
                && messageFromPrev.getHopCount() == 1
                && !Objects.equals(messageFromPrev.getUid(), uid)) {
            sendPlus = new HSMessage(uid, messageFromPrev.getUid(), Direction.IN, 1);
        }

        // Message is getting back
        if (messageFromNext != null
                && messageFromNext.getDirection() == Direction.IN
                && messageFromNext.getHopCount() == 1
                && !Objects.equals(messageFromNext.getUid(), uid)) {
            sendMinus = new HSMessage(uid, messageFromNext.getUid(), Direction.IN, 1);
        }

        // Receiving own tokens back
        if (messageFromPrev != null && messageFromNext != null
                && messageFromPrev.getDirection() == Direction.IN && messageFromNext.getDirection() == Direction.IN
                && messageFromPrev.getHopCount() == 1 && messageFromNext.getHopCount() == 1
                && Objects.equals(messageFromPrev.getUid(), uid) && Objects.equals(messageFromNext.getUid(), uid)) {
            phase = phase + 1;
            sendPlus = new HSMessage(uid, uid, Direction.OUT, 1 << phase);
            sendMinus = new HSMessage(uid, uid, Direction.OUT, 1 << phase);
        }
        state = new HSState(uid, status, phase, sendMinus, sendPlus);
    }

    private HSMessage getMessageFrom(List<HSMessage> hsMessages, Long from) {
        for (HSMessage message : hsMessages) {
            if (Objects.equals(message.getSender(), from)) {
                return message;
            }
        }
        return null;
    }

}
