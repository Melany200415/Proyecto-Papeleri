package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.VentaDAO;
import com.example.papeleria_proyecto.model.Venta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentasController {

    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, Integer> colId, colUsuario;
    @FXML private TableColumn<Venta, LocalDateTime> colFecha;
    @FXML private TableColumn<Venta, BigDecimal> colSubtotal, colIva, colTotal;

    private final VentaDAO ventaDAO = new VentaDAO();
    private final ObservableList<Venta> listaVentas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdVenta()));
        colFecha.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getFecha()));
        colSubtotal.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getSubtotal()));
        colIva.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIva()));
        colTotal.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTotal()));
        colUsuario.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getIdUsuario()));

        tablaVentas.setItems(listaVentas);
        cargarVentas();
    }

    @FXML
    public void cargarVentas() {
        listaVentas.setAll(ventaDAO.listarTodas());
    }
}