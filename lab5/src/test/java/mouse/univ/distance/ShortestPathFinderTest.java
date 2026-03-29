package mouse.univ.distance;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShortestPathFinderTest {
    private final OrientedGraphFactory factory = new OrientedGraphFactory(123L);
    private final ShortestPathFinder finder = new ShortestPathFinder();

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500})
    void findShortestPathLine(int length) {
        OrientedGraph orientedGraph = factory.line(length);
        HashMap<Integer, Integer> shortestPath = finder.findShortestPath(orientedGraph, 0);
        assertEquals(0, shortestPath.get(0));
        int sum = 0;
        for (int i = 1; i < length; i++) {
            OrientedEdge edge = orientedGraph.findEdge(i - 1, i);
            sum += edge.getWeight();
            assertEquals(sum, shortestPath.get(i));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500})
    void findShortestPathWithGoodShortcut(int length) {
        OrientedGraph orientedGraph = factory.lineWithShortcut(length, true);
        HashMap<Integer, Integer> shortestPath = finder.findShortestPath(orientedGraph, 0);
        int shortcutWeight = orientedGraph.findEdge(0, length - 1).getWeight();
        assertEquals(0, shortestPath.get(0));
        assertEquals(shortcutWeight, shortestPath.get(length-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500})
    void findShortestPathWithBadShortcut(int length) {
        OrientedGraph orientedGraph = factory.lineWithShortcut(length, false);
        HashMap<Integer, Integer> shortestPath = finder.findShortestPath(orientedGraph, 0);
        assertEquals(0, shortestPath.get(0));
        int sum = 0;
        for (int i = 1; i < length; i++) {
            OrientedEdge edge = orientedGraph.findEdge(i - 1, i);
            sum += edge.getWeight();
        }
        assertEquals(sum, shortestPath.get(length-1));
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500})
    void findShortestPathRandomGraph(int length) {
        OrientedGraph orientedGraph = factory.createOrientedGraph(length);
        HashMap<Integer, Integer> shortestPath = finder.findShortestPath(orientedGraph, 0);
        assertEquals(0, shortestPath.get(0));
        Set<Vertex> reachable = findReachable(orientedGraph);
        for (Vertex v : reachable) {
            assertNotEquals(-1, shortestPath.get(v.getId()));
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {20, 50, 100, 250, 500})
    void findShortestPathMultipathGraph(int length) {
        OrientedGraph orientedGraph = factory.createMultipathGraph(length);
        HashMap<Integer, Integer> shortestPath = finder.findShortestPath(orientedGraph, 0);
        assertEquals(0, shortestPath.get(0));
        List<Integer> sumPaths = sumOfPaths(orientedGraph);
        assertNotEquals(-1, shortestPath.get(length-1));
        assertEquals(sumPaths.stream().mapToInt(i->i).min().orElse(-1), shortestPath.get(length-1));
    }

    private List<Integer> sumOfPaths(OrientedGraph orientedGraph) {
        List<Integer> sums = new ArrayList<>();
        List<OrientedEdge> origins = orientedGraph.getOut(new Vertex(0));
        for (OrientedEdge origin : origins) {
            Vertex current = origin.getTo();
            int sum = origin.getWeight();
            System.out.println(">" + origin.getFrom().getId() + " --- " + origin.getTo().getId() + " > " + origin.getWeight());
            while (true) {
                List<OrientedEdge> out = orientedGraph.getOut(current);
                if (out.isEmpty()) {
                    break;
                }
                OrientedEdge edge = out.getFirst();
                System.out.println(">" + edge.getFrom().getId() + " --- " + edge.getTo().getId() + " > " + edge.getWeight());
                sum += edge.getWeight();
                current = edge.getTo();
            }
            sums.add(sum);
        }
        return sums;
    }

    private Set<Vertex> findReachable(OrientedGraph orientedGraph) {
        Set<Vertex> visited = new HashSet<>();
        Queue<Vertex> queue = new ArrayDeque<>();
        Vertex origin = orientedGraph.allVertices().getFirst();
        queue.add(origin);
        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            visited.add(current);
            for (OrientedEdge orientedEdge : orientedGraph.getOut(current)) {
                if (!visited.contains(orientedEdge.getTo())) {
                    queue.add(orientedEdge.getTo());
                }
            }
        }
        return visited;
    }
}