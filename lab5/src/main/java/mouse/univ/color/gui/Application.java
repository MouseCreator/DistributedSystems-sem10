package mouse.univ.color.gui;

import mouse.univ.color.Coloring;
import mouse.univ.color.OrderedLocalMinimizationColoring;

public class Application {
    public static void main(String[] args) {
        Coloring coloring = new OrderedLocalMinimizationColoring(2);
        GUIPipeline.run(600, 600, 100, coloring, false);
    }
}
