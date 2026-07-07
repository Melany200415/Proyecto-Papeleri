package com.example.papeleria_proyecto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cbRol;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
     cbRol.getItems().addAll(
            "Administrador",
            "Cajero",
            "Bodeguero"
    );

    }

    @FXML
    private void iniciarSesion() {

        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        String rol = cbRol.getValue();

        if (rol == null) {
            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Seleccione un rol");
            return;
        }

        if (usuario.equals("admin") &&
                password.equals("1234") &&
                rol.equals("Administrador")) {

            lblMensaje.setStyle("-fx-text-fill:green;");
            lblMensaje.setText("Bienvenido Administrador");

        } else if (usuario.equals("cajero") &&
                password.equals("1234") &&
                rol.equals("Cajero")) {

            lblMensaje.setStyle("-fx-text-fill:green;");
            lblMensaje.setText("Bienvenido Cajero");

        } else if (usuario.equals("bodega") &&
                password.equals("1234") &&
                rol.equals("Bodeguero")) {

            lblMensaje.setStyle("-fx-text-fill:green;");
            lblMensaje.setText("Bienvenido Bodeguero");

        } else {

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Datos incorrectos");
        }
    }
    
}