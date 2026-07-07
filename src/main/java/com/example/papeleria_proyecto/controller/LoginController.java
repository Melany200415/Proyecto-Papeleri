package com.example.papeleria_proyecto.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion(ActionEvent event) {

        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        // Validar campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Complete todos los campos.");
            return;
        }

        // Usuarios de prueba (luego se reemplazan por la BD)
        if (usuario.equals("admin") && contrasena.equals("123456")) {
            mostrarMensaje("Bienvenido Administrador");
        } else if (usuario.equals("cliente") && contrasena.equals("123456")) {
            mostrarMensaje("Bienvenido Cliente");
        } else if (usuario.equals("reportes") && contrasena.equals("123456")) {
            mostrarMensaje("Bienvenido Reportes");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}