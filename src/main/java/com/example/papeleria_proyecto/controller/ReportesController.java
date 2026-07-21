package com.example.papeleria_proyecto.controller;

import com.example.papeleria_proyecto.dao.ProductoDAO;
import com.example.papeleria_proyecto.dao.UsuarioDAO;
import com.example.papeleria_proyecto.dao.VentaDAO;
import com.example.papeleria_proyecto.model.Producto;
import com.example.papeleria_proyecto.model.Usuario;
import com.example.papeleria_proyecto.model.Venta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.math.BigDecimal;
import java.util.List;

public class ReportesController {

    @FXML private Label lblTituloReporte;
    @FXML private TextArea txtAreaReporte;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    @FXML
    private void generarReporteStockBajo() {
        lblTituloReporte.setText("Reporte: Productos con Stock Bajo (<= 5 unidades)");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-12s %-25s %-10s %-10s\n", "CÓDIGO", "NOMBRE", "STOCK", "ESTADO"));
        sb.append("-------------------------------------------------------------------\n");

        List<Producto> productos = productoDAO.listarTodos();
        int contador = 0;

        for (Producto p : productos) {
            if (p.getStock() <= 5) {
                sb.append(String.format("%-12s %-25s %-10d %-10s\n",
                        p.getCodigo(),
                        p.getNombre(),
                        p.getStock(),
                        p.getEstado() == 1 ? "Activo" : "Inactivo"));
                contador++;
            }
        }

        if (contador == 0) {
            sb.append("\nNo hay productos con stock igual o inferior a 5 unidades.");
        }

        txtAreaReporte.setText(sb.toString());
    }

    @FXML
    private void generarReporteUsuarios() {
        lblTituloReporte.setText("Reporte: Resumen de Usuarios Registrados");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-15s %-25s %-8s %-10s\n", "ID", "USUARIO", "NOMBRE COMPLETO", "ROL", "ESTADO"));
        sb.append("-------------------------------------------------------------------\n");

        for (Usuario u : usuarioDAO.listarTodos()) {
            String nombreCompleto = u.getNombre() + " " + u.getApellido();
            sb.append(String.format("%-5d %-15s %-25s %-8d %-10s\n",
                    u.getIdUsuario(),
                    u.getUsuario(),
                    nombreCompleto,
                    u.getIdRol(),
                    u.getEstadoTexto()));
        }

        txtAreaReporte.setText(sb.toString());
    }

    @FXML
    private void generarReporteTotalVentas() {
        lblTituloReporte.setText("Reporte: Métricas Totales de Ventas");
        BigDecimal acumTotal = BigDecimal.ZERO;
        int cantidad = 0;

        List<Venta> ventas = ventaDAO.listarTodas();

        for (Venta v : ventas) {
            // Conversión segura sin importar si v.getTotal() retorna double o BigDecimal
            BigDecimal totalVenta = BigDecimal.valueOf(v.getTotal());
            acumTotal = acumTotal.add(totalVenta);
            cantidad++;
        }

        String reporte = "=========================================\n" +
                "          RESUMEN DE INGRESOS           \n" +
                "=========================================\n" +
                "Número total de transacciones: " + cantidad + "\n" +
                "Monto total acumulado:        $" + String.format("%.2f", acumTotal) + "\n" +
                "=========================================\n";

        txtAreaReporte.setText(reporte);
    }
}