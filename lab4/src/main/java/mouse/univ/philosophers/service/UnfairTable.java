package mouse.univ.philosophers.service;

public class UnfairTable implements Table{

    private final int total;

    private final Forks forks;

    public UnfairTable(int N) {
        this.total = N;
        this.forks = new Forks(N);
    }

    @Override
    public boolean acquireForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        if (id == 0) {
            forks.get(rightFork);
            forks.get(leftFork);
        } else {
            forks.get(leftFork);
            forks.get(rightFork);
        }
        return true;
    }

    @Override
    public void releaseForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        forks.release(leftFork);
        forks.release(rightFork);
    }
}
