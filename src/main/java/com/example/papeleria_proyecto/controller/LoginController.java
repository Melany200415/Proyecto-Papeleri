package com.example.papeleria_proyecto.controller;
import com.example.papeleria_proyecto.conexion.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbRol;
    @FXML private Label lblMensaje;

    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

    @FXML
    public void initialize() {
        cbRol.getItems().addAll("Administrador", "Bodeguero", "Cajero");
    }

    @FXML
    private void iniciarSesion() {
        if (intentosFallidos >= MAX_INTENTOS) {
            lblMensaje.setText("Cuenta bloqueada. Intente más tarde.");
            deshabilitarCampos();
            return;
        }

        String user = txtUsuario.getText().trim();
        String pass = txtPassword.getText();
        String rolSel = cbRol.getValue();

        if (user.isEmpty() || pass.isEmpty() || rolSel == null) {
            lblMensaje.setText("⚠️ Complete todos los campos.");
            return;
        }

        if (user.length() < 3) {
            lblMensaje.setText("⚠️ Usuario inválido.");
            return;
        }

        int idRolCombo = obtenerIdRolDesdeCombo(rolSel);
        int idRolBD = getIdRolBD(user, pass);

        if (idRolBD != -1 && idRolBD == idRolCombo) {
            lblMensaje.setText("✓ Acceso concedido...");
            intentosFallidos = 0;
            redirigirSegunIdRol(idRolBD);
        } else {
            intentosFallidos++;
            int intentosRestantes = MAX_INTENTOS - intentosFallidos;
            if (intentosRestantes > 0) {
                lblMensaje.setText("Usuario, contraseña o rol incorrecto. (" + intentosRestantes + " intentos restantes)");
            } else {
                lblMensaje.setText("Demasiados intentos fallidos. Intente más tarde.");
                deshabilitarCampos();
            }
        }
    }

    private int obtenerIdRolDesdeCombo(String rol) {
        return switch (rol) {
            case "Administrador" -> 1;
            case "Cajero" -> 2;
            case "Bodeguero" -> 3;
            default -> -1;
        };
    }

    private int getIdRolBD(String username, String password) {
        String sql = "SELECT id_rol FROM usuarios WHERE usuario = ? AND contrasena = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getConexion();

            if (conn == null) {
                System.err.println("Error: No se pudo conectar a la base de datos.");
                lblMensaje.setText("Error de conexión con la base de datos.");
                return -1;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_rol");
            }

        } catch (SQLException e) {
            System.err.println("Error SQL en validación de credenciales: " + e.getMessage());
            lblMensaje.setText("Error en la validación. Intente nuevamente.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            lblMensaje.setText("Error inesperado. Contacte al administrador.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
        return -1;
    }

    private void redirigirSegunIdRol(int idRol) {
        String fxmlFile = switch (idRol) {
            case 1 -> "admin.fxml";
            case 2 -> "cajero.fxml";
            case 3 -> "bodeguero.fxml";
            default -> "";
        };

        if (fxmlFile.isEmpty()) {
            lblMensaje.setText("Rol inválido.");
            return;
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/papeleria_proyecto/" + fxmlFile));
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Error al cargar interfaz: " + e.getMessage());
            lblMensaje.setText(" Error al cargar la interfaz.");
        }
    }

    private void deshabilitarCampos() {
        txtUsuario.setDisable(true);
        txtPassword.setDisable(true);
        cbRol.setDisable(true);
    }

    @FXML
    private void irARegistro() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/papeleria_proyecto/register.fxml"));
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Error al cargar registro: " + e.getMessage());
            lblMensaje.setText("Error al cargar la ventana de registro.");
        }
    }

}
