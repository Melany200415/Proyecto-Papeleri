module com.example.papeleria_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.papeleria_proyecto to javafx.graphics;
    opens com.example.papeleria_proyecto.controller to javafx.fxml;

    exports com.example.papeleria_proyecto;
    exports com.example.papeleria_proyecto.app;
    opens com.example.papeleria_proyecto.app to javafx.graphics;
    opens com.example.papeleria_proyecto.model to javafx.base;
}