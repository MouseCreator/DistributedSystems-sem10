package mouse.univ;

import mouse.univ.algorithm.Status;

public class Transition {

    public static void transition(State state, int sender, Message message) {
        Message sendPlus = null;
        Message sendMinus = null;
        Status status = state.getStatus();

        if (state.getUid() - 1 == sender) {
            if (state.getUid() > message.getUid() && message.getHopCount() > 1) {
                sendPlus = new Message(state.getUid(), Direction.OUT, message.getHopCount() - 1);
            }
            else if (state.getUid() > message.getUid() && message.getHopCount() == 1) {
                sendMinus = new Message(state.getUid(), Direction.IN, 1);
            }
            else if (state.getUid() == message.getUid()) {
                status = Status.LEADER;
            }
        }
        else if (state.getUid() + 1 == sender) {
            if (state.getUid() > message.getUid() && message.getHopCount() > 1) {
                sendMinus = new Message(state.getUid(), Direction.OUT, message.getHopCount() - 1);
            }
            else if (state.getUid() > message.getUid() && message.getHopCount() == 1) {
                sendPlus = new Message(state.getUid(), Direction.IN, 1);
            }
            else if (state.getUid() == message.getUid()) {
                status = Status.LEADER;
            }
        }
    }
}
