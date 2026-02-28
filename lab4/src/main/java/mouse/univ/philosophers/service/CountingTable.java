package mouse.univ.philosophers.service;

import java.util.concurrent.Semaphore;

public class CountingTable implements Table {

    private final Semaphore semaphore;

    private final int total;

    private final Forks forks;

    public CountingTable(int N) {
        semaphore = new Semaphore(N - 1, true);
        if (N == 1) {
            semaphore.release();
        }
        total = N;
        forks = new Forks(N);
    }

    @Override
    public boolean acquireForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        try {
            semaphore.acquire();
            forks.get(leftFork);
            forks.get(rightFork);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void releaseForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        forks.release(leftFork);
        forks.release(rightFork);
        semaphore.release();
    }
}
