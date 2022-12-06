package backend.treedata;

import backend.CustomFile;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.util.List;

public class FileTreeConverter implements TreeConverter{

    @Override
    public TreeItem<CustomFile> convert(TreeNode treeNode) {
        TreeItem<CustomFile> root = new TreeItem<>(treeNode.getCustomFile());
        traverseTree(treeNode, root);

        return root;
    }

    private void traverseTree(TreeNode treeNode, TreeItem<CustomFile> treeItem) {
        List<TreeNode> nodes = treeNode.getTreeNodeList();

        if (nodes == null) {
            return;
        }

        for (TreeNode node : nodes) {
            if (node.getCustomFile().isDirectory()) {
                TreeItem<CustomFile> newDirectory = new TreeItem<>(node.getCustomFile());
                treeItem.getChildren().add(newDirectory);
                newDirectory.setGraphic(new ImageView(node.getCustomFile().getIcon()));
                traverseTree(node, newDirectory);
            } else {
                TreeItem<CustomFile> fileTreeItem = new TreeItem<>(node.getCustomFile());
                fileTreeItem.setGraphic(new ImageView(node.getCustomFile().getIcon()));
                treeItem.getChildren().add(fileTreeItem);
            }
        }
    }
}
