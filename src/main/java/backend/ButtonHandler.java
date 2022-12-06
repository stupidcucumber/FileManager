package backend;

import backend.treedata.FileTreeConverter;
import backend.treedata.FileTreeVisitor;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import windows.FileMerge;
import windows.InfoWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ButtonHandler {
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
        leftSearch = comboBox.getSelectionModel().getSelectedItem().toLowerCase();
        System.out.println(leftSearch);
    }

    public static void handleFormatChooseRight(ComboBox<String> comboBox) {
        rightSearch = comboBox.getSelectionModel().getSelectedItem().toLowerCase();
        System.out.println(rightSearch);
    }

    public static void handleUpdateButtonClick() {
        updateView();
    }

    private static void updateView() {
        FileTreeConverter converter = new FileTreeConverter();

        rightTreeSearchView.setRoot(null);
        leftTreeSearchView.setRoot(null);

        FileTreeVisitor visitor_1 = new FileTreeVisitor("/Users/ihorkostiuk/");
        FileTreeVisitor visitor_2 = new FileTreeVisitor("/Users/ihorkostiuk");

        leftTreeView.setRoot(converter.convert(visitor_1.buildTree()));
        rightTreeView.setRoot(converter.convert(visitor_2.buildTree()));
    }

    public static void handleContextMenuChooserLeft(ActionEvent event) {
        TreeItem<CustomFile> selectedItem = leftTreeView.getSelectionModel().getSelectedItem();
        MenuItem selectedMenu = (MenuItem) event.getTarget();
        //TODO: Window with creation choosing a name for a directory
        switch (selectedMenu.getText()) {
            case "Create folder" -> createFolder(selectedItem);
            case "Delete" -> delete(selectedItem);
            case "Move to folder" -> moveToFolder(selectedItem);
        }
    }

    private static void createFolder(TreeItem<CustomFile> selectedFolder) {
        Random random = new Random(System.currentTimeMillis());
        CustomFile file = selectedFolder.getValue();
        File newFile = new CustomFile(file.getPath() + "/NewFolder" + random.nextInt(100000));
        if (newFile.mkdir()) {
            TreeItem<CustomFile> temp = new TreeItem<>(new CustomFile(newFile.getPath()));
            selectedFolder.getChildren().add(temp);
            temp.setGraphic(new ImageView(temp.getValue().getIcon()));
        }
    }

    private static void delete(TreeItem<CustomFile> selectedFile) {
        TreeItem<CustomFile> parent = selectedFile.getParent();
        CustomFile customFile = selectedFile.getValue();

        if (!customFile.delete()) {
            System.out.println("Wow");
        } else {
            parent.getChildren().remove(selectedFile);
        }
    }

    private static void moveToFolder(TreeItem<CustomFile> selectedFile) {

    }

    public static void handleSearchFromHtmlButtonLeft() {
        TreeItem<CustomFile> selectedItem_1
                = leftTreeView.getSelectionModel().getSelectedItem();

        String[] format_1 = selectedItem_1.getValue().getName().split("\\.");

        String f_1 = format_1[format_1.length - 1];

        TreeItem<CustomFile> newRoot = new TreeItem<>(new CustomFile("/Users/ihorkostiuk"));

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
    private static void parseHTML(CustomFile customFile, TreeItem<CustomFile> root) throws FileNotFoundException {
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

        String[] format_1 = selectedItem_1.getValue().getName().split("\\.");

        String f_1 = format_1[format_1.length - 1];

        TreeItem<CustomFile> newRoot = new TreeItem<>(new CustomFile("/Users/ihorkostiuk"));

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

        TreeItem<CustomFile> selectedItem = rightTreeView.getSelectionModel().getSelectedItem();
        MenuItem selectedMenu = (MenuItem) event.getTarget();
        //TODO: Window with creation choosing a name for a directory
        switch (selectedMenu.getText()) {
            case "Create folder" -> createFolder(selectedItem);
            case "Delete" -> delete(selectedItem);
            case "Move to folder" -> moveToFolder(selectedItem);
        }
    }

    public static void handleMenuButtonActionsClick(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getTarget();

        switch (menuItem.getText()) {
            case "Merge" -> mergeFiles();
            case "Enhanced copy" -> copyFile();
        }
    }

    private static void copyFile() {

    }

    private static void mergeFiles() {
        CustomFile leftFile
                = leftTreeView.getSelectionModel().getSelectedItem().getValue();
        CustomFile rightFile
                = rightTreeView.getSelectionModel().getSelectedItem().getValue();

        if (!validateMerging(leftFile, rightFile)) {
            return;
        }

        String[] temp = leftFile.getName().split("\\.");
        String format = temp[temp.length - 1];

        String path = FileMerge.launch() + "." + format;

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
    }

    private static boolean validateMerging(CustomFile leftFile, CustomFile rightFile) {

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

        if(leftFile.compareTo(rightFile) == 0) {
            return false;
        }

        return true;
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
