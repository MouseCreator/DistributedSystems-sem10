package mouse.univ.io;

import mouse.univ.crypt.HashFunction;
import mouse.univ.crypt.SHA256;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;import java.util.UUID;

public class Utils {

    public static int getArgument(String[] args) {
        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index must be an integer");
        }

        return index;
    }

    public static String hashed(String number) {
        HashFunction function = new SHA256();
        return function.hash(number);
    }

    public static Process startProcess(String pkg, String className, String... args) {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(pkg + className);
        Collections.addAll(command, args);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[" + className + "]\t" + line);
                    }
                } catch (Exception ignored) {
                }
            }).start();
            return process;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void await(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
