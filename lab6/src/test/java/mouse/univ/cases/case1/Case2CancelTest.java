package mouse.univ.cases.case1;

import mouse.univ.coin.Utils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Case2CancelTest {

    private final static String PKG_NAME = "mouse.univ.cases.case2.";

    @Test
    void cancelCase() throws Exception {
        List<Process> processes = new ArrayList<>();
        try {
            processes.add(Utils.startProcess(PKG_NAME, "Alice", "0"));
            processes.add(Utils.startProcess(PKG_NAME, "Bob", "1"));
            processes.add(Utils.startProcess(PKG_NAME, "Carl", "2"));
            Thread.sleep(12000);
        } finally {
            for (Process p : processes) {
                p.destroy();
            }
        }

    }


}
