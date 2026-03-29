package mouse.univ.color.gui;

import lombok.Data;
import mouse.univ.color.ColoredGraph;

import java.util.HashMap;

@Data
public class GraphModel {
    private final int width;
    private final int height;
    private final int radius;
    private ColoredGraph coloredGraph;
    private final HashMap<Integer, Position> positionHashMap;

    public GraphModel(int width, int height, int radius, ColoredGraph coloredGraph, HashMap<Integer, Position> positionHashMap) {
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.coloredGraph = coloredGraph;
        this.positionHashMap = positionHashMap;
    }

    public GraphModel duplicate() {
        return new GraphModel(
                width,
                height,
                radius,
                new ColoredGraph(coloredGraph.duplicate()),
                new HashMap<>(positionHashMap)
        );
    }
}
