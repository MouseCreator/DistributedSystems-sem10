package mouse.univ.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeFactory {

    public Tree pairs(List<String> pairs) {
        HashMap<String, TreeNode> nodes = new HashMap<>();
        TreeNode root = null;
        if (pairs.size() % 2 == 1) {
            throw new IllegalArgumentException("Cannot create list from pairs. Should be an even number of elements");
        }
        for (int i = 0; i < pairs.size(); i+=2) {

            String from = pairs.get(i);
            String to = pairs.get(i+1);

            TreeNode fromNode;
            TreeNode toNode;

            if (nodes.containsKey(from)) {
                fromNode = nodes.get(from);
            } else {
                fromNode = new TreeNode(from);
                nodes.put(from, fromNode);
            }
            if (nodes.containsKey(to)) {
                toNode = nodes.get(to);
            } else {
                toNode = new TreeNode(to);
                nodes.put(to, toNode);
            }

            if (i == 0) {
                root = fromNode;
            }
            fromNode.getNeighbors().add(toNode);
        }
        if (root == null) {
            throw new IllegalArgumentException("Tree cannot be empty");
        }
        assertNoCycle(root);
        return new Tree(root);
    }

    private void assertNoCycle(TreeNode node) {
        assertNoCycle(node, new HashSet<>());
    }
    private void assertNoCycle(TreeNode node, Set<String> visited) {
        if (visited.contains(node.getLabel())) {
            throw new IllegalStateException("Tree cannot contain a cycle");
        }
        visited.add(node.getLabel());
        for (TreeNode neighbor : node.getNeighbors()) {
            assertNoCycle(neighbor, visited);
        }
    }
}
