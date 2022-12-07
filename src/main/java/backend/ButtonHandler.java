package backend;

import backend.treedata.FileTreeConverter;
import backend.treedata.FileTreeVisitor;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import windows.*;
import windows.editor.EditorFactory;
import windows.editor.ImageEditorFactory;
import windows.editor.TextEditorFactory;
import windows.pathgetter.CopyPathGetterFactory;
import windows.pathgetter.MergePathGetterFactory;
import windows.pathgetter.PathGetter;
import windows.pathgetter.PathGetterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ButtonHandler {
    private static PathGetterFactory pathGetterFactory;
    private static TreeView<CustomFile> leftTreeView;
    private static TreeView<CustomFile> rightTreeView;
    private static TreeView<CustomFile> leftTreeSearchView;
    private static TreeView<CustomFile> rightTreeSearchView;
    private static String leftSearch;
    private static String rightSearch;

    public static void handleOnSearchLeft() {
        FileTreeVisitor visitor = new FileTreeVisitor("/Users/ihorkostiuk/");
        FileTreeConverter converter = new FileTreeConverter();
        if (leftSearch != null) {
            leftTreeSearchView.setRoot(converter.convert(visitor.buildTree(leftSearch)));
        } else {
            leftTreeSearchView.setRoot(null);
        }
    }

    public static void handleOnSearchRight() {
        FileTreeVisitor visitor = new FileTreeVisitor("/Users/ihorkostiuk/");
        FileTreeConverter converter = new FileTreeConverter();
        if (rightSearch != null) {
            rightTreeSearchView.setRoot(converter.convert(visitor.buildTree(rightSearch)));
        } else {
            rightTreeSearchView.setRoot(null);
        }
    }

    public static void handleFormatChooseLeft(ComboBox<String> comboBox) {
        leftSearch
                = comboBox.getSelectionModel().getSelectedItem().toLowerCase();
        System.out.println(leftSearch);
    }

    public static void handleFormatChooseRight(ComboBox<String> comboBox) {
        rightSearch
                = comboBox.getSelectionModel().getSelectedItem().toLowerCase();
        System.out.println(rightSearch);
    }

    public static void handleUpdateButtonClick() {
        updateView();
    }

    private static void updateView() {
        FileTreeConverter converter = new FileTreeConverter();

        rightTreeSearchView.setRoot(null);
        leftTreeSearchView.setRoot(null);

        FileTreeVisitor visitor_1
                = new FileTreeVisitor("/Users/ihorkostiuk/");
        FileTreeVisitor visitor_2
                = new FileTreeVisitor("/Users/ihorkostiuk");

        leftTreeView.setRoot(converter.convert(visitor_1.buildTree()));
        rightTreeView.setRoot(converter.convert(visitor_2.buildTree()));
    }

    public static void handleContextMenuChooserLeft(ActionEvent event) {
        TreeItem<CustomFile> selectedItem
                = leftTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        MenuItem selectedMenu = (MenuItem) event.getTarget();
        //TODO: Window with creation choosing a name for a directory
        switch (selectedMenu.getText()) {
            case "Create folder" -> createFolder(selectedItem);
            case "Delete" -> delete(selectedItem);
            case "Copy" -> copyFile(selectedItem);

            case "Open in editor" -> handleMenuButtonOpenFile(selectedItem);
        }
    }

    private static void renameTo() {

    }

    private static void createFolder(TreeItem<CustomFile> selectedFolder) {
        Random random = new Random(System.currentTimeMillis());
        CustomFile file = selectedFolder.getValue();
        File newFile = new CustomFile(file.getPath()
                + "/NewFolder" + random.nextInt(100000));
        if (newFile.mkdir()) {
            TreeItem<CustomFile> temp
                    = new TreeItem<>(new CustomFile(newFile.getPath()));
            selectedFolder.getChildren().add(temp);
            temp.setGraphic(new ImageView(temp.getValue().getIcon()));
        }

        updateView();
    }

    private static void delete(TreeItem<CustomFile> selectedFile) {
        TreeItem<CustomFile> parent = selectedFile.getParent();
        CustomFile customFile = selectedFile.getValue();
        if (customFile.isDirectory()) {
            recursive_deleting(customFile);
        } else {
            if (!customFile.delete()) {
                System.out.println("Wow");
            } else {
                parent.getChildren().remove(selectedFile);
            }
        }

        updateView();
    }

    private static void recursive_deleting(File c_file) {
        if (c_file == null) {
            return;
        }

        File[] files = c_file.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                recursive_deleting(file);

            }

            if (!file.delete()) {
                System.out.println("Something went wrong with recursive deleting!");
            } else {
                System.out.println("All good!");
            }
        }
    }

    private static void copyFile(TreeItem<CustomFile> selectedFile) {
        pathGetterFactory = new CopyPathGetterFactory();
        PathGetter pathGetter = pathGetterFactory.instantiatePathGetter();

        String[] temp = selectedFile.getValue().getName().split("\\.");
        String format = temp[temp.length - 1];

        String path = pathGetter.launch() + "." + format;

        CustomFile customFile = new CustomFile(path);

        if (customFile.exists()) {
            return;
        } else {
            try {
                customFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CopyMaker copyMaker = new CopyMaker(selectedFile.getValue(), customFile);
        try {
            copyMaker.copyFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateView();
    }

    public static void handleSearchFromHtmlButtonLeft() {
        TreeItem<CustomFile> selectedItem_1
                = leftTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem_1 == null) {
            return;
        }

        String[] format_1 = selectedItem_1.getValue().getName().split("\\.");

        String f_1 = format_1[format_1.length - 1];

        TreeItem<CustomFile> newRoot
                = new TreeItem<>(new CustomFile("/Users/ihorkostiuk"));

        if (f_1.equals("html")) {
            try {
                parseHTML(selectedItem_1.getValue(), newRoot);
                leftTreeSearchView.setRoot(newRoot);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Only absolute paths for images in html-file
     * @param customFile
     * @return
     * @throws FileNotFoundException
     */
    private static void parseHTML(CustomFile customFile, TreeItem<CustomFile> root)
            throws FileNotFoundException {
        Set<String> fileNames = new HashSet<>();
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']([^\"^']*)",
                Pattern.CASE_INSENSITIVE);

        Scanner scanner = new Scanner(customFile);
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                fileNames.add(matcher.group(1));
            }
        }

        for (String path : fileNames) {
            CustomFile c_file = new CustomFile(path);
            if (c_file.exists()) {
                TreeItem<CustomFile> temp = new TreeItem<>(new CustomFile(path));
                temp.setGraphic(new ImageView(temp.getValue().getIcon()));
                root.getChildren().add(temp);
            }
        }
    }

    public static void handleSearchFromHtmlButtonRight() {
        TreeItem<CustomFile> selectedItem_1
                = rightTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem_1 == null) {
            return;
        }

        String[] format_1 = selectedItem_1.getValue().getName().split("\\.");

        String f_1 = format_1[format_1.length - 1];

        TreeItem<CustomFile> newRoot
                = new TreeItem<>(new CustomFile("/Users/ihorkostiuk"));

        if (f_1.equals("html")) {
            try {
                parseHTML(selectedItem_1.getValue(), newRoot);
                rightTreeSearchView.setRoot(newRoot);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void handleContextMenuChooserRight(ActionEvent event) {

        TreeItem<CustomFile> selectedItem
                = rightTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        MenuItem selectedMenu = (MenuItem) event.getTarget();
        //TODO: Window with creation choosing a name for a directory
        switch (selectedMenu.getText()) {
            case "Create folder" -> createFolder(selectedItem);
            case "Delete" -> delete(selectedItem);
            case "Copy" -> copyFile(selectedItem);
            case "Open in editor" -> handleMenuButtonOpenFile(selectedItem);
        }
    }

    public static void handleMenuButtonOpenFile(TreeItem<CustomFile> treeItem) {
        if (treeItem == null) {
            return;
        }

        CustomFile file = treeItem.getValue();

        String[] temp = file.getName().split("\\.");
        String format = temp[temp.length - 1];

        EditorFactory editorFactory;
        if (format.equals("txt") || format.equals("py")
                || format.equals("html") || format.equals("xml")) {
            editorFactory = new TextEditorFactory();
        } else {
            editorFactory = new ImageEditorFactory();
        }

        editorFactory.instantiateEditor().launch(file);
    }

    public static void handleMenuButtonActionsClick(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getTarget();

        switch (menuItem.getText()) {
            case "Merge" -> mergeFiles();
            case "Enhanced copy" -> copyFile();
        }
    }

    private static void copyFile() {

        Boolean answer = true;

        TreeItem<CustomFile> left = leftTreeView.getSelectionModel().getSelectedItem();
        TreeItem<CustomFile> right = rightTreeView.getSelectionModel().getSelectedItem();

        if (left == null && right == null) {
            return;
        } else if (left != null && right != null) {
            answer = ChooseTreeWindow.launch();
            if (answer == null) {
                return;
            }
        } else if (right != null) {
            answer = false;
        }

        CustomFile file = answer ? left.getValue() : right.getValue();

        String[] temp = file.getName().split("\\.");
        String format = temp[temp.length - 1];

        pathGetterFactory = new CopyPathGetterFactory();
        PathGetter pathGetter = pathGetterFactory.instantiatePathGetter();
        String path = pathGetter.launch() + "." + format;

        CustomFile customFile = new CustomFile(path);
        if (customFile.exists()) {
            return;
        } else {
            try {
                customFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CopyMaker copyMaker = new CopyMaker(file, customFile);
        try {
            copyMaker.copy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        updateView();
    }

    private static void mergeFiles() {
        TreeItem<CustomFile> leftItem
                = leftTreeView.getSelectionModel().getSelectedItem();

        TreeItem<CustomFile> rightItem
                = rightTreeView.getSelectionModel().getSelectedItem();

        if (leftItem == null || rightItem == null) {
            return;
        }

        CustomFile leftFile
                = leftItem.getValue();
        CustomFile rightFile
                = rightItem.getValue();

        if (!validateMerging(leftFile, rightFile)) {
            return;
        }

        String[] temp = leftFile.getName().split("\\.");
        String format = temp[temp.length - 1];

        pathGetterFactory = new MergePathGetterFactory();
        PathGetter pathGetter
                = pathGetterFactory.instantiatePathGetter();

        String path = pathGetter.launch() + "." + format;

        CustomFile file = new CustomFile(path);
        boolean created;
        if (file.exists()) {
            return;
        } else {
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (created) {
            Merger merger = new Merger(leftFile, rightFile, file);
            merger.merge();
        }

        updateView();
    }

    private static boolean validateMerging(CustomFile leftFile,
                                           CustomFile rightFile) {

        if (leftFile == null || rightFile == null) {
            return false;
        }

        String[] t_left = leftFile.getName().split("\\.");
        String[] t_right = rightFile.getName().split("\\.");

        String format_left = t_left[t_left.length - 1];
        String format_right = t_right[t_right.length - 1];

        if(!format_left.equals(format_right)) {
            return false;
        }

        String[] allowed = {"txt", "xml", "html", "csv"};


        if(leftFile.compareTo(rightFile) == 0) {
            return false;
        }

        for (String f : allowed) {
            if (format_left.equals(f)) {
                return true;
            }
        }

        return false;
    }

    public static void handleInfoButtonClick() {
        InfoWindow.launch();
    }

    public static TreeView<CustomFile> getRightTreeView() {
        return rightTreeView;
    }

    public static void setRightTreeView(TreeView<CustomFile> rightTreeView) {
        ButtonHandler.rightTreeView = rightTreeView;
    }

    public static TreeView<CustomFile> getLeftTreeView() {
        return leftTreeView;
    }

    public static void setLeftTreeView(TreeView<CustomFile> leftTreeView) {
        ButtonHandler.leftTreeView = leftTreeView;
    }

    public static TreeView<CustomFile> getLeftTreeSearchView() {
        return leftTreeSearchView;
    }

    public static void setLeftTreeSearchView(TreeView<CustomFile> leftTreeSearchView) {
        ButtonHandler.leftTreeSearchView = leftTreeSearchView;
    }

    public static TreeView<CustomFile> getRightTreeSearchView() {
        return rightTreeSearchView;
    }

    public static void setRightTreeSearchView(TreeView<CustomFile> rightTreeSearchView) {
        ButtonHandler.rightTreeSearchView = rightTreeSearchView;
    }
}
