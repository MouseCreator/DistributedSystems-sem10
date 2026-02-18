package mouse.univ.algorithm;

import mouse.univ.model.Tree;
import mouse.univ.model.TreeNode;

import java.util.*;

public class MISTreeAlgorithm {

    private static class State {
        private final Map<String, Integer> a;
        private final Map<String, Integer> b;
        private final Set<String> result;

        public State() {
            a = new HashMap<>();
            b = new HashMap<>();
            result = new HashSet<>();
        }
    }

    public Set<String> findMIS(Tree tree) {
        TreeNode root = tree.getRoot();
        State state = new State();
        processNode(root, state);
        return new HashSet<>(state.result);
    }

    private void processNode(TreeNode node, State state) {
        List<TreeNode> neighbors = node.getNeighbors();
        for (TreeNode neighbor : neighbors) {
            processNode(neighbor, state);
        }

        int a = 1;
        int b = 0;

        for (TreeNode neighbor : neighbors) {
            Integer an = state.a.get(neighbor.getLabel());
            Integer bn = state.b.get(neighbor.getLabel());
            b += Math.max(an, bn);
            a += bn;
        }
        String label = node.getLabel();
        state.a.put(label, a);
        state.b.put(label, b);

        if (a > b) {
            state.result.add(label);
        }
    }
}
