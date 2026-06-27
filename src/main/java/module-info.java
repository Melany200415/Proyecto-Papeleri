module com.example.papeleria_proyecto {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.papeleria_proyecto to javafx.fxml;
    opens com.example.papeleria_proyecto.controller to javafx.fxml;

    exports com.example.papeleria_proyecto;
    exports com.example.papeleria_proyecto.controller;
}