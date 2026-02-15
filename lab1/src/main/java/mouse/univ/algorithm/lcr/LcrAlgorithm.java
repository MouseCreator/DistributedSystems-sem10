package mouse.univ.algorithm.lcr;

import java.util.List;

public class LcrAlgorithm {
    private final int numberProcesses;
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
        for (int i = 0; i < numberProcesses; i++) {
            Long neighbor;
            if (i == 0) {
                neighbor = uids.getLast();
            } else {
                neighbor = uids.get(i+1);
            }
            LcrState state = new LcrState(uids.get(i));
            LcrProcess process = new LcrProcess(state, neighbor, controller, output);
            process.run();
        }
    }
}
