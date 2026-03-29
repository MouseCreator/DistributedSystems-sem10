package mouse.univ.color;

import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class OrderedLocalMinimizationColoring implements Coloring {

    private final int numWorkers;

    public OrderedLocalMinimizationColoring(Integer numWorkers) {
        if (numWorkers == null || numWorkers < 1) {
            numWorkers = 1;
        }
        this.numWorkers = numWorkers;
    }

    @Override
    public ColoredGraph minimize(ColoredGraph coloredGraph) {
        Set<ColoredVertex> all = coloredGraph.duplicate();
        ColoringState coloringState = new ColoringState(numWorkers, all);
        int size = all.size();
        List<Thread> workers = new ArrayList<>();
        int batch = size / numWorkers;
        for (int i = 0; i < numWorkers; i++) {
            int from = i * batch;
            int to = i == numWorkers - 1 ? size : (i + 1) * batch;
            Runnable work = () -> coloringWorker(from, to, coloringState);
            Thread worker = new Thread(work);
            workers.add(worker);
        }

        workers.forEach(Thread::start);

        try {
            coloringState.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ColoredGraph(all);
    }

    private void coloringWorker(int from, int to, ColoringState coloringState) {
        while (! coloringState.isDone()) {

            await(coloringState);
            coloringState.changesLastIteration.set(0);
            await(coloringState);
            for (int i = from; i < to; i++) {
                ColoredVertex current = coloringState.vertexList.get(i);
                boolean success = false;
                int prevColor = current.getColor();
                while (!success) {
                    success = Minimization.minimize(coloringState, current);
                }
                int afterColor = current.getColor();
                if (prevColor != afterColor) {
                    coloringState.changesLastIteration.incrementAndGet();
                }
            }
            await(coloringState);
            if (from == 0) {
                log.info("Changes last iteration: {}", coloringState.changesLastIteration.get());
            }
        }
        coloringState.latch.countDown();
    }

    private void await(ColoringState coloringState) {
        try {
            coloringState.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ColoringState implements GenColoringState {
        private CyclicBarrier barrier;
        private AtomicInteger changesLastIteration;
        private List<ColoredVertex> vertexList;
        private HashMap<Integer, Lock> locks;
        private AtomicInteger successfulMins;
        private AtomicInteger totalMins;
        private CountDownLatch latch;

        public ColoringState(int workers, Set<ColoredVertex> vertices) {
            this.barrier = new CyclicBarrier(workers);
            changesLastIteration = new AtomicInteger(-1);
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
            return changesLastIteration.get() == 0;
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
