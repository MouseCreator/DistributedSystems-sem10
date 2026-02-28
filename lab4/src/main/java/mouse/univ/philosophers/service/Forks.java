package mouse.univ.philosophers.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

public class Forks {

    private final ConcurrentMap<Integer, Semaphore> map;

    public Forks(int n) {
        this.map = new ConcurrentHashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(i, new Semaphore(1));
        }
    }

    public void get(int fork) {
        try {
            map.get(fork).acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean tryGet(int fork) {
        return map.get(fork).tryAcquire();
    }

    public void release(int fork) {
        map.get(fork).release();
    }
}
