package mouse.univ.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode {
    private final List<TreeNode> neighbors;
    private final String label;
    public TreeNode(String label) {
        this.neighbors = new ArrayList<>();
        this.label = label;
    }
}
