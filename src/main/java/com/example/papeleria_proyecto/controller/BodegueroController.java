package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.BodegueroDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;

public class BodegueroController {

    private final BodegueroDAO dao = new BodegueroDAO();

    private enum ModoTabla { PRODUCTOS, CATEGORIAS, DETALLE_VENTAS }
    private ModoTabla modoActual = ModoTabla.PRODUCTOS;

    private int idProductoSeleccionado = -1;

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
    private TableView<ObservableList<String>> tblDatos;

    @FXML
    private TableColumn<ObservableList<String>, String> col8; // si no usas col8, no pasa nada
    @FXML
    private TableColumn<ObservableList<String>, String> col1;
    @FXML
    private TableColumn<ObservableList<String>, String> col2;
    @FXML
    private TableColumn<ObservableList<String>, String> col3;
    @FXML
    private TableColumn<ObservableList<String>, String> col4;
    @FXML
    private TableColumn<ObservableList<String>, String> col5;
    @FXML
    private TableColumn<ObservableList<String>, String> col6;
    @FXML
    private TableColumn<ObservableList<String>, String> col7;

    @FXML
    public void initialize() {
        cbEstado.getItems().addAll("Activo", "Inactivo");

        cargarCategorias();
        mostrarProductos();
        seleccionarFila();
    }

    private void cargarCategorias() {
        try {
            ResultSet rs = dao.listarCategorias();
            while (rs.next()) {
                cbCategoria.getItems().add(rs.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarProductos() {
        modoActual = ModoTabla.PRODUCTOS;

        col1.setVisible(true);
        col2.setVisible(true);
        col3.setVisible(true);
        col4.setVisible(true);
        col5.setVisible(true);
        col6.setVisible(true);
        col7.setVisible(true);

        col1.setText("Código");
        col2.setText("Nombre");
        col3.setText("Descripción");
        col4.setText("Precio");
        col5.setText("Stock");
        col6.setText("Estado");
        col7.setText("Categoría");

        cargarProductos();
    }

    private void cargarProductos() {
        ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();

        try {
            ResultSet rs = dao.listarProductos();

            while (rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();

                fila.add(rs.getString("codigo"));       // 0
                fila.add(rs.getString("nombre"));       // 1
                fila.add(rs.getString("descripcion"));  // 2
                fila.add(rs.getString("precio"));       // 3
                fila.add(rs.getString("stock"));        // 4
                fila.add(rs.getString("estado"));       // 5
                fila.add(rs.getString("categoria"));    // 6

                datos.add(fila);
            }

            col1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
            col2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
            col3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(2)));
            col4.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
            col5.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));
            col6.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(5)));
            col7.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(6)));

            tblDatos.setItems(datos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void mostrarCategorias() {
        modoActual = ModoTabla.CATEGORIAS;

        ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();

        try {
            ResultSet rs = dao.listarCategorias();

            col1.setVisible(true);
            col2.setVisible(true);
            col3.setVisible(true);

            col4.setVisible(false);
            col5.setVisible(false);
            col6.setVisible(false);
            col7.setVisible(false);

            col1.setText("ID");
            col2.setText("Nombre");
            col3.setText("Descripción");

            while (rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();
                fila.add(rs.getString("id_categoria")); // 0
                fila.add(rs.getString("nombre"));        // 1
                fila.add(rs.getString("descripcion"));   // 2
                datos.add(fila);
            }

            col1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
            col2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
            col3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(2)));

            tblDatos.setItems(datos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarDetalleVentas() {
        modoActual = ModoTabla.DETALLE_VENTAS;

        ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();

        try {
            ResultSet rs = dao.listarDetalleVentas();

            col1.setVisible(true);
            col2.setVisible(true);
            col3.setVisible(true);
            col4.setVisible(true);
            col5.setVisible(true);
            col6.setVisible(true);
            col7.setVisible(false);

            col1.setText("Venta");
            col2.setText("Fecha");
            col3.setText("Código");
            col4.setText("Producto");
            col5.setText("Cantidad");
            col6.setText("Subtotal");

            while (rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();

                fila.add(rs.getString("id_venta"));    // 0
                fila.add(rs.getString("fecha"));       // 1
                fila.add(rs.getString("codigo"));      // 2
                fila.add(rs.getString("nombre"));      // 3
                fila.add(rs.getString("cantidad"));    // 4
                fila.add(rs.getString("subtotal"));    // 5

                datos.add(fila);
            }

            col1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
            col2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
            col3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(2)));
            col4.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
            col5.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));
            col6.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(5)));

            tblDatos.setItems(datos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void seleccionarFila() {
        tblDatos.getSelectionModel().selectedItemProperty().addListener(
                (observable, valorAnterior, filaSeleccionada) -> {

                    if (filaSeleccionada == null) return;

                    try {
                        if (modoActual == ModoTabla.PRODUCTOS) {

                            // Productos: 7 columnas (0..6)
                            if (filaSeleccionada.size() < 7) return;

                            // ⚠️ No traes id_producto en cargarProductos() según tu DAO
                            idProductoSeleccionado = -1;

                            txtCodigo.setText(filaSeleccionada.get(0));
                            txtNombre.setText(filaSeleccionada.get(1));
                            txtDescripcion.setText(filaSeleccionada.get(2));
                            txtPrecio.setText(filaSeleccionada.get(3));
                            txtStock.setText(filaSeleccionada.get(4));

                            cbEstado.setValue(filaSeleccionada.get(5));
                            cbCategoria.setValue(filaSeleccionada.get(6));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }


    @FXML
    private void nuevo() {

        limpiar();

        tblDatos.getSelectionModel().clearSelection();

        idProductoSeleccionado = -1;

    }

    @FXML
    private void guardar() {

        if (txtCodigo.getText().isBlank() ||
                txtNombre.getText().isBlank() ||
                txtPrecio.getText().isBlank() ||
                txtStock.getText().isBlank() ||
                cbCategoria.getValue() == null ||
                cbEstado.getValue() == null) {

            new Alert(Alert.AlertType.WARNING,
                    "Complete todos los campos.").showAndWait();

            return;
        }

        try {

            String codigo = txtCodigo.getText();
            String nombre = txtNombre.getText();
            String descripcion = txtDescripcion.getText();

            double precio = Double.parseDouble(txtPrecio.getText());

            int stock = Integer.parseInt(txtStock.getText());

            int estado = cbEstado.getValue().equals("Activo") ? 1 : 0;

            // Aquí obtendremos el id_categoria
            int categoria = dao.obtenerIdCategoria(cbCategoria.getValue());

            boolean ok = dao.insertarProducto(
                    codigo,
                    nombre,
                    descripcion,
                    precio,
                    stock,
                    estado,
                    categoria
            );

            if (ok) {

                new Alert(Alert.AlertType.INFORMATION,
                        "Producto registrado correctamente.")
                        .showAndWait();

                limpiar();

                mostrarProductos();

            } else {

                new Alert(Alert.AlertType.ERROR,
                        "No se pudo registrar.")
                        .showAndWait();

            }

        } catch (Exception e) {

            new Alert(Alert.AlertType.ERROR,
                    "Datos inválidos.")
                    .showAndWait();

        }

    }

    @FXML
    private void editar() {

        if (idProductoSeleccionado == -1) {

            new Alert(Alert.AlertType.WARNING,
                    "Seleccione un producto.")
                    .showAndWait();

            return;

        }

        try {

            boolean ok = dao.editarProducto(
                    idProductoSeleccionado,
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Integer.parseInt(txtStock.getText()),
                    cbEstado.getValue().equals("Activo") ? 1 : 0,
                    dao.obtenerIdCategoria(cbCategoria.getValue())
            );

            if (ok) {

                new Alert(Alert.AlertType.INFORMATION,
                        "Producto actualizado.")
                        .showAndWait();

                mostrarProductos();

                limpiar();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    private void eliminar() {

        if (idProductoSeleccionado == -1) {

            new Alert(Alert.AlertType.WARNING,
                    "Seleccione un producto.")
                    .showAndWait();

            return;

        }

        Alert confirmar = new Alert(
                Alert.AlertType.CONFIRMATION);

        confirmar.setHeaderText("Eliminar producto");

        confirmar.setContentText(
                "¿Desea eliminar este producto?");

        if (confirmar.showAndWait().get() == ButtonType.OK) {

            if (dao.eliminarProducto(idProductoSeleccionado)) {

                new Alert(Alert.AlertType.INFORMATION,
                        "Producto eliminado.")
                        .showAndWait();

                mostrarProductos();

                limpiar();

                idProductoSeleccionado = -1;

            }

        }

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