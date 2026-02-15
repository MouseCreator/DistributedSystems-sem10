package mouse.univ;

import lombok.Getter;
import mouse.univ.algorithm.Status;

@Getter
public class State {
    private long uid;
    private Message sendPlus;
    private Message sendMinus;
    private Status status;
    private int phase;

    public State(long uid) {
        this.uid = uid;
        this.sendPlus = new Message(uid, Direction.OUT, 1);
        this.sendMinus = new Message(uid, Direction.OUT, 1);
        this.status = Status.UNKNOWN;
        this.phase = 0;
    }
}
