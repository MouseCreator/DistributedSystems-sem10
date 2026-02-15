package mouse.univ.algorithm.lcr;

import java.util.ArrayList;
import java.util.List;

public class LcrAlgorithm {
    private final Integer numberProcesses;
    private final LcrController controller;
    private final List<Long> uids;
    private final Output output;

    public LcrAlgorithm(LcrController controller, List<Long> uids, Output output) {
        this.numberProcesses = controller.getMetadata().getProcesses();
        this.controller = controller;
        this.uids = uids;
        this.output = output;
    }

    public void start() {
        List<LcrProcess> threads = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++) {
            Long neighbor;
            if (i == numberProcesses - 1) {
                neighbor = uids.getFirst();
            } else {
                neighbor = uids.get(i+1);
            }
            LcrState state = new LcrState(uids.get(i));
            LcrProcess process = new LcrProcess(state, neighbor, controller, output);
            process.start();
            threads.add(process);
        }
        for (LcrProcess thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
