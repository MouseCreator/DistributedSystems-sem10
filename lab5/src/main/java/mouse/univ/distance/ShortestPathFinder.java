package mouse.univ.distance;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortestPathFinder {

    public HashMap<Integer, Integer> findShortestPath(OrientedGraph orientedGraph, Integer startId) {
        HashMap<Integer, Integer> initialDistances = initDistances(orientedGraph, startId);
        List<Vertex> allVertices = orientedGraph.allVertices();
        ShortestPathState state = new ShortestPathState(orientedGraph, initialDistances);

        boolean lead = true;
        for (Vertex v : allVertices) {
            final boolean isLeader = lead;
            Runnable work = () -> worker(isLeader, v, state);
            lead = false;
            Thread thread = new Thread(work);
            thread.start();
        }

        try {
            state.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new HashMap<>(state.distances);
    }

    private HashMap<Integer, Integer> initDistances(OrientedGraph orientedGraph, Integer startId) {
        List<Vertex> vertices = orientedGraph.allVertices();
        HashMap<Integer, Integer> result = new HashMap<>();
        for (Vertex v : vertices) {
            if (v.getId() == startId) {
                result.put(v.getId(), 0);
            } else {
                result.put(v.getId(), -1);
            }
        }
        return result;
    }

    private static class ShortestPathState {
        OrientedGraph graph;
        ConcurrentHashMap<Integer, Integer> distances;
        int maxIter;
        AtomicInteger numIter;
        CyclicBarrier barrier;
        CountDownLatch latch;

        public boolean isComplete() {
            return numIter.get() >= maxIter;
        }

        public ShortestPathState(OrientedGraph graph, HashMap<Integer, Integer> dist) {
            this.graph = graph;
            this.maxIter = graph.allVertices().size();
            this.barrier = new CyclicBarrier(maxIter);
            numIter = new AtomicInteger(0);
            distances = new ConcurrentHashMap<>(dist);
            latch = new CountDownLatch(maxIter);
        }
    }

    private void worker(boolean leader, Vertex myVertex, ShortestPathState state) {
        List<OrientedEdge> in = state.graph.getIn(myVertex);

        HashMap<Integer, Integer> edgeWeights = new HashMap<>();
        for (OrientedEdge edge : in) {
            edgeWeights.put(edge.getFrom().getId(), edge.getWeight());
        }

        int localMinimum = state.distances.get(myVertex.getId());
        while (! state.isComplete()) {
            int minDistance = localMinimum;
            for (Vertex neighbor : state.graph.allVertices()) {
                if (!edgeWeights.containsKey(neighbor.getId())) {
                    continue;
                }
                Integer dist = state.distances.get(neighbor.getId());
                if (dist == -1) {
                    continue;
                }
                int candidate = dist + edgeWeights.get(neighbor.getId());
                if (minDistance == -1) {
                    minDistance = candidate;
                } else {
                    minDistance = Math.min(minDistance, candidate);
                }
            }
            localMinimum = minDistance;
            state.distances.put(myVertex.getId(), localMinimum);

            await(state);
            if (leader) {
                state.numIter.incrementAndGet();
            }
            await(state);
        }
        state.latch.countDown();
    }

    private static void await(ShortestPathState state) {
        try {
            state.barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
