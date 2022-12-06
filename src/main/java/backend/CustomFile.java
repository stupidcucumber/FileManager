package backend;

import javafx.scene.image.Image;

import java.io.File;

public class CustomFile extends File{

    private Image icon;

    public CustomFile(String pathname) {
        super(pathname);

        if (this.isDirectory()) {
            icon = new Image("folder.png");
        } else if (this.isFile()) {
            String[] str = this.getName().split("\\.");
            if (str.length > 1) {
                if (str[str.length - 1].equals("png") || str[str.length - 1].equals("jpg") ||
                str[str.length - 1].equals("heic") || str[1].equals("jpeg") || str[str.length - 1].equals("pdf")) {
                    icon = new Image("picture.png");
                } else if (str[str.length - 1].equals("txt")) {
                    icon = new Image("txt.png");
                } else if (str[str.length - 1].equals("css")) {
                    icon = new Image("css.png");
                } else if (str[str.length - 1].equals("html")) {
                    icon = new Image("html.png");
                } else if (str[str.length - 1].equals("cpp") || str[str.length - 1].equals("py")) {
                    icon = new Image("code_file.png");
                } else if (str[str.length - 1].equals("csv")) {
                    icon = new Image("csv.png");
                } else if (str[str.length - 1].equals("ipynb")) {
                    icon = new Image("anaconda.png");
                } else if (str[str.length - 1].equals("mp4")) {
                    icon = new Image("video.png");
                }
            } else
                icon = new Image("default_file.png");
        }

    }

    @Override
    public File[] listFiles() {
        return super.listFiles();
    }

    @Override
    public String toString() {
        return super.getName();
    }

    public Image getIcon() {
        return icon;
    }
}
