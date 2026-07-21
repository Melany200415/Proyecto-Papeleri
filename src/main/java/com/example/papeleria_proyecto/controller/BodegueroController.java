package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.BodegueroDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.util.Optional;

public class BodegueroController {

    private final BodegueroDAO dao = new BodegueroDAO();

    private enum ModoTabla { PRODUCTOS, CATEGORIAS, STOCK_BAJO }
    private ModoTabla modoActual = ModoTabla.PRODUCTOS;

    private int idProductoSeleccionado = -1;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private ComboBox<String> cbEstado;

    @FXML private TableView<ObservableList<String>> tblDatos;
    @FXML private TableColumn<ObservableList<String>, String> col1, col2, col3, col4, col5, col6, col7;

    @FXML
    public void initialize() {
        cbEstado.getItems().setAll("Activo", "Inactivo");
        cargarCategorias();
        mostrarProductos();
        seleccionarFila();
    }

    private void cargarCategorias() {
        cbCategoria.getItems().clear();
        try {
            ResultSet rs = dao.listarCategorias();
            while (rs != null && rs.next()) {
                cbCategoria.getItems().add(rs.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarProductos() {
        modoActual = ModoTabla.PRODUCTOS;
        configurarColumnasProductos();
        cargarDatosProductos(dao.listarProductos());
    }

    @FXML
    private void mostrarStockBajo() {
        modoActual = ModoTabla.STOCK_BAJO;
        configurarColumnasProductos();
        cargarDatosProductos(dao.listarProductosStockBajo());
    }

    private void configurarColumnasProductos() {
        col1.setVisible(true); col2.setVisible(true); col3.setVisible(true);
        col4.setVisible(true); col5.setVisible(true); col6.setVisible(true); col7.setVisible(true);

        col1.setText("Código");
        col2.setText("Nombre");
        col3.setText("Descripción");
        col4.setText("Precio");
        col5.setText("Stock");
        col6.setText("Estado");
        col7.setText("Categoría");
    }

    private void cargarDatosProductos(ResultSet rs) {
        ObservableList<ObservableList<String>> datos = FXCollections.observableArrayList();
        try {
            while (rs != null && rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();
                fila.add(rs.getString("id_producto"));                          // 0 (Oculto en lógica)
                fila.add(rs.getString("codigo"));                               // 1
                fila.add(rs.getString("nombre"));                               // 2
                fila.add(rs.getString("descripcion"));                          // 3
                fila.add(String.format("%.2f", rs.getDouble("precio")));        // 4
                fila.add(String.valueOf(rs.getInt("stock")));                   // 5
                fila.add(rs.getInt("estado") == 1 ? "Activo" : "Inactivo");     // 6
                fila.add(rs.getString("categoria"));                            // 7

                datos.add(fila);
            }

            col1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
            col2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(2)));
            col3.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
            col4.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));
            col5.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(5)));
            col6.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(6)));
            col7.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(7)));

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
            col1.setVisible(true); col2.setVisible(true); col3.setVisible(true);
            col4.setVisible(false); col5.setVisible(false); col6.setVisible(false); col7.setVisible(false);

            col1.setText("ID"); col2.setText("Nombre"); col3.setText("Descripción");

            while (rs != null && rs.next()) {
                ObservableList<String> fila = FXCollections.observableArrayList();
                fila.add(rs.getString("id_categoria"));
                fila.add(rs.getString("nombre"));
                fila.add(rs.getString("descripcion"));
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

    private void seleccionarFila() {
        tblDatos.getSelectionModel().selectedItemProperty().addListener((obs, viejo, fila) -> {
            if (fila == null) return;

            try {
                if (modoActual == ModoTabla.PRODUCTOS || modoActual == ModoTabla.STOCK_BAJO) {
                    if (fila.size() < 8) return;

                    idProductoSeleccionado = Integer.parseInt(fila.get(0)); // ID Producto
                    txtCodigo.setText(fila.get(1));
                    txtNombre.setText(fila.get(2));
                    txtDescripcion.setText(fila.get(3));
                    txtPrecio.setText(fila.get(4));
                    txtStock.setText(fila.get(5));
                    cbEstado.setValue(fila.get(6));
                    cbCategoria.setValue(fila.get(7));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void registrarEntradaStock() {
        modificarStockDialogo(true);
    }

    @FXML
    private void registrarSalidaStock() {
        modificarStockDialogo(false);
    }

    private void modificarStockDialogo(boolean esEntrada) {
        if (idProductoSeleccionado == -1) {
            mostrarAlerta("Atención", "Seleccione un producto de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        String operacion = esEntrada ? "Ingreso de Mercadería (+)" : "Salida por Merma/Daño (-)";
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Ajuste de Stock");
        dialog.setHeaderText(operacion + " para: " + txtNombre.getText());
        dialog.setContentText("Ingrese la cantidad:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantStr -> {
            try {
                int cantidad = Integer.parseInt(cantStr);
                if (cantidad <= 0) throw new NumberFormatException();

                if (dao.actualizarStock(idProductoSeleccionado, cantidad, esEntrada)) {
                    mostrarAlerta("Éxito", "Stock actualizado correctamente.", Alert.AlertType.INFORMATION);
                    mostrarProductos();
                    limpiar();
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar el stock.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Ingrese una cantidad entera válida mayor a 0.", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void nuevo() {
        limpiar();
        tblDatos.getSelectionModel().clearSelection();
        idProductoSeleccionado = -1;
    }

    @FXML
    private void guardar() {
        if (txtCodigo.getText().isBlank() || txtNombre.getText().isBlank() ||
                txtPrecio.getText().isBlank() || txtStock.getText().isBlank() ||
                cbCategoria.getValue() == null || cbEstado.getValue() == null) {
            mostrarAlerta("Atención", "Complete todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean ok = dao.insertarProducto(
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    cbEstado.getValue().equals("Activo") ? 1 : 0,
                    dao.obtenerIdCategoria(cbCategoria.getValue())
            );

            if (ok) {
                mostrarAlerta("Éxito", "Producto registrado correctamente.", Alert.AlertType.INFORMATION);
                limpiar();
                mostrarProductos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el producto.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Revise que el precio y stock sean numéricos válidos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void editar() {
        if (idProductoSeleccionado == -1) {
            mostrarAlerta("Atención", "Seleccione un producto para editar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean ok = dao.editarProducto(
                    idProductoSeleccionado,
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    txtDescripcion.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    Integer.parseInt(txtStock.getText().trim()),
                    cbEstado.getValue().equals("Activo") ? 1 : 0,
                    dao.obtenerIdCategoria(cbCategoria.getValue())
            );

            if (ok) {
                mostrarAlerta("Éxito", "Producto actualizado correctamente.", Alert.AlertType.INFORMATION);
                mostrarProductos();
                limpiar();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Datos de entrada no válidos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminar() {
        if (idProductoSeleccionado == -1) {
            mostrarAlerta("Atención", "Seleccione un producto para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar este producto?", ButtonType.YES, ButtonType.NO);
        confirmar.setHeaderText("Eliminar producto");

        if (confirmar.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            if (dao.eliminarProducto(idProductoSeleccionado)) {
                mostrarAlerta("Éxito", "Producto eliminado.", Alert.AlertType.INFORMATION);
                mostrarProductos();
                limpiar();
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
        idProductoSeleccionado = -1;
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea cerrar la sesión?", ButtonType.OK, ButtonType.CANCEL);
        confirmacion.setTitle("Cerrar Sesión");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/papeleria_proyecto/view/login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) tblDatos.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Iniciar Sesión - Papelería");
                stage.centerOnScreen();
                stage.show();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo cargar el login: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}