package mouse.univ.model;

import lombok.Data;

@Data
public class Tree {
    private final TreeNode root;
    public Tree(TreeNode root) {
        this.root = root;
    }
}
