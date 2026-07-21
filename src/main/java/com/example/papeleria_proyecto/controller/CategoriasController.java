package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.CategoriaDAO;
import com.example.papeleria_proyecto.model.Categoria;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class CategoriasController {

    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNombre, colDescripcion;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private VBox panelFormulario;
    @FXML private Label lblTituloFormulario;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    private Categoria categoriaEditar;
    private boolean modoEdicion = false;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdCategoria()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));

        tablaCategorias.setItems(listaCategorias);
        cargarCategorias();
    }

    @FXML
    public void cargarCategorias() {
        listaCategorias.setAll(categoriaDAO.listarTodos());
    }

    @FXML
    private void nuevaCategoria() {
        modoEdicion = false;
        categoriaEditar = null;
        limpiarFormulario();
        lblTituloFormulario.setText("Nueva Categoría");
        mostrarFormulario(true);
    }

    @FXML
    private void editarCategoria() {
        Categoria c = tablaCategorias.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione una categoría.");
            return;
        }

        modoEdicion = true;
        categoriaEditar = c;
        lblTituloFormulario.setText("Editar Categoría");

        txtNombre.setText(c.getNombre());
        txtDescripcion.setText(c.getDescripcion());
        mostrarFormulario(true);
    }

    @FXML
    private void guardarCategoria() {
        String nombre = txtNombre.getText().trim();
        String desc = txtDescripcion.getText().trim();

        if (nombre.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "El nombre de la categoría es obligatorio.");
            return;
        }

        Categoria cat = new Categoria(nombre, desc);

        if (modoEdicion && categoriaEditar != null) {
            cat.setIdCategoria(categoriaEditar.getIdCategoria());
            if (categoriaDAO.actualizar(cat)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Categoría actualizada.");
            }
        } else {
            if (categoriaDAO.insertar(cat)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Categoría guardada.");
            }
        }

        cargarCategorias();
        cancelarFormulario();
    }

    @FXML
    private void eliminarCategoria() {
        Categoria c = tablaCategorias.getSelectionModel().getSelectedItem();
        if (c == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atención", "Seleccione una categoría.");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar la categoría " + c.getNombre() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resp = conf.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            if (categoriaDAO.eliminar(c.getIdCategoria())) {
                cargarCategorias();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Categoría eliminada.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar la categoría si está vinculada a productos.");
            }
        }
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
        txtNombre.clear();
        txtDescripcion.clear();
    }

    private void mostrarAlerta(Alert.AlertType t, String tit, String msg) {
        Alert a = new Alert(t);
        a.setTitle(tit);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}