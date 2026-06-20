module com.example.papeleria_proyecto {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.papeleria_proyecto to javafx.fxml;
    exports com.example.papeleria_proyecto;
}