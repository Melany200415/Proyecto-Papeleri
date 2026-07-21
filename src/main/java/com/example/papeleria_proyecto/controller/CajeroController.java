package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.ProductoDAO;
import com.example.papeleria_proyecto.dao.VentaDAO;
import com.example.papeleria_proyecto.model.DetalleVenta;
import com.example.papeleria_proyecto.model.Producto;
import com.example.papeleria_proyecto.model.Usuario;
import com.example.papeleria_proyecto.model.Venta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CajeroController implements Initializable {

    // --- CONTROLES FXML DE BÚSQUEDA Y PRODUCTO ---
    @FXML private TextField txtBuscarProducto;
    @FXML private TextField txtProducto;
    @FXML private TextField txtStock;
    @FXML private TextField txtPrecio;
    @FXML private Spinner<Integer> spCantidad;

    // --- TABLA Y COLUMNAS DEL CARRITO ---
    @FXML private TableView<DetalleVenta> tblCarrito;
    @FXML private TableColumn<DetalleVenta, String> colCodigo;
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colPrecio;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;
    @FXML private Label lblTotal;

    // --- TABLA Y COLUMNAS HISTORIAL CAJERO ---
    @FXML private TableView<Venta> tblVentas;
    @FXML private TableColumn<Venta, Integer> colVenta;
    @FXML private TableColumn<Venta, LocalDateTime> colFecha;
    @FXML private TableColumn<Venta, Double> colTotalVenta;
    @FXML private TableColumn<Venta, String> colEstado;

    // --- LISTAS Y VARIABLES DE ESTADO ---
    private ObservableList<DetalleVenta> listaCarrito = FXCollections.observableArrayList();
    private ObservableList<Venta> listaHistorialVentas = FXCollections.observableArrayList();

    private Producto productoSeleccionado;
    private Usuario usuarioActual;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTablas();
        configurarSpinner(1);
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarHistorialCajero();
    }

    private void configurarTablas() {
        // Carrito
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblCarrito.setItems(listaCarrito);

        // Historial Cajero
        colVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTotalVenta.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tblVentas.setItems(listaHistorialVentas);
    }

    private void configurarSpinner(int maxStock) {
        int max = Math.max(1, maxStock);
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, max, 1);
        spCantidad.setValueFactory(valueFactory);
    }

    @FXML
    private void buscarProducto() {
        String criterio = txtBuscarProducto.getText().trim();
        if (criterio.isEmpty()) {
            mostrarAlerta("Atención", "Ingrese un código o nombre para buscar.", Alert.AlertType.WARNING);
            return;
        }

        List<Producto> resultados = productoDAO.buscarPorCodigoONombre(criterio);

        if (resultados.isEmpty()) {
            mostrarAlerta("Sin resultados", "No se encontró ningún producto activo.", Alert.AlertType.INFORMATION);
            limpiarFormularioProducto();
        } else if (resultados.size() == 1) {
            cargarProducto(resultados.get(0));
        } else {
            mostrarDialogoSeleccionProducto(resultados);
        }
    }

    private void cargarProducto(Producto producto) {
        if (producto.getStock() <= 0) {
            mostrarAlerta("Sin Stock", "El producto no cuenta con existencias disponibles.", Alert.AlertType.WARNING);
            limpiarFormularioProducto();
            return;
        }

        this.productoSeleccionado = producto;
        txtProducto.setText(producto.getNombre());
        txtStock.setText(String.valueOf(producto.getStock()));
        txtPrecio.setText(String.format("%.2f", producto.getPrecio().doubleValue()));

        configurarSpinner(producto.getStock());
    }

    private void mostrarDialogoSeleccionProducto(List<Producto> lista) {
        ChoiceDialog<Producto> dialog = new ChoiceDialog<>(lista.get(0), lista);
        dialog.setTitle("Seleccionar Producto");
        dialog.setHeaderText("Múltiples coincidencias encontradas:");
        dialog.setContentText("Elija un producto:");

        Optional<Producto> result = dialog.showAndWait();
        result.ifPresent(this::cargarProducto);
    }

    @FXML
    private void agregarProducto() {
        if (productoSeleccionado == null) {
            mostrarAlerta("Atención", "Debe buscar y seleccionar un producto primero.", Alert.AlertType.WARNING);
            return;
        }

        int cantidad = spCantidad.getValue();
        if (cantidad > productoSeleccionado.getStock()) {
            mostrarAlerta("Stock Insuficiente", "La cantidad supera el stock disponible (" + productoSeleccionado.getStock() + ").", Alert.AlertType.ERROR);
            return;
        }

        double precioDouble = productoSeleccionado.getPrecio().doubleValue();

        for (DetalleVenta item : listaCarrito) {
            if (item.getIdProducto() == productoSeleccionado.getIdProducto()) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (nuevaCantidad > productoSeleccionado.getStock()) {
                    mostrarAlerta("Límite Alcanzado", "Supera el stock disponible (" + productoSeleccionado.getStock() + ").", Alert.AlertType.WARNING);
                    return;
                }
                item.setCantidad(nuevaCantidad);
                item.setSubtotal(nuevaCantidad * item.getPrecioUnitario());
                tblCarrito.refresh();
                calcularTotal();
                limpiarFormularioProducto();
                return;
            }
        }

        DetalleVenta detalle = new DetalleVenta();
        detalle.setIdProducto(productoSeleccionado.getIdProducto());
        detalle.setCodigoProducto(productoSeleccionado.getCodigo());
        detalle.setNombreProducto(productoSeleccionado.getNombre());
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioDouble);
        detalle.setSubtotal(cantidad * precioDouble);

        listaCarrito.add(detalle);
        calcularTotal();
        limpiarFormularioProducto();
    }

    @FXML
    private void eliminarProducto() {
        DetalleVenta seleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Seleccione un producto del carrito para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        listaCarrito.remove(seleccionado);
        calcularTotal();
    }

    private void calcularTotal() {
        double total = listaCarrito.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
        lblTotal.setText(String.format("$%.2f", total));
    }

    @FXML
    private void cobrar() {
        if (listaCarrito.isEmpty()) {
            mostrarAlerta("Carrito Vacío", "No hay productos en el carrito de venta.", Alert.AlertType.WARNING);
            return;
        }

        double total = listaCarrito.stream().mapToDouble(DetalleVenta::getSubtotal).sum();

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Cobro");
        confirmacion.setHeaderText(String.format("Total a pagar: $%.2f", total));
        confirmacion.setContentText("¿Desea procesar la venta?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            int idUsuario = (usuarioActual != null) ? usuarioActual.getIdUsuario() : 1;

            int idVentaGenerada = ventaDAO.registrarVenta(idUsuario, total, listaCarrito);

            if (idVentaGenerada > 0) {
                for (DetalleVenta item : listaCarrito) {
                    productoDAO.descontarStock(item.getIdProducto(), item.getCantidad());
                }

                mostrarAlerta("Venta Exitosa", "Venta Nº " + idVentaGenerada + " registrada correctamente.", Alert.AlertType.INFORMATION);

                nuevaVenta();
                cargarHistorialCajero();
            } else {
                mostrarAlerta("Error", "No se pudo registrar la venta en la base de datos.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void nuevaVenta() {
        listaCarrito.clear();
        lblTotal.setText("$0.00");
        limpiarFormularioProducto();
    }

    private void cargarHistorialCajero() {
        if (usuarioActual != null) {
            listaHistorialVentas.clear();
            listaHistorialVentas.addAll(ventaDAO.obtenerVentasPorUsuario(usuarioActual.getIdUsuario()));
        }
    }

    private void limpiarFormularioProducto() {
        productoSeleccionado = null;
        txtBuscarProducto.clear();
        txtProducto.clear();
        txtStock.clear();
        txtPrecio.clear();
        configurarSpinner(1);
    }
    @FXML
    private void cerrarSesion(ActionEvent event) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cerrar Sesión");
        confirmacion.setHeaderText("¿Está seguro de que desea salir?");
        confirmacion.setContentText("Se cerrará la sesión actual del cajero.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // Intentar cargar la vista del login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/papeleria_proyecto/view/login.fxml"));
                Parent root = loader.load();

                // Obtener el Stage usando un control FXML existente (por ejemplo, tblCarrito o txtProducto)
                Stage stage = (Stage) tblCarrito.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Iniciar Sesión - Papelería");
                stage.centerOnScreen();
                stage.show();

            } catch (Exception e) {
                System.err.println("Error al cerrar sesión: " + e.getMessage());
                e.printStackTrace();

                // Alerta si la ruta FXML está mal escrita o no existe
                mostrarAlerta("Error de Navegación",
                        "No se pudo cargar la pantalla de login. Verifique la ruta del archivo FXML.\nDetalle: " + e.getMessage(),
                        Alert.AlertType.ERROR);
            }
        }
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}