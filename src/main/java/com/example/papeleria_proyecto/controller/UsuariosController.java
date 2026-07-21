package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.UsuarioDAO;
import com.example.papeleria_proyecto.model.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colNombreUsuario, colApellidoUsuario, colUsuario, colTelefono, colCorreo, colEstadoUsuario, colIdRol;
    @FXML private TextField txtBuscarUsuarios, txtNombre, txtApellido, txtUsuario, txtTelefono, txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cmbRol, cmbEstado;
    @FXML private VBox panelFormularioUsuario;
    @FXML private Label lblTituloFormulario;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private Usuario usuarioEditar;
    private boolean modoEdicion = false;

    @FXML
    public void initialize() {
        configurarTablaUsuarios();
        configurarFormularioUsuario();
        cargarUsuarios();
    }

    private void configurarTablaUsuarios() {
        colIdUsuario.setCellValueFactory(celda -> new SimpleObjectProperty<>(celda.getValue().getIdUsuario()));
        colNombreUsuario.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getNombre()));
        colApellidoUsuario.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getApellido()));
        colUsuario.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getUsuario()));
        colTelefono.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getTelefono()));
        colCorreo.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getCorreo()));
        colEstadoUsuario.setCellValueFactory(celda -> new SimpleStringProperty(celda.getValue().getEstadoTexto()));
        colIdRol.setCellValueFactory(celda -> new SimpleStringProperty(obtenerNombreRol(celda.getValue().getIdRol())));

        tablaUsuarios.setItems(listaUsuarios);
    }

    private void configurarFormularioUsuario() {
        cmbRol.getItems().setAll("Administrador", "Cajero", "Bodeguero");
        cmbEstado.getItems().setAll("Activo", "Inactivo");
    }

    @FXML
    private void cargarUsuarios() {
        Task<ObservableList<Usuario>> task = new Task<>() {
            @Override
            protected ObservableList<Usuario> call() {
                return FXCollections.observableArrayList(usuarioDAO.listarTodos());
            }
        };

        task.setOnSucceeded(event -> listaUsuarios.setAll(task.getValue()));
        task.setOnFailed(event -> mostrarMensaje(Alert.AlertType.ERROR, "Error", "No se cargaron los usuarios."));

        Thread hilo = new Thread(task);
        hilo.setDaemon(true);
        hilo.start();
    }

    @FXML private void mostrarTablaUsuarios() { cargarUsuarios(); }

    @FXML
    private void nuevoUsuario() {
        modoEdicion = false;
        usuarioEditar = null;
        limpiarFormularioUsuario();
        lblTituloFormulario.setText("Nuevo Usuario");
        cmbRol.getSelectionModel().selectFirst();
        cmbEstado.getSelectionModel().select("Activo");
        mostrarFormularioUsuario(true);
    }

    @FXML
    private void editarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Atención", "Seleccione un usuario para editar.");
            return;
        }

        modoEdicion = true;
        usuarioEditar = seleccionado;
        lblTituloFormulario.setText("Editar Usuario");

        txtNombre.setText(seleccionado.getNombre());
        txtApellido.setText(seleccionado.getApellido());
        txtUsuario.setText(seleccionado.getUsuario());
        txtContrasena.setText(seleccionado.getContrasena());
        txtTelefono.setText(seleccionado.getTelefono());
        txtCorreo.setText(seleccionado.getCorreo());
        cmbRol.getSelectionModel().select(obtenerNombreRol(seleccionado.getIdRol()));
        cmbEstado.getSelectionModel().select(seleccionado.getEstado() == 1 ? "Activo" : "Inactivo");

        mostrarFormularioUsuario(true);
    }

    @FXML
    private void guardarUsuario() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (campoVacio(nombre) || campoVacio(apellido) || campoVacio(usuario) ||
                campoVacio(contrasena) || campoVacio(telefono) || campoVacio(correo) ||
                cmbRol.getValue() == null || cmbEstado.getValue() == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos Incompletos", "Complete todos los campos.");
            return;
        }

        int idRol = obtenerIdRol(cmbRol.getValue());
        int estado = cmbEstado.getValue().equals("Activo") ? 1 : 0;

        Usuario u = new Usuario(nombre, apellido, usuario, contrasena, telefono, correo, estado, idRol);

        boolean exito;
        if (modoEdicion) {
            u.setIdUsuario(usuarioEditar.getIdUsuario());
            exito = usuarioDAO.actualizar(u);
        } else {
            exito = usuarioDAO.insertar(u);
        }

        if (exito) {
            mostrarMensaje(Alert.AlertType.INFORMATION, "Éxito", "Operación realizada con éxito.");
            cargarUsuarios();
            cancelarFormularioUsuario();
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "Error", "No se pudo guardar la información.");
        }
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Atención", "Seleccione un usuario.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Desactivar al usuario " + seleccionado.getUsuario() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resp = confirmacion.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.YES) {
            if (usuarioDAO.eliminar(seleccionado.getIdUsuario())) {
                cargarUsuarios();
                mostrarMensaje(Alert.AlertType.INFORMATION, "Éxito", "Usuario desactivado.");
            }
        }
    }

    @FXML
    private void buscarUsuarios() {
        String texto = txtBuscarUsuarios.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            cargarUsuarios();
            return;
        }

        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();
        for (Usuario u : usuarioDAO.listarTodos()) {
            if (u.getNombre().toLowerCase().contains(texto) ||
                    u.getApellido().toLowerCase().contains(texto) ||
                    u.getUsuario().toLowerCase().contains(texto) ||
                    u.getCorreo().toLowerCase().contains(texto)) {
                filtrados.add(u);
            }
        }
        listaUsuarios.setAll(filtrados);
    }

    @FXML
    private void cancelarFormularioUsuario() {
        limpiarFormularioUsuario();
        usuarioEditar = null;
        modoEdicion = false;
        mostrarFormularioUsuario(false);
    }

    private void mostrarFormularioUsuario(boolean visible) {
        panelFormularioUsuario.setVisible(visible);
        panelFormularioUsuario.setManaged(visible);
    }

    private void limpiarFormularioUsuario() {
        txtNombre.clear(); txtApellido.clear(); txtUsuario.clear();
        txtContrasena.clear(); txtTelefono.clear(); txtCorreo.clear();
        cmbRol.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
    }

    private int obtenerIdRol(String rol) {
        return switch (rol) {
            case "Administrador" -> 1;
            case "Cajero" -> 2;
            case "Bodeguero" -> 3;
            default -> 0;
        };
    }

    private String obtenerNombreRol(int idRol) {
        return switch (idRol) {
            case 1 -> "Administrador";
            case 2 -> "Cajero";
            case 3 -> "Bodeguero";
            default -> "Desconocido";
        };
    }

    private boolean campoVacio(String texto) { return texto == null || texto.trim().isEmpty(); }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}