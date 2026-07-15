package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.db.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField txtNombre, txtApellido, txtUsuario, txtTelefono, txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    @FXML
    private void registrarUsuario() {
        if (txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("El usuario y contraseña son obligatorios.");
            return;
        }

        String sql = "INSERT INTO usuarios (nombre, apellido, usuario, contrasena, telefono, correo, id_rol, estado) VALUES (?, ?, ?, ?, ?, ?, 2, 1)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtNombre.getText());
            pstmt.setString(2, txtApellido.getText());
            pstmt.setString(3, txtUsuario.getText());
            pstmt.setString(4, txtPassword.getText()); // NOTA: Aquí deberías hashear la contraseña
            pstmt.setString(5, txtTelefono.getText());
            pstmt.setString(6, txtCorreo.getText());

            int filasInsertadas = pstmt.executeUpdate();

            if (filasInsertadas > 0) {
                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Registro exitoso. ¡Bienvenido!");
                limpiarCampos();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error al guardar: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtUsuario.clear();
        txtPassword.clear();
        txtTelefono.clear();
        txtCorreo.clear();
    }

    @FXML
    private void volverAlLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/papeleria_proyecto/view/login.fxml"));
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
