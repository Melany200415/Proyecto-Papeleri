package com.example.papeleria_proyecto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class BodegueroController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private ComboBox<String> cbCategoria;

    @FXML
    private ComboBox<String> cbEstado;

    @FXML
    private TableView<?> tblDatos;

    @FXML
    private TableColumn<?, ?> col1;

    @FXML
    private TableColumn<?, ?> col2;

    @FXML
    private TableColumn<?, ?> col3;

    @FXML
    private TableColumn<?, ?> col4;

    @FXML
    private TableColumn<?, ?> col5;

    @FXML
    private TableColumn<?, ?> col6;

    @FXML
    private TableColumn<?, ?> col7;

    @FXML
    public void initialize() {

        cbEstado.getItems().addAll(
                "Activo",
                "Inactivo"
        );

        cbCategoria.getItems().addAll(
                "Útiles",
                "Cuadernos",
                "Oficina",
                "Arte"
        );

        mostrarProductos();
    }



    @FXML
    private void mostrarProductos() {

        col1.setText("Código");
        col2.setText("Nombre");
        col3.setText("Descripción");
        col4.setText("Precio");
        col5.setText("Stock");
        col6.setText("Estado");
        col7.setText("Categoría");

        System.out.println("Tabla Productos");

   
    }

    @FXML
    private void mostrarCategorias() {

        col1.setText("ID");
        col2.setText("Nombre");
        col3.setText("Descripción");

        col4.setVisible(false);
        col5.setVisible(false);
        col6.setVisible(false);
        col7.setVisible(false);

        System.out.println("Tabla Categorías");


    }

    @FXML
    private void mostrarDetalleVentas() {

        col1.setVisible(true);
        col2.setVisible(true);
        col3.setVisible(true);
        col4.setVisible(true);
        col5.setVisible(true);

        col1.setText("ID");
        col2.setText("Venta");
        col3.setText("Producto");
        col4.setText("Cantidad");
        col5.setText("Subtotal");

        col6.setVisible(false);
        col7.setVisible(false);

        System.out.println("Tabla Detalle Venta");
    }

    @FXML
    private void mostrarMovimientos() {

        col1.setVisible(true);
        col2.setVisible(true);
        col3.setVisible(true);
        col4.setVisible(true);
        col5.setVisible(true);
        col6.setVisible(true);
        col7.setVisible(true);

        col1.setText("ID");
        col2.setText("Producto");
        col3.setText("Usuario");
        col4.setText("Tipo");
        col5.setText("Cantidad");
        col6.setText("Motivo");
        col7.setText("Fecha");

        System.out.println("Tabla Movimientos");

    }


    @FXML
    private void nuevo() {

        limpiar();

    }

    @FXML
    private void guardar() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guardar");
        alert.setHeaderText(null);
        alert.setContentText("Producto guardado correctamente.");
        alert.showAndWait();

        // bodegueroDAO.insertarProducto(...);

    }

    @FXML
    private void editar() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Editar");
        alert.setHeaderText(null);
        alert.setContentText("Producto actualizado correctamente.");
        alert.showAndWait();

        // bodegueroDAO.actualizarProducto(...);

    }

    @FXML
    private void eliminar() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eliminar");
        alert.setHeaderText(null);
        alert.setContentText("Producto eliminado correctamente.");
        alert.showAndWait();

    }

    @FXML
    private void limpiar() {

        txtCodigo.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtStock.clear();

        cbCategoria.getSelectionModel().clearSelection();
        cbEstado.getSelectionModel().clearSelection();

    }

}