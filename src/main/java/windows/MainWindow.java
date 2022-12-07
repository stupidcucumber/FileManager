package windows;

import backend.ButtonHandler;
import backend.CustomFile;
import backend.treedata.FileTreeConverter;
import backend.treedata.FileTreeVisitor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    /*
    |- mainView
        |- toolBar
        |- dataViews
            |- leftDataTree
            |- leftDataSearcherView
            |- rightDataTree
            |- rightDataSearcher
        |- controlBar
            |- leftControls
            |- rightControls
     */
    private final static int WIDTH = 1000;
    private final static int HEIGHT = 600;
    private final static String[] formats = {"TXT", "PNG", "JPEG", "HTML", "CSS", "CPP",
                                            "JSON", "XML", "GIF", "HEIC", "PY", "PDF"};

    public static void launch(Stage stage) {
        VBox mainView = new VBox(20);
        setMainView(mainView);

        Scene mainScene = new Scene(mainView);
        stage.setScene(mainScene);
        stage.setTitle("FileManager");
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.show();
    }

    private static void setMainView(VBox mainView) {
        //mainView.setPadding(new Insets(10));

        TilePane toolBar = new TilePane();
        setToolBar(toolBar);

        HBox dataViews = new HBox(10);
        setDataViews(dataViews);

        HBox controlBar = new HBox(10);
        setControlBar(controlBar);


        mainView.getChildren().addAll(toolBar, dataViews, controlBar);
    }

    private static void setControlBar(HBox controlBar) {
        controlBar.setPadding(new Insets(10));
        controlBar.setAlignment(Pos.CENTER);

        VBox leftControls = new VBox(5);
        setLeftControls(leftControls);
        VBox rightControls = new VBox(5);
        setRightControls(rightControls);

        controlBar.getChildren().addAll(leftControls, rightControls);
    }

    private static void setLeftControls(VBox leftControls) {
        leftControls.setMinSize(500, 100);
        Button button_left_search = new Button("Search");
        setButtonSearchLeft(button_left_search);
        Button button_search_from_html = new Button("Search from HTML");
        button_search_from_html.setOnAction(e -> ButtonHandler.handleSearchFromHtmlButtonLeft());

        ComboBox<String> comboBox_formats = new ComboBox<>();
        setComboBoxFormatsLeft(comboBox_formats);

        leftControls.getChildren().addAll(comboBox_formats, button_left_search, button_search_from_html);
    }

    private static void setComboBoxFormatsLeft(ComboBox<String> comboBox) {
        for (String format : formats) {
            comboBox.getItems().add(format);
        }

        comboBox.setOnAction(e -> ButtonHandler.handleFormatChooseLeft(comboBox));
    }

    private static void setComboBoxFormatsRight(ComboBox<String> comboBox) {
        for (String format : formats) {
            comboBox.getItems().add(format);
        }

        comboBox.setOnAction(e -> ButtonHandler.handleFormatChooseRight(comboBox));
    }

    private static void setRightControls(VBox rightControls) {
        rightControls.setMinSize(500, 100);
        Button button_right_search = new Button("Search");
        setButtonSearchRight(button_right_search);
        Button button_search_from_html = new Button("Search from HTML");
        button_search_from_html.setOnAction(e -> ButtonHandler.handleSearchFromHtmlButtonRight());

        ComboBox<String> comboBox_formats = new ComboBox<>();
        setComboBoxFormatsRight(comboBox_formats);

        rightControls.getChildren().addAll(comboBox_formats, button_right_search, button_search_from_html);
    }

    private static void setButtonSearchLeft(Button button_search) {
        button_search.setOnAction(e -> ButtonHandler.handleOnSearchLeft());
    }

    private static void setButtonSearchRight(Button button_search) {
        button_search.setOnAction(e -> ButtonHandler.handleOnSearchRight());
    }

    private static void setDataViews(HBox dataViews) {
        dataViews.setPadding(new Insets(10));

        VBox leftDataTree = new VBox();
        setLeftDataTree(leftDataTree);
        VBox leftDataSearcherView = new VBox();
        setLeftDataSearcherView(leftDataSearcherView);
        VBox rightDataTree = new VBox();
        setRightDataTree(rightDataTree);
        VBox rightDataSearcherView = new VBox();
        setRightDataSearcherView(rightDataSearcherView);

        dataViews.getChildren().addAll(leftDataTree, leftDataSearcherView, rightDataTree, rightDataSearcherView);
    }

    private static void setToolBar(TilePane toolBar) {
        toolBar.setPadding(new Insets(10));
        toolBar.setStyle("-fx-background-color: #CCCCCC;");

        Button button_info = new Button("Info");
        button_info.setOnAction(e -> ButtonHandler.handleInfoButtonClick());
        Button button_preferences = new Button("Preferences");

        Button button_update_view = new Button("Update view");
        button_update_view.setOnAction(e -> ButtonHandler.handleUpdateButtonClick());

        MenuButton menuButton_actions = new MenuButton("Actions");
        setMenuButton(menuButton_actions);


        toolBar.getChildren().addAll(button_info, button_preferences, button_update_view, menuButton_actions);
    }

    private static void setMenuButton(MenuButton menuButton) {
        MenuItem menuItem_merge = new MenuItem("Merge");
        menuItem_merge.setOnAction(ButtonHandler::handleMenuButtonActionsClick);
        MenuItem menuItem_copy = new MenuItem("Enhanced copy");
        menuItem_copy.setOnAction(ButtonHandler::handleMenuButtonActionsClick);

        menuButton.getItems().addAll(menuItem_merge, menuItem_copy);
    }

    private static void setLeftDataTree(VBox leftDataTree) {
        TreeView<CustomFile> treeView = new TreeView<>();
        ButtonHandler.setLeftTreeView(treeView);
        setTreeViewLeft(treeView);

        leftDataTree.getChildren().add(treeView);
    }

    private static void setLeftDataSearcherView(VBox leftDataSearcherView) {
        TreeView<CustomFile> treeView = new TreeView<>();
        ButtonHandler.setLeftTreeSearchView(treeView);

        leftDataSearcherView.getChildren().add(treeView);
    }

    private static void setRightDataTree(VBox rightDataTree) {
        TreeView<CustomFile> treeView = new TreeView<>();
        ButtonHandler.setRightTreeView(treeView);
        setTreeViewRight(treeView);


        rightDataTree.getChildren().add(treeView);
    }

    private static void setTreeViewLeft(TreeView<CustomFile> treeView) {
        ContextMenu contextMenu = new ContextMenu();
        setContextMenuLeft(contextMenu);

        treeView.setContextMenu(contextMenu);

        FileTreeVisitor fileTreeVisitor = new FileTreeVisitor("/Users/ihorkostiuk");
        FileTreeConverter fileTreeConverter = new FileTreeConverter();
        treeView.setRoot(fileTreeConverter.convert(fileTreeVisitor.buildTree()));
    }

    private static void setContextMenuLeft(ContextMenu contextMenu) {
        MenuItem option_1 = new MenuItem("Create folder");
        MenuItem option_2 = new MenuItem("Delete");
        MenuItem option_3 = new MenuItem("Copy");
        MenuItem option_5 = new MenuItem("Rename");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem option_4 = new MenuItem("Open in editor");

        contextMenu.getItems().addAll(option_1, option_2, option_3, option_5,
                separatorMenuItem, option_4);

        contextMenu.setOnAction(ButtonHandler::handleContextMenuChooserLeft);
    }
    private static void setTreeViewRight(TreeView<CustomFile> treeView) {
        ContextMenu contextMenu = new ContextMenu();
        setContextMenuRight(contextMenu);

        treeView.setContextMenu(contextMenu);

        FileTreeVisitor fileTreeVisitor = new FileTreeVisitor("/Users/ihorkostiuk");
        FileTreeConverter fileTreeConverter = new FileTreeConverter();
        treeView.setRoot(fileTreeConverter.convert(fileTreeVisitor.buildTree()));
    }

    private static void setContextMenuRight(ContextMenu contextMenu) {
        MenuItem option_1 = new MenuItem("Create folder");
        MenuItem option_2 = new MenuItem("Delete");
        MenuItem option_3 = new MenuItem("Copy");
        MenuItem option_5 = new MenuItem("Rename");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem option_4 = new MenuItem("Open in editor");

        contextMenu.getItems().addAll(option_1, option_2, option_3, option_5
                , separatorMenuItem, option_4);

        contextMenu.setOnAction(ButtonHandler::handleContextMenuChooserRight);
    }

    private static void setRightDataSearcherView(VBox rightDataSearcherView) {
        TreeView<CustomFile> treeView = new TreeView<>();
        ButtonHandler.setRightTreeSearchView(treeView);

        rightDataSearcherView.getChildren().add(treeView);
    }

}
