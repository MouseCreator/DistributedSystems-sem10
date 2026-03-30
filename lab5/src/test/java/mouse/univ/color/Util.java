package mouse.univ.color;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Util {

    public static void assertCorrectColors(ColoredGraph coloredGraph) {
        Set<ColoredVertex> allVertices = coloredGraph.getAllVertices();
        for (ColoredVertex v : allVertices) {
            for (ColoredVertex n : v.getNeighbors()) {
                assertNotEquals(v.getColor(), n.getColor(), "Duplicate colors: " + v.getId() + ", " + n.getId());
            }
        }
    }
}
