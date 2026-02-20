package mouse.univ.print;

public class TreePrinterFactory {
    public static MockTreePrinter mock() {
        return new MockTreePrinter();
    }
    public static PrettyTreePrinter pretty() {
        return new PrettyTreePrinter();
    }
}
