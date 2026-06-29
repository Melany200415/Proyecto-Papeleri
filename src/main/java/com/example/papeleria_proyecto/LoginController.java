package com.example.papeleria_proyecto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion() {

        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if(usuario.equals("admin") && password.equals("1234")){

            lblMensaje.setStyle("-fx-text-fill:green;");
            lblMensaje.setText("Bienvenido");

            //Aquí se puede abrir la ventana principal

        }else{

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Usuario o contraseña incorrectos");

        }

    }

}