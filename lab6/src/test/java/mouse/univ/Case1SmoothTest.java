package mouse.univ;

import mouse.univ.io.Utils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Case1SmoothTest {

    private final static String PKG_NAME = "mouse.univ.cases.case1.";

    @Test
    void successfulCase() throws Exception {
        List<Process> processes = new ArrayList<>();
        try {
            processes.add(Utils.startProcess(PKG_NAME, "Alice", "0"));
            processes.add(Utils.startProcess(PKG_NAME, "Bob", "1"));
            processes.add(Utils.startProcess(PKG_NAME, "Carl", "2"));
            Thread.sleep(10000);
        } finally {
            for (Process p : processes) {
                p.destroy();
            }
        }

    }


}
