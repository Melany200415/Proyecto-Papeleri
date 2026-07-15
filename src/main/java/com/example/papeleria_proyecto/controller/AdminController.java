package com.example.papeleria_proyecto.controller;



import com.example.papeleria_proyecto.dao.CategoriaDAO;
import com.example.papeleria_proyecto.dao.ProductoDAO;
import com.example.papeleria_proyecto.dao.UsuarioDAO;
import com.example.papeleria_proyecto.dao.VentaDAO;
import com.example.papeleria_proyecto.model.Categoria;
import com.example.papeleria_proyecto.model.Producto;
import com.example.papeleria_proyecto.model.Usuario;
import com.example.papeleria_proyecto.model.Venta;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    // ---------- Paneles ----------
    @FXML private VBox panelUsuarios;
    @FXML private VBox panelProductos;
    @FXML private VBox panelCategorias;
    @FXML private VBox panelVentas;

    @FXML private Label lblEstadoUsuarios;
    @FXML private Label lblEstadoProductos;
    @FXML private Label lblEstadoCategorias;
    @FXML private Label lblEstadoVentas;

    @FXML private TextField txtBuscarUsuarios;
    @FXML private TextField txtBuscarProductos;
    @FXML private TextField txtBuscarCategorias;
    @FXML private TextField txtBuscarVentas;

    // ---------- Tabla Usuarios ----------
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colIdUsuario;
    @FXML private TableColumn<Usuario, String> colNombreUsuario;
    @FXML private TableColumn<Usuario, String> colApellidoUsuario;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colTelefono;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, Integer> colEstadoUsuario;
    @FXML private TableColumn<Usuario, Integer> colIdRol;

    // ---------- Tabla Productos ----------
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colIdProducto;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombreProducto;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, BigDecimal> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Integer> colEstadoProducto;
    @FXML private TableColumn<Producto, Integer> colIdCategoria;

    // ---------- Tabla Categorías ----------
    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, Integer> colIdCategoriaTab;
    @FXML private TableColumn<Categoria, String> colNombreCategoria;
    @FXML private TableColumn<Categoria, String> colDescripcionCategoria;

    // ---------- Tabla Ventas ----------
    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, Integer> colIdVenta;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, BigDecimal> colSubtotal;
    @FXML private TableColumn<Venta, BigDecimal> colIva;
    @FXML private TableColumn<Venta, BigDecimal> colTotal;
    @FXML private TableColumn<Venta, Integer> colIdUsuarioVenta;

    // ---------- DAOs ----------
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    private FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(FXCollections.observableArrayList(), u -> true);
    private FilteredList<Producto> productosFiltrados = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
    private FilteredList<Categoria> categoriasFiltradas = new FilteredList<>(FXCollections.observableArrayList(), c -> true);
    private FilteredList<Venta> ventasFiltradas = new FilteredList<>(FXCollections.observableArrayList(), v -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        configurarFiltros();
        inicializarTablas();
        mostrarUsuarios(); // panel por defecto al abrir
    }

    private void inicializarTablas() {
        mostrarTablaUsuarios();
        mostrarTablaProductos();
        mostrarTablaCategorias();
        mostrarTablaVentas();
    }

    private void configurarFiltros() {
        txtBuscarUsuarios.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroUsuarios(newValue));
        txtBuscarProductos.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroProductos(newValue));
        txtBuscarCategorias.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroCategorias(newValue));
        txtBuscarVentas.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltroVentas(newValue));
    }

    private void aplicarFiltroUsuarios(String texto) {
        String valor = texto == null ? "" : texto.toLowerCase().trim();
        usuariosFiltrados.setPredicate(usuario -> {
            if (valor.isEmpty()) {
                return true;
            }
            return contiene(usuario.getNombre(), valor)
                    || contiene(usuario.getApellido(), valor)
                    || contiene(usuario.getUsuario(), valor)
                    || contiene(usuario.getCorreo(), valor);
        });
    }

    private void aplicarFiltroProductos(String texto) {
        String valor = texto == null ? "" : texto.toLowerCase().trim();
        productosFiltrados.setPredicate(producto -> {
            if (valor.isEmpty()) {
                return true;
            }
            return contiene(producto.getCodigo(), valor)
                    || contiene(producto.getNombre(), valor)
                    || contiene(producto.getDescripcion(), valor);
        });
    }

    private void aplicarFiltroCategorias(String texto) {
        String valor = texto == null ? "" : texto.toLowerCase().trim();
        categoriasFiltradas.setPredicate(categoria -> {
            if (valor.isEmpty()) {
                return true;
            }
            return contiene(categoria.getNombre(), valor)
                    || contiene(categoria.getDescripcion(), valor);
        });
    }

    private void aplicarFiltroVentas(String texto) {
        String valor = texto == null ? "" : texto.toLowerCase().trim();
        ventasFiltradas.setPredicate(venta -> {
            if (valor.isEmpty()) {
                return true;
            }
            return String.valueOf(venta.getIdVenta()).contains(valor)
                    || String.valueOf(venta.getIdUsuario()).contains(valor)
                    || (venta.getTotal() != null && venta.getTotal().toString().contains(valor));
        });
    }

    private boolean contiene(String valor, String texto) {
        return valor != null && valor.toLowerCase().contains(texto);
    }

    private void configurarColumnas() {
        // Usuarios
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoUsuario.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEstadoUsuario.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colIdRol.setCellValueFactory(new PropertyValueFactory<>("idRol"));

        // Productos
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colEstadoProducto.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));

        // Categorías
        colIdCategoriaTab.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcionCategoria.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Ventas
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colFecha.setCellValueFactory(cellData -> {
            Venta venta = cellData.getValue();
            String texto = venta.getFecha() != null
                    ? venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "";
            return new ReadOnlyStringWrapper(texto);
        });
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colIva.setCellValueFactory(new PropertyValueFactory<>("iva"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colIdUsuarioVenta.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
    }

    // ===================== CAMBIO DE PANELES =====================

    private void ocultarTodos() {

        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);

        panelProductos.setVisible(false);
        panelProductos.setManaged(false);

        panelCategorias.setVisible(false);
        panelCategorias.setManaged(false);

        panelVentas.setVisible(false);
        panelVentas.setManaged(false);

    }

    private void actualizarEstado(Label etiqueta, String mensaje, boolean hayDatos) {
        etiqueta.setText(mensaje);
        etiqueta.setStyle(hayDatos
                ? "-fx-text-fill:#2e7d32; -fx-font-weight:bold;"
                : "-fx-text-fill:#c0392b; -fx-font-weight:bold;");
    }

    @FXML
    private void mostrarUsuarios() {
        ocultarTodos();

        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);

        mostrarTablaUsuarios();
    }

    @FXML
    private void mostrarProductos() {

        ocultarTodos();

        panelProductos.setVisible(true);
        panelProductos.setManaged(true);

        mostrarTablaProductos();

    }

    @FXML
    private void mostrarCategorias() {

        ocultarTodos();

        panelCategorias.setVisible(true);
        panelCategorias.setManaged(true);

        mostrarTablaCategorias();

    }

    @FXML
    private void mostrarVentas() {
        ocultarTodos();
        panelVentas.setVisible(true);
        mostrarTablaVentas();
    }

    // ===================== BOTONES "MOSTRAR" (ejecutan el SELECT * y llenan la tabla) =====================

    @FXML
    private void mostrarTablaUsuarios() {

        ObservableList<Usuario> listaUsuarios =
                usuarioDAO.listarTodos();

        tablaUsuarios.setItems(listaUsuarios);

        System.out.println("Usuarios encontrados: "
                + listaUsuarios.size());

    }

    @FXML
    private void buscarUsuarios() {
        aplicarFiltroUsuarios(txtBuscarUsuarios.getText());
    }

    @FXML
    private void mostrarTablaProductos() {
        ObservableList<Producto> datos = productoDAO.listarTodos();
        productosFiltrados = new FilteredList<>(datos, p -> true);
        tablaProductos.setItems(productosFiltrados);
        tablaProductos.setPlaceholder(new Label("No hay productos registrados."));
        aplicarFiltroProductos(txtBuscarProductos.getText());
        actualizarEstado(lblEstadoProductos,
                datos.isEmpty() ? "Sin registros en productos" : "Se cargaron " + datos.size() + " productos",
                !datos.isEmpty());
    }

    @FXML
    private void buscarProductos() {
        aplicarFiltroProductos(txtBuscarProductos.getText());
    }

    @FXML
    private void mostrarTablaCategorias() {
        ObservableList<Categoria> datos = categoriaDAO.listarTodos();
        categoriasFiltradas = new FilteredList<>(datos, c -> true);
        tablaCategorias.setItems(categoriasFiltradas);
        tablaCategorias.setPlaceholder(new Label("No hay categorías registradas."));
        aplicarFiltroCategorias(txtBuscarCategorias.getText());
        actualizarEstado(lblEstadoCategorias,
                datos.isEmpty() ? "Sin registros en categorías" : "Se cargaron " + datos.size() + " categorías",
                !datos.isEmpty());
    }

    @FXML
    private void buscarCategorias() {
        aplicarFiltroCategorias(txtBuscarCategorias.getText());
    }

    @FXML
    private void mostrarTablaVentas() {
        ObservableList<Venta> datos = ventaDAO.listarTodas();
        ventasFiltradas = new FilteredList<>(datos, v -> true);
        tablaVentas.setItems(ventasFiltradas);
        tablaVentas.setPlaceholder(new Label("No hay ventas registradas."));
        aplicarFiltroVentas(txtBuscarVentas.getText());
        actualizarEstado(lblEstadoVentas,
                datos.isEmpty() ? "Sin registros en ventas" : "Se cargaron " + datos.size() + " ventas",
                !datos.isEmpty());
    }

    @FXML
    private void buscarVentas() {
        aplicarFiltroVentas(txtBuscarVentas.getText());
    }

    // ===================== CRUD USUARIOS =====================

    @FXML
    private void nuevoUsuario() {
        Dialog<Usuario> dialog = crearDialogoUsuario(null);
        Optional<Usuario> resultado = dialog.showAndWait();
        resultado.ifPresent(u -> {
            if (usuarioDAO.insertar(u)) {
                mostrarUsuarios();
            } else {
                mostrarError("No se pudo crear el usuario.");
            }
        });
    }

    @FXML
    private void editarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Selecciona un usuario de la tabla.");
            return;
        }
        Dialog<Usuario> dialog = crearDialogoUsuario(seleccionado);
        Optional<Usuario> resultado = dialog.showAndWait();
        resultado.ifPresent(u -> {
            if (usuarioDAO.actualizar(u)) {
                mostrarUsuarios();
            } else {
                mostrarError("No se pudo actualizar el usuario.");
            }
        });
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Selecciona un usuario de la tabla.");
            return;
        }
        if (confirmar("¿Deseas desactivar al usuario \"" + seleccionado.getUsuario() + "\"?")) {
            if (usuarioDAO.eliminar(seleccionado.getIdUsuario())) {
                mostrarUsuarios();
            } else {
                mostrarError("No se pudo eliminar el usuario.");
            }
        }
    }

    private Dialog<Usuario> crearDialogoUsuario(Usuario existente) {
        Dialog<Usuario> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Nuevo usuario" : "Editar usuario");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        TextField txtUsuario = new TextField();
        PasswordField txtContrasena = new PasswordField();
        TextField txtTelefono = new TextField();
        TextField txtCorreo = new TextField();
        CheckBox chkEstado = new CheckBox("Activo");
        TextField txtIdRol = new TextField();

        if (existente != null) {
            txtNombre.setText(existente.getNombre());
            txtApellido.setText(existente.getApellido());
            txtUsuario.setText(existente.getUsuario());
            txtContrasena.setText(existente.getContrasena());
            txtTelefono.setText(existente.getTelefono());
            txtCorreo.setText(existente.getCorreo());
            chkEstado.setSelected(existente.getEstado() == 1);
            txtIdRol.setText(String.valueOf(existente.getIdRol()));
        } else {
            chkEstado.setSelected(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Apellido:"), txtApellido);
        grid.addRow(2, new Label("Usuario:"), txtUsuario);
        grid.addRow(3, new Label("Contraseña:"), txtContrasena);
        grid.addRow(4, new Label("Teléfono:"), txtTelefono);
        grid.addRow(5, new Label("Correo:"), txtCorreo);
        grid.addRow(6, new Label("Id Rol:"), txtIdRol);
        grid.addRow(7, chkEstado);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                try {
                    int idRol = txtIdRol.getText().isBlank() ? 0 : Integer.parseInt(txtIdRol.getText().trim());
                    Usuario u = existente == null ? new Usuario() : existente;
                    u.setNombre(txtNombre.getText());
                    u.setApellido(txtApellido.getText());
                    u.setUsuario(txtUsuario.getText());
                    u.setContrasena(txtContrasena.getText());
                    u.setTelefono(txtTelefono.getText());
                    u.setCorreo(txtCorreo.getText());
                    u.setEstado(chkEstado.isSelected() ? 1 : 0);
                    u.setIdRol(idRol);
                    return u;
                } catch (NumberFormatException e) {
                    mostrarError("El Id Rol debe ser un número.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    // ===================== CRUD PRODUCTOS =====================

    @FXML
    private void nuevoProducto() {
        Dialog<Producto> dialog = crearDialogoProducto(null);
        dialog.showAndWait().ifPresent(p -> {
            if (productoDAO.insertar(p)) {
                mostrarProductos();
            } else {
                mostrarError("No se pudo crear el producto.");
            }
        });
    }

    @FXML
    private void editarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Selecciona un producto de la tabla.");
            return;
        }
        Dialog<Producto> dialog = crearDialogoProducto(seleccionado);
        dialog.showAndWait().ifPresent(p -> {
            if (productoDAO.actualizar(p)) {
                mostrarProductos();
            } else {
                mostrarError("No se pudo actualizar el producto.");
            }
        });
    }

    @FXML
    private void eliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Selecciona un producto de la tabla.");
            return;
        }
        if (confirmar("¿Deseas desactivar el producto \"" + seleccionado.getNombre() + "\"?")) {
            if (productoDAO.eliminar(seleccionado.getIdProducto())) {
                mostrarProductos();
            } else {
                mostrarError("No se pudo eliminar el producto.");
            }
        }
    }

    private Dialog<Producto> crearDialogoProducto(Producto existente) {
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Nuevo producto" : "Editar producto");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtCodigo = new TextField();
        TextField txtNombre = new TextField();
        TextField txtDescripcion = new TextField();
        TextField txtPrecio = new TextField();
        TextField txtStock = new TextField();
        CheckBox chkEstado = new CheckBox("Activo");
        TextField txtIdCategoria = new TextField();

        if (existente != null) {
            txtCodigo.setText(existente.getCodigo());
            txtNombre.setText(existente.getNombre());
            txtDescripcion.setText(existente.getDescripcion());
            txtPrecio.setText(existente.getPrecio() != null ? existente.getPrecio().toString() : "");
            txtStock.setText(String.valueOf(existente.getStock()));
            chkEstado.setSelected(existente.getEstado() == 1);
            txtIdCategoria.setText(String.valueOf(existente.getIdCategoria()));
        } else {
            chkEstado.setSelected(true);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Código:"), txtCodigo);
        grid.addRow(1, new Label("Nombre:"), txtNombre);
        grid.addRow(2, new Label("Descripción:"), txtDescripcion);
        grid.addRow(3, new Label("Precio:"), txtPrecio);
        grid.addRow(4, new Label("Stock:"), txtStock);
        grid.addRow(5, new Label("Id Categoría:"), txtIdCategoria);
        grid.addRow(6, chkEstado);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                try {
                    Producto p = existente == null ? new Producto() : existente;
                    p.setCodigo(txtCodigo.getText());
                    p.setNombre(txtNombre.getText());
                    p.setDescripcion(txtDescripcion.getText());
                    p.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
                    p.setStock(Integer.parseInt(txtStock.getText().trim()));
                    p.setEstado(chkEstado.isSelected() ? 1 : 0);
                    p.setIdCategoria(Integer.parseInt(txtIdCategoria.getText().trim()));
                    return p;
                } catch (NumberFormatException e) {
                    mostrarError("Revisa que precio, stock e id categoría sean números válidos.");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    // ===================== CRUD CATEGORÍAS =====================

    @FXML
    private void nuevaCategoria() {
        Dialog<Categoria> dialog = crearDialogoCategoria(null);
        dialog.showAndWait().ifPresent(c -> {
            if (categoriaDAO.insertar(c)) {
                mostrarCategorias();
            } else {
                mostrarError("No se pudo crear la categoría.");
            }
        });
    }

    @FXML
    private void editarCategoria() {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAdvertencia("Selecciona una categoría de la tabla.");
            return;
        }
        Dialog<Categoria> dialog = crearDialogoCategoria(seleccionada);
        dialog.showAndWait().ifPresent(c -> {
            if (categoriaDAO.actualizar(c)) {
                mostrarCategorias();
            } else {
                mostrarError("No se pudo actualizar la categoría.");
            }
        });
    }

    @FXML
    private void eliminarCategoria() {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAdvertencia("Selecciona una categoría de la tabla.");
            return;
        }
        if (confirmar("¿Deseas eliminar la categoría \"" + seleccionada.getNombre() + "\"?\n"
                + "Ten en cuenta que si hay productos asociados, la eliminación puede fallar.")) {
            if (categoriaDAO.eliminar(seleccionada.getIdCategoria())) {
                mostrarCategorias();
            } else {
                mostrarError("No se pudo eliminar la categoría (puede tener productos asociados).");
            }
        }
    }

    private Dialog<Categoria> crearDialogoCategoria(Categoria existente) {
        Dialog<Categoria> dialog = new Dialog<>();
        dialog.setTitle(existente == null ? "Nueva categoría" : "Editar categoría");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField();
        TextField txtDescripcion = new TextField();

        if (existente != null) {
            txtNombre.setText(existente.getNombre());
            txtDescripcion.setText(existente.getDescripcion());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Nombre:"), txtNombre);
        grid.addRow(1, new Label("Descripción:"), txtDescripcion);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                Categoria c = existente == null ? new Categoria() : existente;
                c.setNombre(txtNombre.getText());
                c.setDescripcion(txtDescripcion.getText());
                return c;
            }
            return null;
        });

        return dialog;
    }

    // ===================== VENTAS (solo consulta) =====================

    @FXML
    private void verDetalleVenta() {
        Venta seleccionada = tablaVentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAdvertencia("Selecciona una venta de la tabla.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle de venta");
        alert.setHeaderText("Venta #" + seleccionada.getIdVenta());
        alert.setContentText(
                "Fecha: " + seleccionada.getFecha() + "\n"
                        + "Subtotal: " + seleccionada.getSubtotal() + "\n"
                        + "IVA: " + seleccionada.getIva() + "\n"
                        + "Total: " + seleccionada.getTotal() + "\n"
                        + "Usuario: " + seleccionada.getIdUsuario()
        );
        alert.showAndWait();
    }

    // ===================== UTILIDADES =====================

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.setTitle("Error");
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING, mensaje, ButtonType.OK);
        alert.setTitle("Atención");
        alert.showAndWait();
    }

    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmar");
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.YES;
    }
}