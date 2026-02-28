package mouse.univ.philosophers.service;

public interface Table {
    boolean acquireForks(int id);
    void releaseForks(int id);
}
