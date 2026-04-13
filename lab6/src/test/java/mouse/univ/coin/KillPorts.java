package mouse.univ.coin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class KillPorts {

    private static final int[] PORTS = {
            7000, 7001, 7002,
            7010, 7011, 7012,
            7020, 7021, 7022
    };

    public static void main(String[] args) {
        for (int port : PORTS) {
            try {
                Set<String> pids = getPidsUsingPort(port);

                if (pids.isEmpty()) {
                    System.out.println("Port " + port + " is not in use.");
                } else {
                    for (String pid : pids) {
                        killProcess(pid, port);
                    }
                }

            } catch (Exception e) {
                System.err.println("Error handling port " + port + ": " + e.getMessage());
            }
        }
    }

    private static Set<String> getPidsUsingPort(int port) throws Exception {
        Set<String> pids = new HashSet<>();

        Process process = Runtime.getRuntime().exec(
                "cmd /c netstat -ano | findstr :" + port
        );

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\s+");
                String pid = parts[parts.length - 1];
                pids.add(pid);
            }
        }

        return pids;
    }

    private static void killProcess(String pid, int port) throws Exception {
        System.out.println("Killing PID " + pid + " on port " + port);

        Process process = Runtime.getRuntime().exec(
                "cmd /c taskkill /PID " + pid + " /F"
        );

        process.waitFor();

        System.out.println("Killed PID " + pid);
    }
}
