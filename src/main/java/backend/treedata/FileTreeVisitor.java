package backend.treedata;

import backend.CustomFile;

import java.io.File;

public class FileTreeVisitor implements TreeVisitor{
    private final TreeNode treeRoot;

    private final String path;

    public FileTreeVisitor(String path) {
        this.path = path;
        treeRoot = new TreeNode(new CustomFile(path));
    }

    private void traverseTree(CustomFile file, TreeNode treeRoot) {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }

        for (File c_file : files) {
            if (validate(c_file)) {
                if (c_file.isDirectory()) {
                    TreeNode directory = new TreeNode(new CustomFile(c_file.getPath()));
                    treeRoot.add(directory);
                    traverseTree(new CustomFile(c_file.getPath()), directory);
                } else {
                    treeRoot.add(new TreeNode(new CustomFile(c_file.getPath())));
                }
            }
        }
    }

    private boolean validate(File file) {
        if (file.isHidden()) {
            return false;
        } else if (file.isDirectory()) {
            if (file.getName().equals("Applications")) {
                return false;
            } else if (file.getName().equals("IdeaProjects")) {
                return false;
            } else if (file.getName().equals("opt")) {
                return false;
            } else if (file.getName().equals("Library")) {
                return false;
            } else if (file.getName().equals("Pictures")) {
                return false;
            }
        }

        return true;
    }

    public TreeNode buildTree() {
        traverseTree(new CustomFile(path), treeRoot);
        return treeRoot;
    }

    public TreeNode buildTree(String format) {
        findFiles(format, new CustomFile(path), treeRoot);
        return treeRoot;
    }

    private void findFiles(String format, CustomFile file, TreeNode treeRoot) {
        File[] files = file.listFiles();

        if (files == null) {
            return;
        }

        for (File c_file : files) {
            if (validate(c_file)) {
                if (c_file.isDirectory()) {
                    findFiles(format, new CustomFile(c_file.getPath()), treeRoot);
                } else {
                    String[] temp = c_file.getName().split("\\.");
                    if (temp[temp.length - 1].equals(format)) {
                        treeRoot.add(new TreeNode(new CustomFile(c_file.getPath())));
                    }
                }
            }
        }
    }
    /**
     *

     private void traverseTree(File file, TreeItem<File> treeItem) {
     File[] files = file.listFiles();
     if (files == null) {
     return;
     }

     for (File c_file : files) {
     if (c_file.isDirectory()) {
     TreeItem<File> newDirectory = new TreeItem<>(new CustomFile(c_file.getPath()));
     treeItem.getChildren().add(newDirectory);
     traverseTree(c_file, newDirectory);
     } else if (c_file.isFile()) {
     treeItem.getChildren().add(new TreeItem<>(new CustomFile(c_file.getPath())));
     }
     }
     }
     * @param file
     * @param treeItem
     */
}
