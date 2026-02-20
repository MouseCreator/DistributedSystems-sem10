package mouse.univ;

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
}
