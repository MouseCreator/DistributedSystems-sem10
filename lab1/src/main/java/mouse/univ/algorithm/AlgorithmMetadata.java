package mouse.univ.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AlgorithmMetadata {
    private long leader;
    private int processes;
    private int messages;
    private int rounds;
}
