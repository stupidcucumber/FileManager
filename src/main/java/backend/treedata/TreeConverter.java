package backend.treedata;

import backend.CustomFile;
import javafx.scene.control.TreeItem;

public interface TreeConverter {
    TreeItem<CustomFile> convert(TreeNode treeNode);
}
