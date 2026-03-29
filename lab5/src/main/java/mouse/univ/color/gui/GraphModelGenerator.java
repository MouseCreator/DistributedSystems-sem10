package mouse.univ.color.gui;

import lombok.extern.log4j.Log4j2;
import mouse.univ.color.ColoredGraph;
import mouse.univ.color.ColoredVertex;

import java.util.*;

@Log4j2
public class GraphModelGenerator {

    private final int WIDTH;

    private final int HEIGHT;

    private final int RADIUS = 5;

    private final Random RANDOM;
    private final double ALLOCATION_CHANCE = 0.95;

    private static class Edge {
        Position p1;
        Position p2;

        public Edge(Position p1, Position p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public GraphModelGenerator(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        RANDOM = new Random(2);
    }

    public GraphModel generate(int numVertices) {
        HashMap<Integer, Position> positionHashMap = new HashMap<>();

        List<ColoredVertex> coloredVertexList = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            ColoredVertex vertex = new ColoredVertex(i, i);
            Position position = generatePosition(positionHashMap);
            if (position != null) {
                coloredVertexList.add(vertex);
                positionHashMap.put(i, position);
            }
        }
        int totalVertices = coloredVertexList.size();
        List<Edge> existingEdges = new ArrayList<>();
        int allowedDistance = (WIDTH + HEIGHT) / 10;
        for (int i = 0; i < totalVertices; i++) {
            for (int j = i + 1; j < totalVertices; j++) {
                ColoredVertex origin = coloredVertexList.get(i);
                ColoredVertex target = coloredVertexList.get(j);
                Position originPosition = positionHashMap.get(origin.getId());
                Position targetPosition = positionHashMap.get(target.getId());
                if (Geometry.distancePoints(originPosition, targetPosition) < allowedDistance) {
                    double allocate = RANDOM.nextDouble(0, 1);
                    if (allocate <= ALLOCATION_CHANCE) {
                        if (canDrawEdge(origin.getId(), target.getId(), positionHashMap, existingEdges)) {
                            origin.addNeighbor(target);
                            existingEdges.add(new Edge(originPosition, targetPosition));
                        }
                    }
                }

            }

        }

        return new GraphModel(WIDTH, HEIGHT, RADIUS, new ColoredGraph(coloredVertexList), positionHashMap);
    }

    private boolean canDrawEdge(int i, int j, HashMap<Integer, Position> positions, List<Edge> existingEdges) {
        Position originPosition = positions.get(i);
        Position targetPosition = positions.get(j);
        for (Integer k : positions.keySet()) {
            if (k == i || k == j) {
                continue;
            }
            double v = Geometry.distancePointToSegment(positions.get(k), originPosition, targetPosition);
            if (v < RADIUS) {
                return false;
            }
        }
        for (Edge edge: existingEdges) {
            if (Geometry.segmentIntersect(originPosition, targetPosition, edge.p1, edge.p2)) {
                return false;
            }
        }
        return true;
    }

    private Position generatePosition(HashMap<Integer, Position> positionsMap) {
        HashSet<Position> positions = new HashSet<>(positionsMap.values());
        for (int i = 0; i < 100; i++) {
            int x = RANDOM.nextInt(RADIUS, WIDTH-RADIUS);
            int y = RANDOM.nextInt(RADIUS,HEIGHT-RADIUS);
            Position position = new Position(x, y);
            boolean success = true;
            for (Position existing: positions) {
                double v = Geometry.distancePoints(position, existing);
                if (v < RADIUS) {
                    success = false;
                    break;
                }
            }
            if (success) {
                return position;
            }
        }
        return null;

    }
}
