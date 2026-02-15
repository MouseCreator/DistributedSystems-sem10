package mouse.univ.algorithm;

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
}
