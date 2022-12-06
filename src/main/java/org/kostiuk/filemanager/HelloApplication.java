package org.kostiuk.filemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import windows.MainWindow;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainWindow.launch(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}