package mouse.univ.philosophers.service;

import java.util.concurrent.Semaphore;

public class MutexTable implements Table {

    private final int total;


    private final Semaphore mutex;

    private final Forks forks;

    public MutexTable(int N) {
        this.total = N;
        this.mutex = new Semaphore(1, true);
        this.forks = new Forks(N);
    }

    @Override
    public boolean acquireForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        try {
            this.mutex.acquire();
            boolean successLeft = this.forks.tryGet(leftFork);
            if (successLeft) {

                boolean successRight = this.forks.tryGet(rightFork);
                this.mutex.release();
                if (successRight) {
                    return true;
                } else {
                    this.forks.release(leftFork);
                }
            } else {
                this.mutex.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void releaseForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        this.forks.release(leftFork);
        this.forks.release(rightFork);
    }
}
