package windows.editor;

import backend.CustomFile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class ImageEditor implements Editor{

    private CustomFile customFile;
    private Stage stage;
    @Override
    public void launch(CustomFile customFile) {
        this.customFile = customFile;
        stage = new Stage();
        VBox mainView = new VBox();
        try {
            setMainView(mainView);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(mainView);
        stage.setScene(scene);
        stage.setTitle("GraphicEditor");
        stage.showAndWait();
    }

    private void setMainView(VBox mainView) throws URISyntaxException, IOException {
        mainView.setAlignment(Pos.CENTER);
        mainView.setPadding(new Insets(20));

        System.out.println(customFile.getAbsolutePath());

        ImageView imageView
                = new ImageView(new Image(customFile.toURI().toURL().toString(), false));

        imageView.setFitHeight(600);
        imageView.setFitWidth(1000);

        mainView.getChildren().addAll(imageView);
    }
}
