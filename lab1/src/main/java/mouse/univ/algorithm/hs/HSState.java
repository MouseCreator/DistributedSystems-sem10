package mouse.univ.algorithm.hs;

import lombok.AllArgsConstructor;
import lombok.Data;
import mouse.univ.algorithm.Direction;
import mouse.univ.algorithm.Status;

@Data
@AllArgsConstructor
public class HSState {
    private final Long uid;
    private final Status status;
    private final Integer phase;
    private final HSMessage sendMinus;
    private final HSMessage sendPlus;

    public HSState(Long uid) {
        this.uid = uid;
        this.status = Status.UNKNOWN;
        phase = 0;
        sendPlus = new HSMessage(uid, uid, Direction.OUT, 1);
        sendMinus = new HSMessage(uid, uid, Direction.OUT, 1);
    }
}
