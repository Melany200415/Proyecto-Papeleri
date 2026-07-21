package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.db.Conexion;
import com.example.papeleria_proyecto.model.DetalleVenta;
import com.example.papeleria_proyecto.model.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {


    public int registrarVenta(int idUsuario, double total, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (id_usuario, fecha, total) VALUES (?, NOW(), ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        ResultSet rsKeys = null;

        try {
            con = Conexion.getConexion();
            // Iniciar transacción explícita
            con.setAutoCommit(false);

            // 1. Insertar Cabecera de la Venta
            psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, idUsuario);
            psVenta.setDouble(2, total);

            int filasAfectadas = psVenta.executeUpdate();

            if (filasAfectadas == 0) {
                con.rollback();
                return -1;
            }

            // Obtener el ID autogenerado de la venta recién insertada
            rsKeys = psVenta.getGeneratedKeys();
            int idVentaGenerado = -1;
            if (rsKeys.next()) {
                idVentaGenerado = rsKeys.getInt(1);
            } else {
                con.rollback();
                return -1;
            }

            // 2. Insertar los Detalles de la Venta en Lote (Batch)
            psDetalle = con.prepareStatement(sqlDetalle);
            for (DetalleVenta item : detalles) {
                psDetalle.setInt(1, idVentaGenerado);
                psDetalle.setInt(2, item.getIdProducto());
                psDetalle.setInt(3, item.getCantidad());
                psDetalle.setDouble(4, item.getPrecioUnitario());
                psDetalle.setDouble(5, item.getSubtotal());
                psDetalle.addBatch();
            }

            psDetalle.executeBatch();

            // Confirmar transacción si todo salió bien
            con.commit();
            return idVentaGenerado;

        } catch (SQLException e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return -1;
        } finally {
            try {
                if (rsKeys != null) rsKeys.close();
                if (psVenta != null) psVenta.close();
                if (psDetalle != null) psDetalle.close();
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene el historial de ventas realizadas únicamente por un usuario específico.
     */
    public List<Venta> obtenerVentasPorUsuario(int idUsuario) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE id_usuario = ? ORDER BY fecha DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Venta v = new Venta();
                    v.setIdVenta(rs.getInt("id_venta"));
                    v.setIdUsuario(rs.getInt("id_usuario"));

                    if (rs.getTimestamp("fecha") != null) {
                        v.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    }

                    v.setTotal(rs.getDouble("total"));
                    v.setEstado("Completado"); // Estado por defecto para la interfaz
                    lista.add(v);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener ventas del usuario: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene todas las ventas registradas en la base de datos (Para Reportes/Admin).
     */
    public List<Venta> listarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("id_venta"));
                v.setIdUsuario(rs.getInt("id_usuario"));

                if (rs.getTimestamp("fecha") != null) {
                    v.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                }

                v.setTotal(rs.getDouble("total"));
                v.setEstado("Completado"); // Estado por defecto para la interfaz

                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar todas las ventas: " + e.getMessage());
        }

        return lista;
    }
}