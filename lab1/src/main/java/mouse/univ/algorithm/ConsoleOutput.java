package mouse.univ.algorithm;

public class ConsoleOutput implements Output {
    @Override
    public void write(String message) {
        System.out.println(message);
    }
}
