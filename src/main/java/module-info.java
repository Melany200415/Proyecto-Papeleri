module com.example.papeleria_proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.papeleria_proyecto;

    opens com.example.papeleria_proyecto to javafx.fxml;
    opens com.example.papeleria_proyecto.controller to javafx.fxml;
}