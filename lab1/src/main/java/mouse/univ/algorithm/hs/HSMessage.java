package mouse.univ.algorithm.hs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import mouse.univ.algorithm.Direction;

@Data
@AllArgsConstructor
public class HSMessage {
    private Long sender;
    @NonNull
    private Long uid;
    private Direction direction;
    private int hopCount;
}
