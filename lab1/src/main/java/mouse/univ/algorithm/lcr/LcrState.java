package mouse.univ.algorithm.lcr;

import lombok.Data;
import mouse.univ.algorithm.Status;

@Data
public class LcrState {
    private final Long uid;
    private final LcrMessage send;
    private final Status status;

    public LcrState(Long uid) {
        this.uid = uid;
        this.send = new LcrMessage(uid);
        this.status = Status.UNKNOWN;
    }

    public LcrState(Long uid, LcrMessage send, Status status) {
        this.uid = uid;
        this.send = send;
        this.status = status;
    }
}
