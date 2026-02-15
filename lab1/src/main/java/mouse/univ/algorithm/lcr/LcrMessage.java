package mouse.univ.algorithm.lcr;

import lombok.Data;

@Data
public class LcrMessage {
    private Long uid;

    public LcrMessage(Long uid) {
        this.uid = uid;
    }
}
