package com.example.papeleria_proyecto.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CajeroController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtProducto;

    @FXML
    private TextField txtPrecio;

    @FXML
    private Spinner<Integer> spCantidad;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<ObservableList<String>> tblCarrito;

    @FXML
    private TableColumn<ObservableList<String>, String> colCodigo;

    @FXML
    private TableColumn<ObservableList<String>, String> colProducto;

    @FXML
    private TableColumn<ObservableList<String>, String> colCantidad;

    @FXML
    private TableColumn<ObservableList<String>, String> colPrecio;

    @FXML
    private TableColumn<ObservableList<String>, String> colSubtotal;

    @FXML
    private TableView<ObservableList<String>> tblVentas;

    @FXML
    private TableColumn<ObservableList<String>, String> colVenta;

    @FXML
    private TableColumn<ObservableList<String>, String> colFecha;

    @FXML
    private TableColumn<ObservableList<String>, String> colCliente;

    @FXML
    private TableColumn<ObservableList<String>, String> colTotalVenta;

    @FXML
    private TableColumn<ObservableList<String>, String> colEstado;

    private final ObservableList<ObservableList<String>> carrito =
            FXCollections.observableArrayList();

    private double total = 0;

    @FXML
    public void initialize() {

        spCantidad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1)
        );

        configurarTablaCarrito();
        configurarTablaVentas();

        lblTotal.setText("$0.00");
    }

    private void configurarTablaCarrito() {

        colCodigo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(0)));

        colProducto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(1)));

        colCantidad.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(2)));

        colPrecio.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(3)));

        colSubtotal.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(4)));

        tblCarrito.setItems(carrito);
    }

    private void configurarTablaVentas() {

        colVenta.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(0)));

        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(1)));

        colCliente.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(2)));

        colTotalVenta.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(3)));

        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().get(4)));
    }

    @FXML
    private void agregarProducto() {

        String codigo = txtCodigo.getText();
        String producto = txtProducto.getText();

        if(codigo.isEmpty() || producto.isEmpty()){
            mostrarMensaje("Ingrese un producto.");
            return;
        }

        int cantidad = spCantidad.getValue();
        double precio = Double.parseDouble(txtPrecio.getText());

        double subtotal = cantidad * precio;

        ObservableList<String> fila =
                FXCollections.observableArrayList();

        fila.add(codigo);
        fila.add(producto);
        fila.add(String.valueOf(cantidad));
        fila.add(String.format("%.2f", precio));
        fila.add(String.format("%.2f", subtotal));

        carrito.add(fila);

        total += subtotal;

        lblTotal.setText("$" + String.format("%.2f", total));

        limpiarCampos();
    }

    @FXML
    private void eliminarProducto() {

        ObservableList<String> fila =
                tblCarrito.getSelectionModel().getSelectedItem();

        if(fila == null){
            mostrarMensaje("Seleccione un producto.");
            return;
        }

        total -= Double.parseDouble(fila.get(4));

        carrito.remove(fila);

        lblTotal.setText("$" + String.format("%.2f", total));
    }

    @FXML
    private void nuevaVenta() {

        carrito.clear();

        total = 0;

        lblTotal.setText("$0.00");

        limpiarCampos();
    }

    @FXML
    private void cobrar() {

        if(carrito.isEmpty()){
            mostrarMensaje("No existen productos en la venta.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Venta");

        alert.setHeaderText(null);

        alert.setContentText("Venta registrada correctamente.");

        alert.showAndWait();

        nuevaVenta();
    }

    private void limpiarCampos(){

        txtCodigo.clear();
        txtProducto.clear();
        txtPrecio.clear();

        spCantidad.getValueFactory().setValue(1);
    }

    private void mostrarMensaje(String mensaje){

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}