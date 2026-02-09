package mouse.univ;

import lombok.Getter;

@Getter
public class Message {

    private long uid;
    private Direction direction;
    private int hopCount;

    public Message(long uid, Direction direction, int hopCount) {
        this.uid = uid;
        this.direction = direction;
        this.hopCount = hopCount;
    }


}
