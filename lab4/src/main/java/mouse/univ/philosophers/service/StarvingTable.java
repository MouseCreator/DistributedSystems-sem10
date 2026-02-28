package mouse.univ.philosophers.service;

public class StarvingTable implements Table{

    private final int total;

    private final Forks forks;

    public StarvingTable(int N) {
        this.total = N;
        this.forks = new Forks(N);
    }

    @Override
    public boolean acquireForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        if (id == 0) {
            forks.get(leftFork);
            boolean gotRight = forks.tryGet(rightFork);
            if (gotRight) {
                return true;
            }
            forks.release(leftFork);
            return false;
        } else {
            forks.get(leftFork);
            forks.get(rightFork);
            return true;
        }
    }

    @Override
    public void releaseForks(int id) {
        int leftFork = id;
        int rightFork = (id + 1) % total;
        forks.release(leftFork);
        forks.release(rightFork);
    }
}
