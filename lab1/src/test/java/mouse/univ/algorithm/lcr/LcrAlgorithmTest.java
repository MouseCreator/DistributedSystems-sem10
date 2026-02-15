package mouse.univ.algorithm.lcr;

import mouse.univ.algorithm.AlgorithmMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LcrAlgorithmTest {

    private static final Output output = new ConsoleOutput();

    private List<Long> generateUids(int number) {
        List<Long> result = new ArrayList<>();
        for (long i = 0; i < number; i++) {
            result.add(i);
        }
        return result;
    }
    private List<Long> generateShuffledUids(int number) {
        List<Long> uids = generateUids(number);
        Collections.shuffle(uids);
        return uids;
    }
    private Long maxUid(List<Long> uids) {
        return uids.stream().mapToLong(t->t).max().orElseThrow();
    }

    @Test
    void testRunsOnThree() {
        List<Long> uids = generateShuffledUids(3);
        Long maxUid = maxUid(uids);

        LcrController controller = new LcrControllerImpl(uids);
        LcrAlgorithm algorithm = new LcrAlgorithm(controller, uids, output);
        algorithm.start();
        AlgorithmMetadata metadata = controller.getMetadata();

        assertTrue(metadata.hasLeader());
        assertEquals(maxUid, metadata.getLeader());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 8, 10, 15, 20})
    void testRunsOnLargerValues(int processes) {
        List<Long> uids = generateShuffledUids(processes);
        Long maxUid = maxUid(uids);

        LcrController controller = new LcrControllerImpl(uids);
        LcrAlgorithm algorithm = new LcrAlgorithm(controller, uids, output);
        algorithm.start();
        AlgorithmMetadata metadata = controller.getMetadata();

        assertTrue(metadata.hasLeader());
        assertEquals(maxUid, metadata.getLeader());
    }

}