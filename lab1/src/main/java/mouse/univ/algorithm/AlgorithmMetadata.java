package mouse.univ.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlgorithmMetadata {
    private Long leader;
    private Integer processes;
    private Integer messages;
    private Integer rounds;

    public boolean hasLeader() {
        return leader != null;
    }
}
