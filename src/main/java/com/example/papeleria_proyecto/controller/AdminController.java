package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.UsuarioDAO;
import com.example.papeleria_proyecto.model.Usuario;
import com.example.papeleria_proyecto.dao.ProductoDAO;
import com.example.papeleria_proyecto.model.Producto;
import com.example.papeleria_proyecto.dao.CategoriaDAO;
import com.example.papeleria_proyecto.model.Categoria;
import com.example.papeleria_proyecto.dao.ProveedorDAO;
import com.example.papeleria_proyecto.model.Proveedor;
import java.math.BigDecimal;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colIdUsuario;
    @FXML
    private TableColumn<Usuario, String> colNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> colApellidoUsuario;
    @FXML
    private TableColumn<Usuario, String> colUsuario;
    @FXML
    private TableColumn<Usuario, String> colTelefono;
    @FXML
    private TableColumn<Usuario, String> colCorreo;
    @FXML
    private TableColumn<Usuario, String> colEstadoUsuario;
    @FXML
    private TableColumn<Usuario, String> colIdRol;
    @FXML
    private TextField txtBuscarUsuarios;
    @FXML
    private VBox panelFormularioUsuario;
    @FXML
    private Label lblTituloFormulario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private VBox panelFormularioProducto;
    @FXML
    private Label lblTituloFormularioProducto;
    @FXML
    private TextField txtCodigoProducto;
    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtDescripcionProducto;
    @FXML
    private TextField txtPrecioProducto;
    @FXML
    private TextField txtStockProducto;
    @FXML
    private ComboBox<String> cmbCategoriaProducto;
    @FXML
    private ComboBox<String> cmbEstadoProducto;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, Integer> colIdProducto;
    @FXML
    private TableColumn<Producto, String> colCodigoProducto;
    @FXML
    private TableColumn<Producto, String> colNombreProducto;
    @FXML
    private TableColumn<Producto, String> colDescripcionProducto;
    @FXML
    private TableColumn<Producto, BigDecimal> colPrecioProducto;
    @FXML
    private TableColumn<Producto, Integer> colStockProducto;
    @FXML
    private TableColumn<Producto, String> colEstadoProducto;
    @FXML
    private TableColumn<Producto, String> colCategoriaProducto;
    @FXML
    private TextField txtBuscarProductos;
    @FXML
    private VBox panelUsuarios;
    @FXML
    private VBox panelProductos;
    @FXML
    private VBox panelCategorias;
    @FXML
    private VBox panelProveedores;
    @FXML
    private VBox panelVentas;
    @FXML
    private VBox panelReportes;
    @FXML
    private TableView<Proveedor> tablaProveedores;
    @FXML
    private TableColumn<Proveedor, Integer> colIdProveedor;
    @FXML
    private TableColumn<Proveedor, String> colNombreProveedor;
    @FXML
    private TableColumn<Proveedor, String> colTelefonoProveedor;
    @FXML
    private TableColumn<Proveedor, String> colCorreoProveedor;
    @FXML
    private TableColumn<Proveedor, String> colDireccionProveedor;
    @FXML
    private VBox panelFormularioProveedor;
    @FXML
    private Label lblTituloFormularioProveedor;
    @FXML
    private TextField txtNombreProveedor;
    @FXML
    private TextField txtTelefonoProveedor;
    @FXML
    private TextField txtCorreoProveedor;
    @FXML
    private TextField txtDireccionProveedor;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();


    private final ObservableList<Usuario> listaUsuarios =
            FXCollections.observableArrayList();

    private Usuario usuarioEditar;

    private boolean modoEdicion = false;

    @FXML
    public void initialize() {

        configurarTablaUsuarios();
        configurarFormularioUsuario();
        configurarTablaProductos();
        configurarFormularioProducto();
        configurarTablaCategorias();
        configurarTablaProveedores();
    }
    private void configurarTablaUsuarios() {


        colIdUsuario.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getIdUsuario()

                )

        );


        colNombreUsuario.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getNombre()

                )

        );


        colApellidoUsuario.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getApellido()

                )

        );


        colUsuario.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getUsuario()

                )

        );


        colTelefono.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getTelefono()

                )

        );


        colCorreo.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getCorreo()

                )

        );


        colEstadoUsuario.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getEstadoTexto()

                )

        );


        colIdRol.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        obtenerNombreRol(

                                celda.getValue().getIdRol()

                        )

                )

        );


        tablaUsuarios.setItems(listaUsuarios);

    }

    // CONFIGURAR FORMULARIO

    private void configurarFormularioUsuario() {


        cmbRol.getItems().addAll(

                "Administrador",

                "Cajero",

                "Bodeguero"

        );


        cmbEstado.getItems().addAll(

                "Activo",

                "Inactivo"

        );

    }

    // CARGAR USUARIOS
    private void cargarUsuarios() {

        Task<ObservableList<Usuario>> task = new Task<>() {

            @Override
            protected ObservableList<Usuario> call() {

                return FXCollections.observableArrayList(
                        usuarioDAO.listarTodos()
                );

            }

        };

        task.setOnSucceeded(event -> {

            listaUsuarios.setAll(task.getValue());

        });

        task.setOnFailed(event -> {

            task.getException().printStackTrace();

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudieron cargar los usuarios."
            );

        });

        Thread hilo = new Thread(task);

        hilo.setDaemon(true);

        hilo.start();

    }
    // MOSTRAR USUARIOS

    @FXML
    private void mostrarUsuarios() {


        ocultarPaneles();


        panelUsuarios.setVisible(true);


        cargarUsuarios();

    }


    @FXML
    private void mostrarTablaUsuarios() {


        cargarUsuarios();

    }

    // BUSCAR USUARIOS

    @FXML
    private void buscarUsuarios() {


        String texto =

                txtBuscarUsuarios

                        .getText()

                        .trim()

                        .toLowerCase();


        if (texto.isEmpty()) {


            cargarUsuarios();


            return;

        }


        ObservableList<Usuario> resultados =

                FXCollections.observableArrayList();


        for (Usuario u : usuarioDAO.listarTodos()) {


            boolean coincide =


                    u.getNombre()

                            .toLowerCase()

                            .contains(texto)


                            ||


                            u.getApellido()

                                    .toLowerCase()

                                    .contains(texto)


                            ||


                            u.getUsuario()

                                    .toLowerCase()

                                    .contains(texto)


                            ||


                            u.getCorreo()

                                    .toLowerCase()

                                    .contains(texto);


            if (coincide) {


                resultados.add(u);

            }

        }


        listaUsuarios.setAll(resultados);

    }
    // NUEVO USUARIO


    @FXML
    private void nuevoUsuario() {


        modoEdicion = false;


        usuarioEditar = null;


        limpiarFormularioUsuario();


        lblTituloFormulario.setText(

                "Nuevo Usuario"

        );


        cmbRol

                .getSelectionModel()

                .selectFirst();


        cmbEstado

                .getSelectionModel()

                .select("Activo");


        mostrarFormularioUsuario();

    }
    // EDITAR USUARIO

    @FXML
    private void editarUsuario() {


        Usuario seleccionado =


                tablaUsuarios

                        .getSelectionModel()

                        .getSelectedItem();


        if (seleccionado == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione un usuario para editar."

            );


            return;

        }


        modoEdicion = true;


        usuarioEditar = seleccionado;


        lblTituloFormulario.setText(

                "Editar Usuario"

        );


        txtNombre.setText(

                seleccionado.getNombre()

        );


        txtApellido.setText(

                seleccionado.getApellido()

        );


        txtUsuario.setText(

                seleccionado.getUsuario()

        );


        txtContrasena.setText(

                seleccionado.getContrasena()

        );


        txtTelefono.setText(

                seleccionado.getTelefono()

        );


        txtCorreo.setText(

                seleccionado.getCorreo()

        );


        cmbRol

                .getSelectionModel()

                .select(

                        obtenerNombreRol(

                                seleccionado.getIdRol()

                        )

                );


        cmbEstado

                .getSelectionModel()

                .select(

                        seleccionado.getEstado() == 1

                                ? "Activo"

                                : "Inactivo"

                );


        mostrarFormularioUsuario();

    }

    // GUARDAR USUARIO
    @FXML
    private void guardarUsuario() {


        String nombre =

                txtNombre

                        .getText()

                        .trim();


        String apellido =

                txtApellido

                        .getText()

                        .trim();


        String usuario =

                txtUsuario

                        .getText()

                        .trim();


        String contrasena =

                txtContrasena

                        .getText();


        String telefono =

                txtTelefono

                        .getText()

                        .trim();


        String correo =

                txtCorreo

                        .getText()

                        .trim();


        if (

                nombre.isEmpty()

                        || apellido.isEmpty()

                        || usuario.isEmpty()

                        || contrasena.isEmpty()

                        || telefono.isEmpty()

                        || correo.isEmpty()

        ) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Campos incompletos",

                    "Complete todos los campos."

            );


            return;

        }


        if (cmbRol.getValue() == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Rol requerido",

                    "Seleccione un rol."

            );


            return;

        }


        if (cmbEstado.getValue() == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Estado requerido",

                    "Seleccione un estado."

            );


            return;

        }


        int idRol =

                obtenerIdRol(

                        cmbRol.getValue()

                );


        int estado =


                cmbEstado

                        .getValue()

                        .equals("Activo")

                        ? 1

                        : 0;


        Usuario usuarioGuardar =


                new Usuario(

                        nombre,

                        apellido,

                        usuario,

                        contrasena,

                        telefono,

                        correo,

                        estado,

                        idRol

                );


        boolean resultado;


        if (modoEdicion) {


            usuarioGuardar.setIdUsuario(

                    usuarioEditar.getIdUsuario()

            );


            resultado =

                    usuarioDAO.actualizar(

                            usuarioGuardar

                    );


        } else {


            resultado =

                    usuarioDAO.insertar(

                            usuarioGuardar

                    );

        }


        if (resultado) {


            mostrarMensaje(

                    Alert.AlertType.INFORMATION,

                    "Éxito",

                    modoEdicion

                            ? "Usuario actualizado correctamente."

                            : "Usuario registrado correctamente."

            );


            cargarUsuarios();


            cancelarFormularioUsuario();


        } else {


            mostrarMensaje(

                    Alert.AlertType.ERROR,

                    "Error",

                    "No se pudo guardar el usuario."

            );

        }

    }

    // CANCELAR FORMULARIO
    @FXML
    private void cancelarFormularioUsuario() {


        limpiarFormularioUsuario();


        usuarioEditar = null;


        modoEdicion = false;


        panelFormularioUsuario.setVisible(false);


        panelFormularioUsuario.setManaged(false);

    }

    // MOSTRAR FORMULARIO

    private void mostrarFormularioUsuario() {


        panelFormularioUsuario.setVisible(true);


        panelFormularioUsuario.setManaged(true);

    }

    // LIMPIAR FORMULARIO
    private void limpiarFormularioUsuario() {


        txtNombre.clear();

        txtApellido.clear();

        txtUsuario.clear();

        txtContrasena.clear();

        txtTelefono.clear();

        txtCorreo.clear();


        cmbRol

                .getSelectionModel()

                .clearSelection();


        cmbEstado

                .getSelectionModel()

                .clearSelection();

    }

    // ELIMINAR USUARIO
    @FXML
    private void eliminarUsuario() {


        Usuario seleccionado =


                tablaUsuarios

                        .getSelectionModel()

                        .getSelectedItem();


        if (seleccionado == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione un usuario."

            );


            return;

        }


        Alert confirmacion =


                new Alert(

                        Alert.AlertType.CONFIRMATION

                );


        confirmacion.setTitle(

                "Eliminar usuario"

        );


        confirmacion.setHeaderText(null);


        confirmacion.setContentText(

                "¿Desea desactivar al usuario "

                        + seleccionado.getUsuario()

                        + "?"

        );


        if (


                confirmacion

                        .showAndWait()

                        .orElse(ButtonType.CANCEL)

                        == ButtonType.OK

        ) {


            boolean eliminado =


                    usuarioDAO.eliminar(

                            seleccionado.getIdUsuario()

                    );


            if (eliminado) {


                cargarUsuarios();


                mostrarMensaje(

                        Alert.AlertType.INFORMATION,

                        "Éxito",

                        "Usuario desactivado correctamente."

                );

            }

        }

    }

    // CONVERTIR ROL

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

    //  PRODUCTOS
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<Producto> listaProductos =
            FXCollections.observableArrayList();
    private Producto productoEditar;
    private boolean modoEdicionProducto = false;
    //Categoria
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ObservableList<Categoria> listaCategorias =
            FXCollections.observableArrayList();
    @FXML
    private TableView<Categoria> tablaCategorias;

    @FXML
    private TableColumn<Categoria, Integer> colIdCategoria;

    @FXML
    private TableColumn<Categoria, String> colNombreCategoria;

    @FXML
    private TableColumn<Categoria, String> colDescripcionCategoria;

    @FXML
    private VBox panelFormularioCategoria;

    @FXML
    private Label lblTituloFormularioCategoria;

    @FXML
    private TextField txtNombreCategoria;

    @FXML
    private TextField txtDescripcionCategoria;

    private Categoria categoriaEditar;

    private boolean modoEdicionCategoria = false;
    private void configurarTablaProductos() {


        colIdProducto.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getIdProducto()

                )

        );


        colCodigoProducto.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getCodigo()

                )

        );


        colNombreProducto.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getNombre()

                )

        );
        colDescripcionProducto.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getDescripcion()

                )

        );
        colPrecioProducto.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getPrecio()

                )

        );
        colStockProducto.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getStock()

                )

        );


        colEstadoProducto.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getEstado() == 1

                                ? "Activo"

                                : "Inactivo"

                )

        );


        colCategoriaProducto.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        obtenerNombreCategoria(

                                celda.getValue().getIdCategoria()

                        )

                )

        );


        tablaProductos.setItems(listaProductos);

    }
    private void configurarFormularioProducto() {


        cmbEstadoProducto.getItems().addAll(

                "Activo",

                "Inactivo"

        );
        cargarCategoriasProducto();

    }
    private void cargarProductos() {


        listaProductos.clear();


        listaProductos.addAll(

                productoDAO.listarTodos()

        );

    }
    @FXML
    private void mostrarProductos() {


        ocultarPaneles();


        panelProductos.setVisible(true);


        cargarProductos();

    }
    @FXML
    private void mostrarTablaProductos() {


        cargarProductos();

    }
    @FXML
    private void nuevoProducto() {


        modoEdicionProducto = false;


        productoEditar = null;


        limpiarFormularioProducto();


        lblTituloFormularioProducto.setText(

                "Nuevo Producto"

        );
        cmbEstadoProducto

                .getSelectionModel()

                .select("Activo");


        mostrarFormularioProducto();

    }
    @FXML
    private void editarProducto() {


        Producto seleccionado =


                tablaProductos

                        .getSelectionModel()

                        .getSelectedItem();


        if (seleccionado == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione un producto para editar."

            );


            return;

        }

        modoEdicionProducto = true;

        productoEditar = seleccionado;


        lblTituloFormularioProducto.setText(

                "Editar Producto"

        );


        txtCodigoProducto.setText(

                seleccionado.getCodigo()

        );
        txtNombreProducto.setText(

                seleccionado.getNombre()

        );


        txtDescripcionProducto.setText(

                seleccionado.getDescripcion()

        );


        txtPrecioProducto.setText(

                seleccionado.getPrecio().toString()

        );


        txtStockProducto.setText(

                String.valueOf(

                        seleccionado.getStock()

                )

        );


        cmbEstadoProducto

                .getSelectionModel()

                .select(

                        seleccionado.getEstado() == 1

                                ? "Activo"

                                : "Inactivo"

                );
        Categoria categoria = categoriaDAO.buscarPorId(

                seleccionado.getIdCategoria()

        );

        if (categoria != null) {

            cmbCategoriaProducto

                    .getSelectionModel()

                    .select(

                            categoria.getIdCategoria()

                                    + " - "

                                    + categoria.getNombre()

                    );

        }
        mostrarFormularioProducto();
    }
    @FXML
    private void guardarProducto() {


        try {


            String codigo =

                    txtCodigoProducto

                            .getText()

                            .trim();


            String nombre =

                    txtNombreProducto

                            .getText()

                            .trim();


            String descripcion =

                    txtDescripcionProducto

                            .getText()

                            .trim();


            BigDecimal precio =

                    new BigDecimal(

                            txtPrecioProducto

                                    .getText()

                                    .trim()

                    );


            int stock =

                    Integer.parseInt(

                            txtStockProducto

                                    .getText()

                                    .trim()

                    );


            if (cmbEstadoProducto.getValue() == null) {
                mostrarMensaje(Alert.AlertType.WARNING,
                        "Estado requerido",
                        "Seleccione el estado del producto."
                );
                return;
            }
            int estado =
                    cmbEstadoProducto
                            .getValue()
                            .equals("Activo")
                            ? 1
                            : 0;
            int idCategoria = obtenerIdCategoriaSeleccionada();
            if (idCategoria == 0) {
                mostrarMensaje(Alert.AlertType.WARNING,
                        "Categoría requerida",
                        "Seleccione una categoría."

                );
                return;
            }
            if (codigo.isEmpty() || nombre.isEmpty()) {
                mostrarMensaje(
                        Alert.AlertType.WARNING,

                        "Campos incompletos",

                        "Código y nombre son obligatorios."

                );


                return;

            }


            Producto producto =


                    new Producto(

                            codigo,

                            nombre,

                            descripcion,

                            precio,

                            stock,

                            estado,

                            idCategoria

                    );
            boolean resultado;

            if (modoEdicionProducto) {


                producto.setIdProducto(

                        productoEditar.getIdProducto()

                );


                resultado =

                        productoDAO.actualizar(

                                producto

                        );


            } else {


                resultado =

                        productoDAO.insertar(

                                producto

                        );

            }


            if (resultado) {


                mostrarMensaje(

                        Alert.AlertType.INFORMATION,

                        "Éxito",

                        modoEdicionProducto

                                ? "Producto actualizado correctamente."

                                : "Producto registrado correctamente."

                );
                cargarProductos();
                cancelarFormularioProducto();
            } else {
                mostrarMensaje(

                        Alert.AlertType.ERROR,

                        "Error",

                        "No se pudo guardar el producto."

                );

            }
        } catch (NumberFormatException e) {
            mostrarMensaje(

                    Alert.AlertType.ERROR,

                    "Datos inválidos",

                    "Precio y stock deben tener valores válidos."

            );

        }
    }
    @FXML
    private void cancelarFormularioProducto() {
        limpiarFormularioProducto();
        productoEditar = null;
        modoEdicionProducto = false;
        panelFormularioProducto.setVisible(false);
        panelFormularioProducto.setManaged(false);

    }
    private void mostrarFormularioProducto() {
        panelFormularioProducto.setVisible(true);
        panelFormularioProducto.setManaged(true);

    }
    private void limpiarFormularioProducto() {

        txtCodigoProducto.clear();
        txtNombreProducto.clear();
        txtDescripcionProducto.clear();
        txtPrecioProducto.clear();
        txtStockProducto.clear();
        cmbCategoriaProducto

                .getSelectionModel()
                .clearSelection();

        cmbEstadoProducto

                .getSelectionModel()
                .clearSelection();

    }
    @FXML
    private void eliminarProducto() {
        Producto seleccionado =
                tablaProductos

                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {


            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione un producto."

            );


            return;

        }


        Alert confirmacion =


                new Alert(

                        Alert.AlertType.CONFIRMATION

                );


        confirmacion.setTitle(

                "Eliminar producto"

        );


        confirmacion.setHeaderText(null);


        confirmacion.setContentText(

                "¿Desea desactivar el producto "

                        + seleccionado.getNombre()

                        + "?"

        );


        if (
                confirmacion

                        .showAndWait()

                        .orElse(ButtonType.CANCEL)

                        == ButtonType.OK

        ) {
            boolean eliminado =
                    productoDAO.eliminar(

                            seleccionado.getIdProducto()

                    );


            if (eliminado) {
                cargarProductos();
                mostrarMensaje(

                        Alert.AlertType.INFORMATION,

                        "Éxito",

                        "Producto desactivado correctamente."

                );

            }

        }

    }
    @FXML
    private void buscarProductos() {

        String texto = txtBuscarProductos.getText()
                .trim()
                .toLowerCase();

        if (texto.isEmpty()) {
            cargarProductos();
            return;
        }

        ObservableList<Producto> resultados =
                FXCollections.observableArrayList();

        for (Producto p : productoDAO.listarTodos()) {

            boolean coincide =
                    p.getCodigo().toLowerCase().contains(texto)
                            ||
                            p.getNombre().toLowerCase().contains(texto)
                            ||
                            p.getDescripcion().toLowerCase().contains(texto);

            if (coincide) {
                resultados.add(p);
            }
        }

        listaProductos.setAll(resultados);
    }
    //  CATEGORÍAS
    private String obtenerNombreCategoria(int idCategoria) {

        Categoria categoria = categoriaDAO.buscarPorId(idCategoria);

        if (categoria != null) {
            return categoria.getNombre();
        }

        return "Sin categoría";
    }
    private void cargarCategoriasProducto() {

        cmbCategoriaProducto.getItems().clear();

        for (Categoria categoria : categoriaDAO.listarTodos()) {

            cmbCategoriaProducto.getItems().add(

                    categoria.getIdCategoria()
                            + " - "
                            + categoria.getNombre()

            );

        }
    }
    private int obtenerIdCategoriaSeleccionada() {

        String seleccion = cmbCategoriaProducto.getValue();

        if (seleccion == null || seleccion.isEmpty()) {

            return 0;

        }

        return Integer.parseInt(

                seleccion.split(" - ")[0]

        );

    }
    @FXML
    private void mostrarCategorias() {
        ocultarPaneles();
        panelCategorias.setVisible(true);
        cargarCategorias();

    }
    private void configurarTablaCategorias() {

        colIdCategoria.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getIdCategoria()

                )

        );
        colNombreCategoria.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getNombre()

                )

        );

        colDescripcionCategoria.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getDescripcion()

                )

        );

        tablaCategorias.setItems(listaCategorias);

    }
    private void cargarCategorias() {

        listaCategorias.clear();

        listaCategorias.addAll(

                categoriaDAO.listarTodos()

        );

    }
    @FXML
    private void mostrarTablaCategorias() {
        cargarCategorias();
    }
    @FXML
    private void nuevaCategoria() {
        modoEdicionCategoria = false;
        categoriaEditar = null;
        limpiarFormularioCategoria();
        lblTituloFormularioCategoria.setText(
                "Nueva Categoría"

        );
        mostrarFormularioCategoria();
    }
    @FXML
    private void editarCategoria() {

        Categoria seleccionada =

                tablaCategorias
                        .getSelectionModel()
                        .getSelectedItem();
        if (seleccionada == null) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione una categoría para editar."
            );
            return;
        }
        modoEdicionCategoria = true;
        categoriaEditar = seleccionada;
        lblTituloFormularioCategoria.setText(
                "Editar Categoría"
        );
        txtNombreCategoria.setText(
                seleccionada.getNombre()
        );
        txtDescripcionCategoria.setText(
                seleccionada.getDescripcion()
        );
        mostrarFormularioCategoria();
    }
    @FXML
    private void guardarCategoria() {

        String nombre =

                txtNombreCategoria
                        .getText()
                        .trim();

        String descripcion =

                txtDescripcionCategoria
                        .getText()
                        .trim();

        if (nombre.isEmpty()) {

            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Campo incompleto",

                    "El nombre de la categoría es obligatorio."

            );

            return;

        }

        Categoria categoria =

                new Categoria(

                        nombre,
                        descripcion

                );
        boolean resultado;
        if (modoEdicionCategoria) {
            categoria.setIdCategoria(
                    categoriaEditar.getIdCategoria()
            );
            resultado =
                    categoriaDAO.actualizar(categoria);
        } else {
            resultado = categoriaDAO.insertar(categoria);
        }
        if (resultado) {
            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    modoEdicionCategoria
                            ? "Categoría actualizada correctamente."
                            : "Categoría registrada correctamente."
            );
            cargarCategorias();
            cargarCategoriasProducto();
            cancelarFormularioCategoria();
        } else {
            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo guardar la categoría."
            );
        }
    }
    @FXML
    private void cancelarFormularioCategoria() {

        limpiarFormularioCategoria();

        categoriaEditar = null;

        modoEdicionCategoria = false;

        panelFormularioCategoria.setVisible(false);

        panelFormularioCategoria.setManaged(false);

    }
    private void mostrarFormularioCategoria() {

        panelFormularioCategoria.setVisible(true);

        panelFormularioCategoria.setManaged(true);

    }
    private void limpiarFormularioCategoria() {

        txtNombreCategoria.clear();

        txtDescripcionCategoria.clear();

    }
    @FXML
    private void eliminarCategoria() {

        Categoria seleccionada =

                tablaCategorias
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionada == null) {

            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione una categoría."

            );

            return;

        }

        Alert confirmacion =

                new Alert(

                        Alert.AlertType.CONFIRMATION

                );

        confirmacion.setTitle(

                "Eliminar categoría"

        );

        confirmacion.setHeaderText(null);

        confirmacion.setContentText(

                "¿Desea eliminar la categoría "

                        + seleccionada.getNombre()

                        + "?"

        );

        if (

                confirmacion
                        .showAndWait()
                        .orElse(ButtonType.CANCEL)
                        == ButtonType.OK

        ) {

            boolean eliminado =

                    categoriaDAO.eliminar(

                            seleccionada.getIdCategoria()

                    );

            if (eliminado) {

                cargarCategorias();

                cargarCategoriasProducto();

                mostrarMensaje(

                        Alert.AlertType.INFORMATION,

                        "Éxito",

                        "Categoría eliminada correctamente."

                );

            }

        }

    }
    // PROVEEDORES
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    private final ObservableList<Proveedor> listaProveedores =
            FXCollections.observableArrayList();

    private Proveedor proveedorEditar;

    private boolean modoEdicionProveedor = false;
    private void configurarTablaProveedores() {

        colIdProveedor.setCellValueFactory(

                celda -> new SimpleObjectProperty<>(

                        celda.getValue().getIdProveedor()

                )

        );

        colNombreProveedor.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getNombre()

                )

        );

        colTelefonoProveedor.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getTelefono()

                )

        );

        colCorreoProveedor.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getCorreo()

                )

        );

        colDireccionProveedor.setCellValueFactory(

                celda -> new SimpleStringProperty(

                        celda.getValue().getDireccion()

                )

        );

        tablaProveedores.setItems(listaProveedores);
    }
    private void cargarProveedores() {

        listaProveedores.clear();

        listaProveedores.addAll(

                proveedorDAO.listarTodos()

        );
    }
    @FXML
    private void mostrarProveedores() {

        ocultarPaneles();

        panelProveedores.setVisible(true);

        cargarProveedores();
    }
    @FXML
    private void mostrarTablaProveedores() {

        cargarProveedores();
    }
    @FXML
    private void nuevoProveedor() {
        modoEdicionProveedor = false;
        proveedorEditar = null;
        limpiarFormularioProveedor();
        lblTituloFormularioProveedor.setText(
                "Nuevo Proveedor"
        );
        mostrarFormularioProveedor();
    }
    @FXML
    private void editarProveedor() {
        Proveedor seleccionado =

                tablaProveedores
                        .getSelectionModel()
                        .getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Atención",
                    "Seleccione un proveedor para editar."
            );
            return;
        }
        modoEdicionProveedor = true;
        proveedorEditar = seleccionado;
        lblTituloFormularioProveedor.setText(
                "Editar Proveedor"
        );
        txtNombreProveedor.setText(
                seleccionado.getNombre()
        );
        txtTelefonoProveedor.setText(
                seleccionado.getTelefono()
        );
        txtCorreoProveedor.setText(
                seleccionado.getCorreo()
        );
        txtDireccionProveedor.setText(
                seleccionado.getDireccion()
        );
        mostrarFormularioProveedor();
    }
    @FXML
    private void guardarProveedor() {

        String nombre =

                txtNombreProveedor
                        .getText()
                        .trim();

        String telefono =

                txtTelefonoProveedor
                        .getText()
                        .trim();

        String correo =

                txtCorreoProveedor
                        .getText()
                        .trim();

        String direccion =

                txtDireccionProveedor
                        .getText()
                        .trim();

        if (nombre.isEmpty()) {
            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Campo incompleto",
                    "El nombre del proveedor es obligatorio."
            );
            return;
        }
        Proveedor proveedor =
                new Proveedor(
                        nombre,
                        telefono,
                        correo,
                        direccion
                );
        boolean resultado;
        if (modoEdicionProveedor) {
            proveedor.setIdProveedor(
                    proveedorEditar.getIdProveedor()
            );
            resultado = proveedorDAO.actualizar(proveedor);
        } else {
            resultado = proveedorDAO.insertar(proveedor);
        }
        if (resultado) {
            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    modoEdicionProveedor
                            ? "Proveedor actualizado correctamente."
                            : "Proveedor registrado correctamente."

            );
            cargarProveedores();
            cancelarFormularioProveedor();
        } else {
            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo guardar el proveedor."
            );
        }

    }
    @FXML
    private void cancelarFormularioProveedor() {
        limpiarFormularioProveedor();
        proveedorEditar = null;
        modoEdicionProveedor = false;
        panelFormularioProveedor.setVisible(false);
        panelFormularioProveedor.setManaged(false);
    }
    private void mostrarFormularioProveedor() {

        panelFormularioProveedor.setVisible(true);

        panelFormularioProveedor.setManaged(true);
    }
    private void limpiarFormularioProveedor() {

        txtNombreProveedor.clear();

        txtTelefonoProveedor.clear();

        txtCorreoProveedor.clear();

        txtDireccionProveedor.clear();
    }
    @FXML
    private void eliminarProveedor() {

        Proveedor seleccionado =

                tablaProveedores
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarMensaje(

                    Alert.AlertType.WARNING,

                    "Atención",

                    "Seleccione un proveedor."

            );

            return;
        }

        Alert confirmacion =

                new Alert(

                        Alert.AlertType.CONFIRMATION

                );

        confirmacion.setTitle(

                "Eliminar proveedor"

        );

        confirmacion.setHeaderText(null);

        confirmacion.setContentText(

                "¿Desea eliminar el proveedor "

                        + seleccionado.getNombre()

                        + "?"

        );

        if (

                confirmacion
                        .showAndWait()
                        .orElse(ButtonType.CANCEL)
                        == ButtonType.OK

        ) {

            boolean eliminado =

                    proveedorDAO.eliminar(

                            seleccionado.getIdProveedor()

                    );

            if (eliminado) {

                cargarProveedores();

                mostrarMensaje(

                        Alert.AlertType.INFORMATION,

                        "Éxito",

                        "Proveedor eliminado correctamente."

                );
            }
        }
    }
    // MOSTRAR VENTAS
    @FXML
    private void mostrarVentas() {


        ocultarPaneles();


        panelVentas.setVisible(true);

    }


    // MOSTRAR REPORTES

    @FXML
    private void mostrarReportes() {


        ocultarPaneles();


        panelReportes.setVisible(true);

    }


    // =====================================================
    // OCULTAR PANELES
    // =====================================================

    private void ocultarPaneles() {


        panelUsuarios.setVisible(false);


        panelProductos.setVisible(false);


        panelCategorias.setVisible(false);


        panelProveedores.setVisible(false);


        panelVentas.setVisible(false);


        panelReportes.setVisible(false);

    }


    // =====================================================
    // CERRAR SESIÓN
    // =====================================================

    @FXML
    private void cerrarSesion() {


        try {


            Parent root =


                    FXMLLoader.load(

                            getClass().getResource(

                                    "/com/example/papeleria_proyecto/view/login.fxml"

                            )

                    );


            Stage stage =


                    (Stage) tablaUsuarios

                            .getScene()

                            .getWindow();


            stage.setScene(

                    new Scene(root)

            );


            stage.show();


        } catch (Exception e) {


            e.printStackTrace();

        }

    }


    // =====================================================
    // MENSAJES
    // =====================================================

    private void mostrarMensaje(


            Alert.AlertType tipo,


            String titulo,


            String mensaje

    ) {


        Alert alert =


                new Alert(tipo);


        alert.setTitle(titulo);


        alert.setHeaderText(null);


        alert.setContentText(mensaje);


        alert.showAndWait();

    }

}