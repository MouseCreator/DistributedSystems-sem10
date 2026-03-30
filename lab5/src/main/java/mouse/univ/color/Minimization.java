package mouse.univ.color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;

public class Minimization {

    public static boolean minimize(GenColoringState coloringState, ColoredVertex current) {
        int id = current.getId();
        Lock currentLock = coloringState.getLocks().get(id);
        try {
            coloringState.semaphore().acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        currentLock.lock();
        List<Lock> acquired = new ArrayList<>();
        acquired.add(currentLock);
        boolean success = true;
        for (ColoredVertex n : current.getNeighbors()) {
            Lock neighborLock = coloringState.getLocks().get(n.getId());
            boolean locked = neighborLock.tryLock();
            if (locked) {
                acquired.add(neighborLock);
            } else {
                acquired.forEach(Lock::unlock);
                success = false;
                break;
            }
        }

        if (!success) {
            return false;
        }
        Set<Integer> knownColors = new HashSet<>();
        int minColor = 0;
        for (ColoredVertex n : current.getNeighbors()) {
            int color = n.getColor();
            knownColors.add(color);
            while (knownColors.contains(minColor)) {
                minColor++;
            }
        }
        if (minColor < current.getColor()) {
            coloringState.incrementSuccess();
            current.setColor(minColor);
        }
        acquired.forEach(Lock::unlock);
        coloringState.semaphore().release();
        coloringState.incrementTotal();
        return true;
    }
}
