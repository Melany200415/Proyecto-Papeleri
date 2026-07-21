package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.ProveedorDAO;
import com.example.papeleria_proyecto.model.Proveedor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.regex.Pattern;

public class ProveedoresController {

    @FXML private TableView<Proveedor> tablaProveedores;
    @FXML private TableColumn<Proveedor, Integer> colId;
    @FXML private TableColumn<Proveedor, String> colNombre, colTelefono, colCorreo, colDireccion;
    @FXML private TextField txtBuscar, txtNombre, txtTelefono, txtCorreo, txtDireccion;
    @FXML private VBox panelFormulario;
    @FXML private Label lblTituloFormulario;

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();
    private Proveedor proveedorEditar;
    private boolean modoEdicion = false;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdProveedor()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colDireccion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDireccion()));

        tablaProveedores.setItems(listaProveedores);
        cargarProveedores();
    }

    @FXML
    public void cargarProveedores() {
        listaProveedores.setAll(proveedorDAO.listarTodos());
    }

    @FXML
    private void nuevoProveedor() {
        modoEdicion = false;
        proveedorEditar = null;
        limpiarFormulario();
        lblTituloFormulario.setText("Nuevo Proveedor");
        mostrarFormulario(true);
    }

    @FXML
    private void editarProveedor() {
        Proveedor p = tablaProveedores.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un proveedor.");
            return;
        }

        modoEdicion = true;
        proveedorEditar = p;
        lblTituloFormulario.setText("Editar Proveedor");

        txtNombre.setText(p.getNombre());
        txtTelefono.setText(p.getTelefono());
        txtCorreo.setText(p.getCorreo());
        txtDireccion.setText(p.getDireccion());
        mostrarFormulario(true);
    }

    @FXML
    private void guardarProveedor() {
        String nombre = txtNombre.getText().trim();
        String telf = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String dir = txtDireccion.getText().trim();

        if (nombre.isEmpty() || telf.isEmpty() || correo.isEmpty() || dir.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Complete todos los campos del formulario.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(correo).matches()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Correo Inválido", "Ingrese una dirección de correo válida.");
            return;
        }

        Proveedor p = new Proveedor(nombre, telf, correo, dir);

        if (modoEdicion && proveedorEditar != null) {
            p.setIdProveedor(proveedorEditar.getIdProveedor());
            if (proveedorDAO.actualizar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Proveedor actualizado.");
            }
        } else {
            if (proveedorDAO.insertar(p)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Proveedor registrado.");
            }
        }

        cargarProveedores();
        cancelarFormulario();
    }

    @FXML
    private void eliminarProveedor() {
        Proveedor p = tablaProveedores.getSelectionModel().getSelectedItem();
        if (p == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione un proveedor.");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar al proveedor " + p.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resp = conf.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            if (proveedorDAO.eliminar(p.getIdProveedor())) {
                cargarProveedores();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Proveedor eliminado.");
            }
        }
    }

    @FXML
    private void buscarProveedor() {
        String text = txtBuscar.getText().trim().toLowerCase();
        if (text.isEmpty()) {
            cargarProveedores();
            return;
        }

        ObservableList<Proveedor> filtrados = FXCollections.observableArrayList();
        for (Proveedor p : proveedorDAO.listarTodos()) {
            if (p.getNombre().toLowerCase().contains(text) || p.getCorreo().toLowerCase().contains(text)) {
                filtrados.add(p);
            }
        }
        listaProveedores.setAll(filtrados);
    }

    @FXML
    private void cancelarFormulario() {
        limpiarFormulario();
        mostrarFormulario(false);
    }

    private void mostrarFormulario(boolean vis) {
        panelFormulario.setVisible(vis);
        panelFormulario.setManaged(vis);
    }

    private void limpiarFormulario() {
        txtNombre.clear(); txtTelefono.clear();
        txtCorreo.clear(); txtDireccion.clear();
    }

    private void mostrarAlerta(Alert.AlertType t, String tit, String msg) {
        Alert a = new Alert(t);
        a.setTitle(tit);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}