package mouse.univ.distance;

import lombok.Data;

@Data
public class OrientedEdge {
    private final Vertex from;
    private final int weight;
    private final Vertex to;

    public OrientedEdge(int i, int j, int weight) {
        from = new Vertex(i);
        to = new Vertex(j);
        this.weight = weight;
    }

    public OrientedEdge(Vertex prev, Vertex current, int weight) {
        this.from = prev;
        this.to = current;
        this.weight = weight;
    }
}
