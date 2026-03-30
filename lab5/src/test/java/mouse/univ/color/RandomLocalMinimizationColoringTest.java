package mouse.univ.color;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomLocalMinimizationColoringTest {
    @Test
    void minimizeCompleteGraph() {
        int nodes = 50;
        ColoredGraphGenerator graphGenerator = new ColoredGraphGenerator(1, 0.5);
        ColoredGraph generated = graphGenerator.complete(nodes);
        RandomLocalMinimizationColoring orderedLocalMinimizationColoring = new RandomLocalMinimizationColoring(5, null, 100);
        ColoredGraph minimized = orderedLocalMinimizationColoring.minimize(generated);

        Set<ColoredVertex> allVertices = minimized.getAllVertices();
        int maxColor = 0;
        for (ColoredVertex v : allVertices) {
            maxColor = Math.max(v.getColor(), maxColor);
        }
        assertEquals(nodes - 1, maxColor);
        Util.assertCorrectColors(minimized);
    }

    @Test
    void minimizeUnconnectedGraph() {
        ColoredGraphGenerator graphGenerator = new ColoredGraphGenerator(1, 0.5);
        ColoredGraph generated = graphGenerator.unconnected(100);
        RandomLocalMinimizationColoring orderedLocalMinimizationColoring = new RandomLocalMinimizationColoring(5, null, 1000);
        ColoredGraph minimized = orderedLocalMinimizationColoring.minimize(generated);

        Set<ColoredVertex> allVertices = minimized.getAllVertices();
        for (ColoredVertex v : allVertices) {
            assertEquals(0, v.getColor());
        }
        Util.assertCorrectColors(minimized);
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500, 600})
    void minimizeRandomGraph(int nodes) {
        ColoredGraphGenerator graphGenerator = new ColoredGraphGenerator(32, 0.8);
        ColoredGraph generated = graphGenerator.generate(nodes);
        RandomLocalMinimizationColoring orderedLocalMinimizationColoring = new RandomLocalMinimizationColoring(5, null, 100000);
        ColoredGraph minimized = orderedLocalMinimizationColoring.minimize(generated);
        int edges = minimized.countEdges();
        int maxColor = minimized.maxColor();
        double theoreticalMax = Math.floor(3 + Math.sqrt(8 * edges - 4 * nodes + 1) / 2);
        System.out.println("Max color: " + maxColor);
        System.out.println("Theoretical max: " + theoreticalMax);
        assertTrue(maxColor <= theoreticalMax);
        Util.assertCorrectColors(minimized);
    }

    @Test
    void minimizeRandomSmallGraph() {
        int nodes = 5;
        ColoredGraphGenerator graphGenerator = new ColoredGraphGenerator(123, 0.5);
        ColoredGraph generated = graphGenerator.generate(nodes);
        RandomLocalMinimizationColoring orderedLocalMinimizationColoring = new RandomLocalMinimizationColoring(5, null, 100000);
        ColoredGraph minimized = orderedLocalMinimizationColoring.minimize(generated);
        int edges = minimized.countEdges();
        int maxColor = minimized.maxColor();
        double theoreticalMax = Math.floor(3 + Math.sqrt(Math.max(0, 8 * edges - 4 * nodes + 1)) / 2);
        System.out.println("Max color: " + maxColor);
        System.out.println("Theoretical max: " + theoreticalMax);
        assertTrue(maxColor <= theoreticalMax);
        Util.assertCorrectColors(minimized);
    }
}