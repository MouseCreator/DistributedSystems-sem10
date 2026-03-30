package mouse.univ.color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColoredGraphGenerator {
    private final Random random;
    private final double connectivityChance;

    public ColoredGraphGenerator(Integer seed, double connectivityChance) {
        if (seed == null) {
            random = new Random();
        } else {
            random = new Random(seed);
        }
        this.connectivityChance = connectivityChance;
    }

    public ColoredGraph generate(int numV) {
        List<ColoredVertex> coloredVertexList = new ArrayList<>();

        for (int i = 0; i < numV; i++) {
            ColoredVertex v = new ColoredVertex(i, i);
            for (int j = 0; j < i; j++) {
                if (random.nextDouble(0,1) < connectivityChance) {
                    v.addNeighbor(coloredVertexList.get(j));
                }
            }
            coloredVertexList.add(v);
        }

        return new ColoredGraph(coloredVertexList);
    }

    public ColoredGraph complete(int numV) {
        List<ColoredVertex> coloredVertexList = new ArrayList<>();

        for (int i = 0; i < numV; i++) {
            ColoredVertex v = new ColoredVertex(i, i);
            for (int j = 0; j < i; j++) {
                v.addNeighbor(coloredVertexList.get(j));
            }
            coloredVertexList.add(v);
        }
        return new ColoredGraph(coloredVertexList);
    }

    public ColoredGraph unconnected(int numV) {
        List<ColoredVertex> coloredVertexList = new ArrayList<>();

        for (int i = 0; i < numV; i++) {
            ColoredVertex v = new ColoredVertex(i, i);
            coloredVertexList.add(v);
        }

        return new ColoredGraph(coloredVertexList);
    }

    public ColoredGraph generateLine(int nodes) {
        List<ColoredVertex> coloredVertexList = new ArrayList<>();
        ColoredVertex prev = new ColoredVertex(0, 0);
        coloredVertexList.add(prev);
        for (int i = 0; i < nodes; i++) {
            ColoredVertex v = new ColoredVertex(i, i);
            v.addNeighbor(prev);
            coloredVertexList.add(v);
            prev = v;
        }
        return new ColoredGraph(coloredVertexList);
    }

    public ColoredGraph generateStar(int nodes) {
        List<ColoredVertex> coloredVertexList = new ArrayList<>();
        ColoredVertex first = new ColoredVertex(0, 0);
        coloredVertexList.add(first);
        for (int i = 1; i < nodes; i++) {
            ColoredVertex v = new ColoredVertex(i, i);
            v.addNeighbor(first);
            coloredVertexList.add(v);
        }
        return new ColoredGraph(coloredVertexList);
    }
}
