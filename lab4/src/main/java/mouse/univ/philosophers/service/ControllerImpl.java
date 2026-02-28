package mouse.univ.philosophers.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControllerImpl implements Controller {

    private final AtomicBoolean active;

    private final ConcurrentMap<Integer, Integer> counts;

    public ControllerImpl() {
        active = new AtomicBoolean(false);
        counts = new ConcurrentHashMap<>();
    }

    @Override
    public boolean active() {
        return active.get();
    }

    @Override
    public void setActive(boolean b) {
        active.set(b);
    }

    @Override
    public void think(int id) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eat(int id) {
        if (!counts.containsKey(id)) {
            counts.put(id, 0);
        }
        counts.compute(id, (k, prev) -> prev + 1);
    }

    @Override
    public Map<Integer, Integer> getCounts() {
        return new HashMap<>(counts);
    }
}
