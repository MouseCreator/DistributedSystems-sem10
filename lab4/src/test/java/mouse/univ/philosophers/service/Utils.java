package mouse.univ.philosophers.service;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Philosopher> createPhilosophers(Table table, Controller controller, int count) {
        List<Philosopher> philosopherList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            philosopherList.add(new Philosopher(table, controller, i));
        }
        return philosopherList;
    }
}
