package mouse.univ.algorithm;

import mouse.univ.model.Tree;
import mouse.univ.print.PrettyTreePrinter;
import mouse.univ.print.TreePrinter;
import org.junit.jupiter.api.Test;

import mouse.univ.model.TreeFactory;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MISTreeAlgorithmTest {

    private final MISTreeAlgorithm misTreeAlgorithm = new MISTreeAlgorithm();
    private final TreeFactory treeFactory = new TreeFactory();
    private final TreePrinter treePrinter = new PrettyTreePrinter();

    @Test
    void findMISExample() {
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
}