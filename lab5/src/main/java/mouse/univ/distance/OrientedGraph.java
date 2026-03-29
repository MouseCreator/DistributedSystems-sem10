package mouse.univ.distance;

import java.util.*;

public class OrientedGraph {

    private final List<Vertex> vertices;

    private final HashMap<Integer, List<OrientedEdge>> out;
    private final HashMap<Integer, List<OrientedEdge>> in;

    public OrientedGraph() {
        vertices = new ArrayList<>();
        out = new HashMap<>();
        in = new HashMap<>();
    }

    public List<Vertex> allVertices() {
        return vertices;
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
        out.put(v.getId(), new ArrayList<>());
        in.put(v.getId(), new ArrayList<>());
    }

    public void addOrientedEdge(OrientedEdge edge) {
        Vertex edgeFrom = edge.getFrom();
        Vertex edgeTo = edge.getTo();
        try {
            out.get(edgeFrom.getId()).add(edge);
            in.get(edgeTo.getId()).add(edge);
        } catch (Exception e) {
            throw new IllegalArgumentException("Edge contains unknown vertex: " + edge, e);
        }
    }

    public List<OrientedEdge> getOut(Vertex v) {
        return out.get(v.getId());
    }
    public List<OrientedEdge> getIn(Vertex v) {
        return in.get(v.getId());
    }

    public OrientedEdge findEdge(int i, int i1) {
        return out.get(i).stream().filter(o -> o.getTo().getId() == i1).findAny().orElse(null);
    }
}
