package mouse.univ.philosophers.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnfairTableTest {
    @Test
    void testNotBalancedFor5() throws InterruptedException {
        int N = 5;
        UnfairTable countingTable = new UnfairTable(N);
        Controller controller = new ControllerImpl();
        List<Philosopher> philosophers = Utils.createPhilosophers(countingTable, controller, N);
        philosophers.forEach(Philosopher::start);
        controller.setActive(true);
        Thread.sleep(10_000);
        controller.setActive(false);
        Thread.sleep(10);
        Map<Integer, Integer> counts = controller.getCounts();
        assertEquals(N, counts.size());
        System.out.println("Counts: " + counts);
        double avg = counts.values().stream().mapToInt(i->i).average().orElseThrow();
        assertTrue(counts.get(0) < avg);
        assertTrue(counts.get(1) < avg);
    }
}