package mouse.univ.algorithm.lcr;

import mouse.univ.algorithm.AlgorithmMetadata;
import mouse.univ.algorithm.ConsoleOutput;
import mouse.univ.algorithm.Output;
import mouse.univ.algorithm.Stopwatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private void testRun(List<Long> uids) {
        Long maxUid = maxUid(uids);

        LcrController controller = new LcrControllerImpl(uids);
        LcrAlgorithm algorithm = new LcrAlgorithm(controller, uids, output);
        Stopwatch.measure(algorithm::start);
        AlgorithmMetadata metadata = controller.getMetadata();

        assertTrue(metadata.hasLeader());
        assertEquals(maxUid, metadata.getLeader());
    }

    @Test
    void testRunsOnThree() {
        testRun(generateShuffledUids(3));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 8, 10, 15, 20})
    void testRunsOnLargerValues(int processes) {
        testRun(generateShuffledUids(processes));
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 500})
    void testRunsOnEvenLargerValues(int processes) {
        testRun(generateShuffledUids(processes));
    }

    @Test
    void testRunsWithStraightList() {
        List<Long> longs = generateUids(100);
        testRun(longs);
    }

    @Test
    void testRunsWithReversedList() {
        List<Long> longs = generateUids(100);
        List<Long> reversed = longs.reversed();
        testRun(reversed);
    }

    @Test
    void testUidsDoesNotMatter() {
        List<Long> hardcoded = List.of(105L, 1000L, 2L, 88L);
        testRun(hardcoded);
    }

    @Test
    void testWorksOnOne() {
        List<Long> hardcoded = List.of(35L);
        testRun(hardcoded);
    }

}