package mouse.univ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class Stopwatch {

    private Stopwatch() {
    }

    public static void measure(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        long msElapsed = (end - start) / 1_000_000;
        System.out.println("Time elapsed: " + msElapsed);
    }

    public static <T> T measure(Callable<T> r) {
        long start = System.nanoTime();
        T result;
        try {
            result = r.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        long msElapsed = (end - start) / 1_000_000;
        System.out.println("Time elapsed: " + msElapsed);
        return result;
    }

    public static <T> T measure(Callable<T> r, int times) {
        T result = null;
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            long start = System.nanoTime();
            try {
                result = r.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            long end = System.nanoTime();
            long ms = (end - start) / 1_000_000;
            list.add(ms);
        }
        double msElapsed = list.stream().mapToLong(s->s).average().orElseThrow();
        System.out.println("Time elapsed: " + msElapsed);
        return result;
    }
}
