package backend.treedata;

import backend.CustomFile;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private final CustomFile customFile;
    private final List<TreeNode> treeNodeList;

    public TreeNode(CustomFile file) {
        customFile = file;
        treeNodeList = new ArrayList<>();
    }

    public List<TreeNode> getTreeNodeList() {
        return treeNodeList;
    }

    public CustomFile getCustomFile() {
        return customFile;
    }

    public void add(TreeNode treeNode) {
        treeNodeList.add(treeNode);
    }
}
