package mouse.univ.philosophers.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MutexTableTest {
    @Test
    void testBalancedFor5() throws InterruptedException {
        int N = 5;
        MutexTable countingTable = new MutexTable(N);
        Controller controller = new ControllerImpl();
        List<Philosopher> philosophers = Utils.createPhilosophers(countingTable, controller, N);
        philosophers.forEach(Philosopher::start);
        controller.setActive(true);
        Thread.sleep(10_000);
        controller.setActive(false);

        Map<Integer, Integer> counts = controller.getCounts();
        assertEquals(N, counts.size());
        System.out.println("Counts: " + counts);

        List<Integer> list = counts.values().stream().toList();
        double std = standardDeviation(list);
        System.out.println("Standard deviation" + std);
        assertTrue(std < 50);
    }

    private double standardDeviation(List<Integer> values) {
        double sum = 0;
        int n = values.size();

        double t = 0;
        for (Integer v : values) {
            t = t + v;
        }
        double mean = t / n;
        for (Integer v : values) {
            sum += (v - mean) * (v - mean);
        }
        return Math.sqrt(sum / n);
    }
}
