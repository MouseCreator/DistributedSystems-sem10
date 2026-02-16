package mouse.univ.algorithm.lcr;

import mouse.univ.algorithm.AlgorithmMetadata;
import mouse.univ.algorithm.Stopwatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static mouse.univ.algorithm.GlobalTestConfig.*;

class LcrAlgorithmTest {

    private void testRun(List<Long> uids) {
        Long maxUid = maxUid(uids);
        if (PRINT_ORDER) {
            System.out.println("Process order:" + uids);
        }
        LcrController controller = new LcrControllerImpl(uids);
        LcrAlgorithm algorithm = new LcrAlgorithm(controller, uids, OUTPUT);
        Stopwatch.measure(algorithm::start);
        AlgorithmMetadata metadata = controller.getMetadata();

        assertNull(controller.getError());
        assertTrue(metadata.hasLeader());
        assertEquals(maxUid, metadata.getLeader());
    }

    @Test
    void testRunsOnThree() {
        testRun(generateShuffledUids(3));
    }

    @Test
    void testRunsOnFive() {
        testRun(List.of(2L, 0L, 3L, 4L, 1L));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 10, 15, 20})
    void testRunsOnLargerValues(int processes) {
        testRun(generateShuffledUids(processes));
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 300, 400, 500, 600})
    void testRunsOnEvenLargerValues(int processes) {
        testRun(generateShuffledUids(processes));
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 300, 400, 500, 600})
    void testRunsWithStraightList(int processes) {
        List<Long> longs = generateUids(processes);
        testRun(longs);
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 300, 400, 500, 600})
    void testRunsWithReversedList(int processes) {
        List<Long> longs = generateUids(processes);
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