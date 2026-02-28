package mouse.univ.philosophers.service;

import java.util.Map;

public interface Controller {
    boolean active();
    void setActive(boolean b);
    void think(int id);
    void eat(int id);
    Map<Integer, Integer> getCounts();
}
