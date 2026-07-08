package com.example.papeleria_proyecto.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField txtNombre, txtApellido, txtUsuario, txtTelefono, txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cbRol;

    @FXML
    public void initialize() {
        cbRol.getItems().add("Cajero");
        cbRol.getSelectionModel().select(0);
    }

    @FXML
    private void handleRegistrar(ActionEvent event) {

        String nombre = txtNombre.getText();
        String usuario = txtUsuario.getText();

        System.out.println("Registrando a: " + nombre + " con rol ID 2");
    }

    @FXML
    private void handleRegresar(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/com/example/papeleria_proyecto/login.fxml"));
        Scene loginScene = new Scene(loginParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }
}
