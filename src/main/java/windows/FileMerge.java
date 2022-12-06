package windows;

import backend.CustomFile;
import backend.treedata.FileTreeConverter;
import backend.treedata.FileTreeVisitor;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class FileMerge {
    private static TreeView<CustomFile> treeView;
    private final static Stage stage = new Stage();
    private static TextField textField;
    private static String result = "";

    public static String launch() {
        VBox mainView = new VBox(10);
        mainView.setPadding(new Insets(20));

        setMainView(mainView);

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("Merging...");
        stage.showAndWait();

        return result;
    }
    public static void setMainView(VBox view) {
        view.setAlignment(Pos.CENTER);

        textField = new TextField();
        treeView = new TreeView<>();
        setTreeView(treeView);

        HBox controls = new HBox(20);
        setControlsLayout(controls);

        view.getChildren().addAll(textField, treeView, controls);
    }

    private static void setTreeView(TreeView<CustomFile> treeView) {
        FileTreeVisitor visitor = new FileTreeVisitor("/Users/ihorkostiuk");
        treeView.setRoot(new FileTreeConverter().convert(visitor.buildTree()));
    }

    private static void setControlsLayout(HBox controlsLayout) {
        controlsLayout.setAlignment(Pos.CENTER);

        Button button_merge = new Button("Merge files");
        button_merge.setOnAction(e -> handleMerging());
        Button button_cancel = new Button("Cancel");
        button_cancel.setOnAction(e -> handleCancelling());

        controlsLayout.getChildren().addAll(button_merge, button_cancel);
    }

    private static void handleMerging() {
        CustomFile file = treeView.getSelectionModel().getSelectedItem().getValue();
        if (!file.isDirectory() || textField.getText().equals("")) {
            return;
        }

        result = file.getPath() + "/" + textField.getText();

        stage.close();
    }

    private static void handleCancelling() {
        stage.close();
    }
}
