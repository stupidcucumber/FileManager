module org.kostiuk.filemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.kostiuk.filemanager to javafx.fxml;
    exports org.kostiuk.filemanager;
}