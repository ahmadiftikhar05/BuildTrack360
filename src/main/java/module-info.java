module com.example.buildtrack360 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;



    opens com.example.buildtrack360 to javafx.fxml;
    exports com.example.buildtrack360;
}