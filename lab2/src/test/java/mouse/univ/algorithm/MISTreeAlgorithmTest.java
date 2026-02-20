package mouse.univ.algorithm;

import mouse.univ.Stopwatch;
import mouse.univ.model.Tree;
import mouse.univ.model.TreeNode;
import mouse.univ.print.TreePrinter;
import mouse.univ.print.TreePrinterFactory;
import org.junit.jupiter.api.Test;

import mouse.univ.model.TreeFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MISTreeAlgorithmTest {

    private final MISTreeAlgorithm misTreeAlgorithm = new MISTreeAlgorithm();
    private final TreeFactory treeFactory = new TreeFactory();
    private final TreePrinter treePrinter = TreePrinterFactory.pretty();

    @Test
    void findMISStar() {
        Tree tree = treeFactory.pairs(
                List.of(
                        "A", "B",
                        "A", "C",
                        "A", "D",
                        "A", "E"
                )
        );
        treePrinter.print(tree);
        Set<String> mis = misTreeAlgorithm.findMIS(tree);
        assertEquals(4, mis.size());
        assertEquals(Set.of("B","C","D", "E"), mis);
    }

    @Test
    void findMISList() {
        Tree tree = treeFactory.pairs(
                List.of(
                        "A", "B",
                        "B", "C",
                        "C", "D",
                        "D", "E"
                )
        );
        treePrinter.print(tree);
        Set<String> mis = misTreeAlgorithm.findMIS(tree);
        assertEquals(3, mis.size());
        assertEquals(Set.of("A","C","E"), mis);
    }

    @Test
    void findMISTree() {
        Tree tree = treeFactory.pairs(
                List.of(
                        "A", "B",
                        "A", "C",
                        "A", "D",
                        "C", "E",
                        "C", "F",
                        "C", "G",
                        "D", "H",
                        "D", "I"
                )
        );
        treePrinter.print(tree);
        Set<String> mis = misTreeAlgorithm.findMIS(tree);
        assertEquals(6, mis.size());
        assertEquals(Set.of("B","E","F","G","H","I"), mis);
    }

    @Test
    void findMISExtendedTree() {
        Tree tree = treeFactory.pairs(
                List.of(
                        "A", "B",
                        "A", "C",
                        "A", "D",
                        "C", "E",
                        "C", "F",
                        "C", "G",
                        "D", "H",
                        "D", "I",
                        "B", "J"
                )
        );
        treePrinter.print(tree);
        Set<String> mis = misTreeAlgorithm.findMIS(tree);
        assertEquals(7, mis.size());
        assertEquals(Set.of("A","E","F","G","H","I","J"), mis);
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 1000, 10_000, 100_000, 250_000, 500_000, 1_000_000})
    void findMISList(int size) {
        Tree tree = treeFactory.ofSize(size);
        Set<String> mis = Stopwatch.measure(() -> misTreeAlgorithm.findMIS(tree));
        assertTrue(mis.size() < size);
        assertIndependent(tree, mis);
    }

    private void assertIndependent(Tree tree, Set<String> mis) {
        TreeNode root = tree.getRoot();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode current = queue.pollFirst();

            if (mis.contains(current.getLabel())) {
                for (TreeNode neighbor : current.getNeighbors()) {
                    assertFalse(mis.contains(neighbor.getLabel()));
                }
            }

            for (TreeNode neighbor : current.getNeighbors()) {
                queue.addLast(neighbor);
            }
        }
    }
}