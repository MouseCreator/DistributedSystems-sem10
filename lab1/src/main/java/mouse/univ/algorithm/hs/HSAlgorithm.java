package mouse.univ.algorithm.hs;

import mouse.univ.algorithm.Algorithm;
import mouse.univ.algorithm.Output;

import java.util.ArrayList;
import java.util.List;

public class HSAlgorithm implements Algorithm {
    private final Integer numberProcesses;
    private final HSController controller;
    private final List<Long> uids;
    private final Output output;

    public HSAlgorithm(HSController controller, List<Long> uids, Output output) {
        this.numberProcesses = controller.getMetadata().getProcesses();
        this.controller = controller;
        this.uids = uids;
        this.output = output;
    }

    public void start() {
        List<HSProcess> threads = new ArrayList<>();
        for (int i = 0; i < numberProcesses; i++) {
            Long prev, next;

            if (i == 0) {
                prev = uids.getLast();
            } else {
                prev = uids.get(i-1);
            }

            if (i == numberProcesses - 1) {
                next = uids.getFirst();
            } else {
                next = uids.get(i+1);
            }

            HSState state = new HSState(uids.get(i));
            HSProcess process = new HSProcess(state, prev, next, controller, output);
            process.start();
            threads.add(process);
        }
        for (HSProcess thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
