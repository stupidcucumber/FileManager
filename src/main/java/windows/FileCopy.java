package windows;

import backend.CustomFile;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileCopy {
    private static TextField textField;
    private static TreeView<CustomFile> customFileTreeView;
    private static Stage stage;
    private static String result;
    public static String launch() {
        stage = new Stage();
        VBox mainView = new VBox();

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("Copying...");
        stage.showAndWait();

        return result;
    }

    private static void launch() {

    }
}
