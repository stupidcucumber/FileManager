package windows.editor;

import backend.CustomFile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextEditor implements Editor{
    private Stage stage;
    private CustomFile customFile;
    private TextArea textArea;
    @Override
    public void launch(CustomFile customFile) {
        this.customFile = customFile;

        stage = new Stage();
        VBox mainView = new VBox();
        setMainView(mainView);

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("Text Editor");
        stage.showAndWait();
    }

    private void setMainView(VBox mainView) {
        mainView.setAlignment(Pos.CENTER);
        mainView.setPadding(new Insets(20));

        textArea = new TextArea();
        setTextArea(textArea);

        HBox controls = new HBox(10);
        setControls(controls);

        mainView.getChildren().addAll(textArea, controls);
    }

    private void setTextArea(TextArea textArea) {
        textArea.setMinSize(1000, 600);

        Scanner scanner;
        try {
             scanner = new Scanner(customFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder stringBuilder = new StringBuilder("");
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine()).append("\n");
        }

        textArea.setText(stringBuilder.toString());
    }

    private void setControls(HBox controls) {
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10));

        Button button_save = new Button("Save");
        button_save.setOnAction(e -> handleSaveButtonClick());
        Button button_cancel = new Button("Cancel");
        button_cancel.setOnAction(e -> handleCancelButtonClick());

        controls.getChildren().addAll(button_save, button_cancel);
    }

    private void handleSaveButtonClick() {
        String text = textArea.getText();

        try {
            FileWriter fileWriter = new FileWriter(customFile);
            fileWriter.write(text);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.close();
    }

    private void handleCancelButtonClick() {
        stage.close();
    }
}
