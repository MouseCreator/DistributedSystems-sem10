package mouse.univ.philosophers.service;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class CountingTableTest {

    @Test
    void testBalancedFor5() throws InterruptedException {
        int N = 5;
        CountingTable countingTable = new CountingTable(N);
        Controller controller = new ControllerImpl();
        List<Philosopher> philosophers = Utils.createPhilosophers(countingTable, controller, N);
        philosophers.forEach(Philosopher::start);
        controller.setActive(true);
        Thread.sleep(10_000);
        controller.setActive(false);

        Map<Integer, Integer> counts = controller.getCounts();
        assertEquals(N, counts.size());
        System.out.println("Counts: " + counts);

        int max = counts.values().stream().max(Integer::compare).orElseThrow();
        int min = counts.values().stream().min(Integer::compare).orElseThrow();

        assertTrue(max - min < 5);
    }
}