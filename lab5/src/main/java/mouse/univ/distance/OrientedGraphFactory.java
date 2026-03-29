package mouse.univ.distance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrientedGraphFactory {
    private final Random random;

    public OrientedGraphFactory(Long seed) {
        if (seed == null) {
            random = new Random();
        } else {
            random = new Random(seed);
        }
    }

    public OrientedGraph createOrientedGraph(int numV) {
        OrientedGraph orientedGraph = new OrientedGraph();

        for (int i = 0; i < numV; i++) {
            orientedGraph.addVertex(new Vertex(i));
        }
        for (int i = 0; i < numV; i++) {
            for (int j = i + 1; j < numV; j++) {
                double cased = random.nextDouble(0, 1);
                if (cased < 0.2) {
                    orientedGraph.addOrientedEdge(new OrientedEdge(i, j, weight()));
                }
                else if (cased < 0.4) {
                    orientedGraph.addOrientedEdge(new OrientedEdge(j, i, weight()));
                } else if (cased < 0.8) {
                    orientedGraph.addOrientedEdge(new OrientedEdge(i, j, weight()));
                    orientedGraph.addOrientedEdge(new OrientedEdge(j, i, weight()));
                }
            }
        }
        return orientedGraph;
    }

    public OrientedGraph line(int numV) {
        OrientedGraph orientedGraph = new OrientedGraph();
        Vertex origin = new Vertex(0);
        orientedGraph.addVertex(origin);
        Vertex prev = origin;
        for (int i = 1; i < numV; i++) {
            Vertex current = new Vertex(i);
            orientedGraph.addVertex(current);
            orientedGraph.addOrientedEdge(new OrientedEdge(prev, current, weight()));
            prev = current;
        }
        return orientedGraph;
    }

    public OrientedGraph lineWithShortcut(int numV, boolean shortcutIsBetter) {
        OrientedGraph graph = line(numV);
        if (shortcutIsBetter) {
            graph.addOrientedEdge(new OrientedEdge(0, numV - 1, 1));
        } else {
            graph.addOrientedEdge(new OrientedEdge(0, numV - 1, 10 * numV + 20));
        }
        return graph;
    }

    private int weight() {
        return random.nextInt(1, 10);
    }

    public OrientedGraph createMultipathGraph(int length) {
        OrientedGraph orientedGraph = new OrientedGraph();
        int last = length - 1;
        Vertex origin = new Vertex(0);
        orientedGraph.addVertex(origin);
        Vertex lastV = new Vertex(last);
        orientedGraph.addVertex(lastV);
        Vertex prev = origin;

        List<Vertex> pathEndings = new ArrayList<>();
        int cycle = 0;
        for (int i = 1; i < length - 1; i++) {
            Vertex current = new Vertex(i);
            orientedGraph.addVertex(current);
            orientedGraph.addOrientedEdge(new OrientedEdge(prev, current, weight()));
            cycle++;
            if (cycle == 10 || i == length - 2) {
                pathEndings.add(current);
                prev = origin;
            } else {
                prev = current;
            }
        }
        for (Vertex ending : pathEndings) {
            orientedGraph.addOrientedEdge(new OrientedEdge(ending, lastV, weight()));
        }
        return orientedGraph;
    }
}
