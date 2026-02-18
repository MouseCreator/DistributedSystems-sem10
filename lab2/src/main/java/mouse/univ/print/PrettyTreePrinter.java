package mouse.univ.print;

import mouse.univ.model.Tree;
import mouse.univ.model.TreeNode;

import java.util.*;
import java.util.stream.Collectors;

public class PrettyTreePrinter implements TreePrinter {

    public void print(Tree root) {
        System.out.println(convert(root));
    }

    public String convert(Tree tree) {
        TreeNode root = tree.getRoot();
        if (root == null) return "";

        List<StringBuilder> lines = new ArrayList<>();
        lines.add(new StringBuilder());
        Set<TreeNode> visited = Collections.newSetFromMap(new IdentityHashMap<>());
        visited.add(root);

        printInline(root, null, visited, lines, 0);

        return lines.stream().map(StringBuilder::toString).collect(Collectors.joining("\n"));
    }

    private void printInline(
            TreeNode node,
            TreeNode parent,
            Set<TreeNode> visited,
            List<StringBuilder> lines,
            int lineIndex
    ) {
        StringBuilder sb = lines.get(lineIndex);
        int labelStartCol = sb.length();
        sb.append(node.getLabel());

        int edgeDashCol = labelStartCol + node.getLabel().length() + 1;

        List<TreeNode> children = childrenOf(node, parent, visited);

        if (children.isEmpty()) return;

        sb.append(" --- ");
        TreeNode first = children.getFirst();
        visited.add(first);
        printInline(first, node, visited, lines, lineIndex);

        for (int i = 1; i < children.size(); i++) {
            TreeNode child = children.get(i);
            visited.add(child);

            StringBuilder childLine = new StringBuilder();
            childLine.append(spaces(edgeDashCol));
            childLine.append("--- ");
            int newLineIndex = lines.size();
            lines.add(childLine);

            printInline(child, node, visited, lines, newLineIndex);
        }
    }

    private List<TreeNode> childrenOf(TreeNode node, TreeNode parent, Set<TreeNode> visited) {
        return node.getNeighbors().stream()
                .filter(n -> n != parent)
                .filter(n -> !visited.contains(n))
                .sorted(Comparator.comparing(TreeNode::getLabel))
                .toList();
    }

    private String spaces(int count) {
        if (count <= 0) return "";
        return " ".repeat(count);
    }
}
