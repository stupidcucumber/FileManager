package windows.pathgetter;

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

public class CopyPathGetter implements PathGetter {

    private static TextField textField;
    private static TreeView<CustomFile> customFileTreeView;
    private static Stage stage;
    private static String result;

    @Override
    public String launch() {
        stage = new Stage();
        VBox mainView = new VBox(20);
        setMainView(mainView);

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("Copying...");
        stage.showAndWait();

        return result;
    }

    private static void setMainView(VBox mainView) {
        mainView.setPadding(new Insets(20));
        mainView.setAlignment(Pos.CENTER);

        textField = new TextField();
        customFileTreeView = new TreeView<>();
        setCustomFileTreeView(customFileTreeView);

        HBox controls = new HBox(10);
        setControls(controls);

        mainView.getChildren().addAll(textField, customFileTreeView, controls);
    }

    private static void setCustomFileTreeView(TreeView<CustomFile> treeView) {
        FileTreeVisitor visitor = new FileTreeVisitor("/Users/ihorkostiuk");
        treeView.setRoot(new FileTreeConverter().convert(visitor.buildTree()));
    }

    private static void setControls(HBox controls) {
        controls.setAlignment(Pos.CENTER);

        Button button_copy_file = new Button("Copy file");
        button_copy_file.setOnAction(e -> handleCopyFileButtonClick());
        Button button_cancel = new Button("Cancel");
        button_cancel.setOnAction(e -> handleCancelButtonClick());

        controls.getChildren().addAll(button_copy_file, button_cancel);
    }

    private static void handleCopyFileButtonClick() {

        result = customFileTreeView.getSelectionModel()
                .getSelectedItem().getValue().getPath() + "/" + textField.getText();

        stage.close();
    }

    private static void handleCancelButtonClick() {
        stage.close();
    }
}
