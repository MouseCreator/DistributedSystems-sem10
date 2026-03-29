package mouse.univ.distance;

import java.util.List;

public interface DistanceCalculator {
    List<Distance> find(OrientedGraph graph, String startVertexId);
}
