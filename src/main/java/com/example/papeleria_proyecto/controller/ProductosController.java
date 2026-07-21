package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.CategoriaDAO;
import com.example.papeleria_proyecto.dao.ProductoDAO;
import com.example.papeleria_proyecto.model.Categoria;
import com.example.papeleria_proyecto.model.Producto;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.Optional;

public class ProductosController {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colId, colStock, colCategoria;
    @FXML private TableColumn<Producto, String> colCodigo, colNombre, colDescripcion, colEstado;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TextField txtBuscar, txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private VBox panelFormulario;
    @FXML private Label lblTituloFormulario;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    private Producto productoEditar;
    private boolean modoEdicion = false;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCategorias();
        cmbEstado.getItems().setAll("Activo", "Inactivo");
        cargarProductos();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdProducto()));
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrecio()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStock()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado() == 1 ? "Activo" : "Inactivo"));
        colCategoria.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdCategoria()));

        tablaProductos.setItems(listaProductos);
    }

    private void cargarCategorias() {
        cmbCategoria.setItems(categoriaDAO.listarTodos());
    }

    @FXML
    public void cargarProductos() {
        Task<ObservableList<Producto>> task = new Task<>() {
            @Override
            protected ObservableList<Producto> call() {
                return productoDAO.listarTodos();
            }
        };

        task.setOnSucceeded(e -> listaProductos.setAll(task.getValue()));
        task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los productos."));

        Thread h = new Thread(task);
        h.setDaemon(true);
        h.start();
    }

    @FXML
    private void nuevoProducto() {
        modoEdicion = false;
        productoEditar = null;
        limpiarFormulario();
        lblTituloFormulario.setText("Nuevo Producto");
        cmbEstado.getSelectionModel().select("Activo");
        mostrarFormulario(true);
    }

    @FXML
    private void editarProducto() {
        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un producto de la tabla.");
            return;
        }

        modoEdicion = true;
        productoEditar = p;
        lblTituloFormulario.setText("Editar Producto");

        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(p.getPrecio() != null ? p.getPrecio().toString() : "");
        txtStock.setText(String.valueOf(p.getStock()));
        cmbEstado.getSelectionModel().select(p.getEstado() == 1 ? "Activo" : "Inactivo");

        for (Categoria cat : cmbCategoria.getItems()) {
            if (cat.getIdCategoria() == p.getIdCategoria()) {
                cmbCategoria.getSelectionModel().select(cat);
                break;
            }
        }

        mostrarFormulario(true);
    }

    @FXML
    private void guardarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();
        String precioText = txtPrecio.getText().trim();
        String stockText = txtStock.getText().trim();
        Categoria cat = cmbCategoria.getValue();
        String est = cmbEstado.getValue();

        if (codigo.isEmpty() || nombre.isEmpty() || precioText.isEmpty() || stockText.isEmpty() || cat == null || est == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obligatorios", "Complete los campos requeridos.");
            return;
        }

        // Validación de código duplicado al crear
        if (!modoEdicion) {
            boolean existe = listaProductos.stream()
                    .anyMatch(prod -> prod.getCodigo().equalsIgnoreCase(codigo));
            if (existe) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código Duplicado", "El código ingresado ya pertenece a otro producto.");
                return;
            }
        }

        BigDecimal precio;
        int stock;
        try {
            precio = new BigDecimal(precioText.replace(",", ".")); // Soporta comas y puntos
            if (precio.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Precio Inválido", "El precio debe ser un número decimal positivo.");
            return;
        }

        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Stock Inválido", "El stock debe ser un número entero positivo.");
            return;
        }

        int estadoInt = "Activo".equals(est) ? 1 : 0;
        Producto p = new Producto(codigo, nombre, desc, precio, stock, estadoInt, cat.getIdCategoria());

        if (modoEdicion && productoEditar != null) {
            p.setIdProducto(productoEditar.getIdProducto());
            if (productoDAO.actualizar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto actualizado correctamente.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el producto en la base de datos.");
                return;
            }
        } else {
            if (productoDAO.insertar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto registrado correctamente.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar el producto en la base de datos.");
                return;
            }
        }

        Platform.runLater(this::cargarProductos);
        cancelarFormulario();
    }

    @FXML
    private void eliminarProducto() {
        Producto p = tablaProductos.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un producto para desactivar.");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea desactivar el producto " + p.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resp = conf.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            if (productoDAO.eliminar(p.getIdProducto())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto desactivado.");
                Platform.runLater(this::cargarProductos);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo desactivar el producto.");
            }
        }
    }

    @FXML
    private void buscarProducto() {
        String query = txtBuscar.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            cargarProductos();
            return;
        }

        ObservableList<Producto> filtrados = FXCollections.observableArrayList();
        for (Producto p : productoDAO.listarTodos()) {
            if ((p.getCodigo() != null && p.getCodigo().toLowerCase().contains(query)) ||
                    (p.getNombre() != null && p.getNombre().toLowerCase().contains(query))) {
                filtrados.add(p);
            }
        }
        listaProductos.setAll(filtrados);
    }

    @FXML
    private void cancelarFormulario() {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void mostrarFormulario(boolean visible) {
        panelFormulario.setVisible(visible);
        panelFormulario.setManaged(visible);
    }

    private void limpiarFormulario() {
        txtCodigo.clear(); txtNombre.clear(); txtDescripcion.clear();
        txtPrecio.clear(); txtStock.clear();
        cmbEstado.getSelectionModel().clearSelection();
        cmbCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String tit, String msg) {
        Alert a = new Alert(tipo);
        a.setTitle(tit);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}