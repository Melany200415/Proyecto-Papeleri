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
        sb.append("CODIGO\t\tNOMBRE\t\t\tSTOCK\tESTADO\n");
        sb.append("-----------------------------------------------------------\n");

        for (Producto p : productoDAO.listarTodos()) {
            if (p.getStock() <= 5) {
                sb.append(p.getCodigo()).append("\t\t")
                        .append(p.getNombre()).append("\t\t")
                        .append(p.getStock()).append("\t\t")
                        .append(p.getEstado() == 1 ? "Activo" : "Inactivo").append("\n");
            }
        }

        txtAreaReporte.setText(sb.toString());
    }

    @FXML
    private void generarReporteUsuarios() {
        lblTituloReporte.setText("Reporte: Resumen de Usuarios Registrados");
        StringBuilder sb = new StringBuilder();
        sb.append("ID\tUSUARIO\t\tNOMBRE COMPLETO\t\tROL\tESTADO\n");
        sb.append("-----------------------------------------------------------\n");

        for (Usuario u : usuarioDAO.listarTodos()) {
            sb.append(u.getIdUsuario()).append("\t")
                    .append(u.getUsuario()).append("\t\t")
                    .append(u.getNombre()).append(" ").append(u.getApellido()).append("\t\t")
                    .append(u.getIdRol()).append("\t")
                    .append(u.getEstadoTexto()).append("\n");
        }

        txtAreaReporte.setText(sb.toString());
    }

    @FXML
    private void generarReporteTotalVentas() {
        lblTituloReporte.setText("Reporte: Métricas Totales de Ventas");
        BigDecimal acumTotal = BigDecimal.ZERO;
        int cantidad = 0;

        for (Venta v : ventaDAO.listarTodas()) {
            acumTotal = acumTotal.add(v.getTotal());
            cantidad++;
        }

        String sb = "Resumen de Ingresos:\n" +
                "-----------------------------------------\n" +
                "Número total de transacciones: " + cantidad + "\n" +
                "Monto total acumulado: $" + acumTotal + "\n";

        txtAreaReporte.setText(sb);
    }
}