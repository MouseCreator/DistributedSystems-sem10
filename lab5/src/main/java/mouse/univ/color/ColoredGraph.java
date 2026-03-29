package mouse.univ.color;

import java.util.*;

public class ColoredGraph {

    private final Set<ColoredVertex> coloredVertices;

    public ColoredGraph(Collection<ColoredVertex> coloredVertices) {
        this.coloredVertices = new HashSet<>(coloredVertices);
    }

    public Set<ColoredVertex> getAllVertices() {
        return new HashSet<>(coloredVertices);
    }

    public Set<ColoredVertex> duplicate() {
        Set<ColoredVertex> result = new HashSet<>();
        HashMap<Integer, ColoredVertex> vertexHashMap = new HashMap<>();
        HashMap<Integer, List<Integer>> neighborsMap = new HashMap<>();
        for (ColoredVertex vertex : coloredVertices) {
            ColoredVertex newVertex = new ColoredVertex(vertex.getId(), vertex.getColor());
            vertexHashMap.put(vertex.getId(), newVertex);
            List<Integer> neighbors = new ArrayList<>();
            for (ColoredVertex n : vertex.getNeighbors()) {
                neighbors.add(n.getId());
            }
            neighborsMap.put(vertex.getId(), neighbors);
            result.add(newVertex);
        }
        for (ColoredVertex vertex : result) {
            for (Integer t : neighborsMap.get(vertex.getId())) {
                vertex.addNeighbor(vertexHashMap.get(t));
            }
        }
        return result;
    }

    public int countEdges() {
        double e = 0;
        for (ColoredVertex coloredVertex : coloredVertices) {
            e += (coloredVertex.getNeighbors().size()) / 2.0;
        }
        return (int) Math.round(e);

    }

    public int maxColor() {
        int maxColor = 0;
        for (ColoredVertex v : getAllVertices()) {
            maxColor = Math.max(v.getColor(), maxColor);
        }
        return maxColor;
    }
}
