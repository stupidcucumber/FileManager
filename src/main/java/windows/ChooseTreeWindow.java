package windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChooseTreeWindow {
    private static Boolean left = null; // By default, return left Tree; null if something went wrongly
    private static Stage stage;

    public static Boolean launch() {
        stage = new Stage();
        VBox mainView = new VBox(10);
        setMainView(mainView);

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("Choose tree to copy from");
        stage.showAndWait();

        return left;
    }

    public static void setMainView(VBox mainView) {
        mainView.setPadding(new Insets(20));
        mainView.setAlignment(Pos.CENTER);

        HBox controls = new HBox(20);
        setControls(controls);

        mainView.getChildren().addAll(new Label("We noticed that you have chosen files from both trees. \n"
                + "From which tree we need to make copy?"), controls);
    }

    public static void setControls(HBox controls) {
        controls.setAlignment(Pos.CENTER);

        Button leftTree = new Button("Left Tree");
        leftTree.setOnAction(e -> {
            left = true;
            stage.close();
        });
        Button rightTree = new Button("Right Tree");
        rightTree.setOnAction(e -> {
            left = false;
            stage.close();
        });

        controls.getChildren().addAll(leftTree, rightTree);
    }
}
