package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.db.Conexion;
import com.example.papeleria_proyecto.model.Usuario;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cbRol;

    @FXML
    private Label lblMensaje;

    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

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

        if (intentosFallidos >= MAX_INTENTOS) {
            lblMensaje.setText("Cuenta bloqueada.");
            deshabilitarCampos();
            return;
        }

        String nombreUsuario =
                txtUsuario.getText().trim();

        String password =
                txtPassword.getText();

        String rol =
                cbRol.getValue();

        if (nombreUsuario.isEmpty()
                || password.isEmpty()
                || rol == null) {

            lblMensaje.setText(
                    "Complete todos los campos."
            );

            return;
        }

        int idRol = obtenerIdRol(rol);

        String sql = """
                SELECT *
                FROM usuarios
                WHERE usuario = ?
                AND contrasena = ?
                AND id_rol = ?
                AND estado = 1
                """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, password);
            ps.setInt(3, idRol);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    intentosFallidos = 0;

                    Usuario usuarioActual =
                            new Usuario();

                    usuarioActual.setIdUsuario(
                            rs.getInt("id_usuario")
                    );

                    usuarioActual.setNombre(
                            rs.getString("nombre")
                    );

                    usuarioActual.setApellido(
                            rs.getString("apellido")
                    );

                    usuarioActual.setUsuario(
                            rs.getString("usuario")
                    );

                    usuarioActual.setContrasena(
                            rs.getString("contrasena")
                    );

                    usuarioActual.setTelefono(
                            rs.getString("telefono")
                    );

                    usuarioActual.setCorreo(
                            rs.getString("correo")
                    );

                    usuarioActual.setEstado(
                            rs.getInt("estado")
                    );

                    usuarioActual.setIdRol(idRol);

                    lblMensaje.setText(
                            "Bienvenido "
                                    + usuarioActual.getNombre()
                    );

                    System.out.println(
                            "Usuario autenticado: "
                                    + usuarioActual.getIdUsuario()
                                    + " - "
                                    + usuarioActual.getNombre()
                    );

                    abrirModulo(
                            idRol,
                            usuarioActual
                    );

                } else {

                    intentosFallidos++;

                    int restantes =
                            MAX_INTENTOS
                                    - intentosFallidos;

                    if (restantes > 0) {

                        lblMensaje.setText(
                                "Credenciales incorrectas. "
                                        + "Intentos restantes: "
                                        + restantes
                        );

                    } else {

                        lblMensaje.setText(
                                "Cuenta bloqueada."
                        );

                        deshabilitarCampos();
                    }
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            lblMensaje.setText(
                    "Error de conexión."
            );
        }
    }

    private int obtenerIdRol(String rol) {

        return switch (rol) {
            case "Administrador" -> 1;
            case "Cajero" -> 2;
            case "Bodeguero" -> 3;
            default -> 0;
        };
    }

    private void abrirModulo(
            int idRol,
            Usuario usuarioActual
    ) {

        String vista = switch (idRol) {
            case 1 -> "admin.fxml";
            case 2 -> "cajero.fxml";
            case 3 -> "bodeguero.fxml";
            default -> "";
        };

        if (vista.isEmpty()) {

            lblMensaje.setText(
                    "Rol no válido."
            );

            return;
        }

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/example/papeleria_proyecto/view/"
                                            + vista
                            )
                    );

            Parent root =
                    loader.load();

            if (idRol == 2) {

                CajeroController cajeroController =
                        loader.getController();

                cajeroController.setUsuarioActual(
                        usuarioActual
                );
            }

            Stage stage =
                    (Stage) txtUsuario
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            lblMensaje.setText(
                    "No se pudo abrir el módulo."
            );
        }
    }

    private void deshabilitarCampos() {

        txtUsuario.setDisable(true);
        txtPassword.setDisable(true);
        cbRol.setDisable(true);
    }

    @FXML
    private void salir() {

        Stage stage =
                (Stage) txtUsuario
                        .getScene()
                        .getWindow();

        stage.close();
    }
}