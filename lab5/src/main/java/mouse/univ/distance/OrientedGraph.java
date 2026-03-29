package mouse.univ.distance;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrientedGraph {

    private final HashMap<String, Vertex> vertices;

    public OrientedGraph() {
        this.vertices = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        vertices.put(v.id, v);
    }

    private Set<Vertex> getVertices() {
        return new HashSet<>(vertices.values());
    }

    public Vertex getVertex(String startVertexId) {
        return vertices.get(startVertexId);
    }
    @Data
    public static class Edge {
        Vertex to;
        private int weight;
    }
    @Data
    public static class Vertex {
        private String id;
        private List<Edge> edgeList;
    }
}
