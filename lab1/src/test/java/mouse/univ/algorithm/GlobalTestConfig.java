package mouse.univ.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GlobalTestConfig {
    public static final Output OUTPUT = new ConsoleOutput();
    public static final Boolean PRINT_ORDER = Boolean.FALSE;
    public static final Long SEED = 218L;

    public static List<Long> generateUids(int number) {
        List<Long> result = new ArrayList<>();
        for (long i = 0; i < number; i++) {
            result.add(i);
        }
        return result;
    }
    public static List<Long> generateShuffledUids(int number) {
        List<Long> uids = generateUids(number);
        Collections.shuffle(uids, SEED == null ? new Random() : new Random(SEED));
        return uids;
    }
    public static Long maxUid(List<Long> uids) {
        return uids.stream().mapToLong(t->t).max().orElseThrow();
    }
}
