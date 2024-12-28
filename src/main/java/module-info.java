module com.example.buildtrack360 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens com.example.buildtrack360 to javafx.fxml;
    exports com.example.buildtrack360;
    exports com.example.buildtrack360.Controllers;
    opens com.example.buildtrack360.Controllers to javafx.fxml;
    opens com.example.buildtrack360.Controllers.ProjectManager to javafx.fxml;

    exports com.example.buildtrack360.Backend.Project;
    opens com.example.buildtrack360.Backend.Project to javafx.fxml;
    exports com.example.buildtrack360.Backend;
    opens com.example.buildtrack360.Backend to javafx.fxml;
}
