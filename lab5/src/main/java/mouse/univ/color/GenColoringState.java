package mouse.univ.color;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public interface GenColoringState {
    HashMap<Integer, Lock> getLocks();
    void incrementSuccess();
    void incrementTotal();
}
