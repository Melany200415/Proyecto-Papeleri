package com.example.papeleria_proyecto.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {

    @FXML
    private StackPane contenedorVistas;

    @FXML
    public void initialize() {
        // Cargar vista inicial por defecto
        mostrarUsuarios();
    }

    @FXML
    private void mostrarUsuarios() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/usuarios.fxml");
    }

    @FXML
    private void mostrarProductos() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/productos.fxml");
    }

    @FXML
    private void mostrarCategorias() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/categorias.fxml");
    }

    @FXML
    private void mostrarProveedores() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/proveedores.fxml");
    }

    @FXML
    private void mostrarVentas() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/ventas.fxml");
    }

    @FXML
    private void mostrarReportes() {
        cargarVistaSecundaria("/com/example/papeleria_proyecto/view/reportes.fxml");
    }

    /**
     * Carga de vistas asíncrona para evitar que la interfaz gráfica (UI Thread) se congele.
     */
    private void cargarVistaSecundaria(String rutaFxml) {
        Task<Parent> cargaTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
                return loader.load();
            }
        };

        cargaTask.setOnSucceeded(e -> {
            contenedorVistas.getChildren().clear();
            contenedorVistas.getChildren().add(cargaTask.getValue());
        });

        cargaTask.setOnFailed(e -> {
            cargaTask.getException().printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error de Navegación",
                    "No se pudo cargar la vista seleccionada.");
        });

        Thread hiloCarga = new Thread(cargaTask);
        hiloCarga.setDaemon(true);
        hiloCarga.start();
    }

    @FXML
    private void cerrarSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/papeleria_proyecto/view/login.fxml"));
            Stage stage = (Stage) contenedorVistas.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}