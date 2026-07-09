package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.LoginDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class LoginController {

    private LoginDAO dao = new LoginDAO();

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

        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();
        String rolSeleccionado = cbRol.getValue();

        if (usuario.isEmpty() || password.isEmpty() || rolSeleccionado == null) {

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Complete todos los campos.");
            return;

        }

        try {

            ResultSet rs = dao.iniciarSesion(usuario, password);

            if (rs != null && rs.next()) {

                String rolBD = rs.getString("rol");

                if (!rolBD.equalsIgnoreCase(rolSeleccionado)) {

                    lblMensaje.setStyle("-fx-text-fill:red;");
                    lblMensaje.setText("El rol seleccionado no corresponde al usuario.");
                    return;

                }

                lblMensaje.setStyle("-fx-text-fill:green;");
                lblMensaje.setText("Bienvenido " + usuario);

                abrirModulo(rolBD);

            } else {

                lblMensaje.setStyle("-fx-text-fill:red;");
                lblMensaje.setText("Usuario o contraseña incorrectos.");

            }

        } catch (Exception e) {

            e.printStackTrace();

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Error al conectar con la base de datos.");

        }

    }

    private void abrirModulo(String rol) {

        try {

            String vista = "";

            switch (rol.toLowerCase()) {

                case "administrador":
                    vista = "admin.fxml";
                    break;

                case "cajero":
                    vista = "cajero.fxml";
                    break;

                case "bodeguero":
                    vista = "bodeguero.fxml";
                    break;

                default:
                    lblMensaje.setText("Rol no válido.");
                    return;

            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/papeleria_proyecto/" + vista)
            );

            Parent root = loader.load();

            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Papelería");
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("No se pudo abrir el módulo.");

        }

    }

    @FXML
    private void irARegistro() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/papeleria_proyecto/register.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) txtUsuario.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Registro de Usuario");
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            lblMensaje.setStyle("-fx-text-fill:red;");
            lblMensaje.setText("Error al abrir la ventana de registro.");

        }

    }

}