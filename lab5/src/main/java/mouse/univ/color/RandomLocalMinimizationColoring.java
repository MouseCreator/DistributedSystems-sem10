package mouse.univ.color;

import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class RandomLocalMinimizationColoring implements Coloring {

    private final int numWorkers;
    private final Random random;
    private final int maxIterations;

    public RandomLocalMinimizationColoring(Integer numWorkers, Integer seed, int maxIterations) {
        if (numWorkers == null || numWorkers < 1) {
            numWorkers = 1;
        }
        this.numWorkers = numWorkers;
        if (seed == null) {
            this.random = new Random();
        } else {
            this.random = new Random(seed);
        }
        this.maxIterations = maxIterations;
    }

    @Override
    public ColoredGraph minimize(ColoredGraph coloredGraph) {
        Set<ColoredVertex> all = coloredGraph.duplicate();
        ColoringState coloringState = new ColoringState(maxIterations, numWorkers, all);

        List<Thread> workers = new ArrayList<>();
        for (int i = 0; i < numWorkers; i++) {
            Runnable work = () -> coloringWorker(coloringState);
            Thread worker = new Thread(work);
            workers.add(worker);
        }

        workers.forEach(Thread::start);

        try {
            coloringState.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Total successful minimizations: {}", coloringState.successfulMins.get());
        log.info("Total minimizations: {}", coloringState.totalMins.get());
        return new ColoredGraph(all);
    }

    private void coloringWorker(ColoringState coloringState) {
        int size = coloringState.vertexList.size();
        while (! coloringState.isDone()) {
            ColoredVertex current = coloringState.vertexList.get(random.nextInt(size));
            Minimization.minimize(coloringState, current);
        }
        coloringState.latch.countDown();
    }

    private static class ColoringState implements GenColoringState {
        private final int maxIterations;
        private List<ColoredVertex> vertexList;
        private HashMap<Integer, Lock> locks;
        private AtomicInteger successfulMins;
        private AtomicInteger totalMins;
        private CountDownLatch latch;

        public ColoringState(int maxIterations, int workers, Set<ColoredVertex> vertices) {
            this.maxIterations = maxIterations;
            Set<Integer> vIds = new HashSet<>();
            for (ColoredVertex v : vertices) {
                vIds.add(v.getId());
            }
            locks = new HashMap<>();
            for (Integer vId : vIds) {
                locks.put(vId, new ReentrantLock());
            }
            vertexList = new ArrayList<>(vertices);
            successfulMins = new AtomicInteger(0);
            totalMins = new AtomicInteger(0);
            latch = new CountDownLatch(workers);
        }
        private boolean isDone() {
            return totalMins.get() > maxIterations;
        }

        @Override
        public HashMap<Integer, Lock> getLocks() {
            return locks;
        }

        @Override
        public void incrementSuccess() {
            successfulMins.incrementAndGet();
        }

        @Override
        public void incrementTotal() {
            totalMins.incrementAndGet();
        }
    }
}
