package com.example.papeleria_proyecto.dao;


import com.example.papeleria_proyecto.db.Conexion;
import com.example.papeleria_proyecto.model.Venta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class VentaDAO {

    // El admin normalmente solo consulta ventas, pero se deja el insertar por si lo necesitas
    public boolean insertar(Venta v) {
        String sql = "INSERT INTO ventas (subtotal, iva, total, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, v.getSubtotal());
            ps.setBigDecimal(2, v.getIva());
            ps.setBigDecimal(3, v.getTotal());
            ps.setInt(4, v.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Venta> listarTodas() {
        ObservableList<Venta> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM ventas ORDER BY fecha DESC";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp fecha = rs.getTimestamp("fecha");
                lista.add(new Venta(
                        rs.getInt("id_venta"),
                        fecha != null ? fecha.toLocalDateTime() : null,
                        rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("iva"),
                        rs.getBigDecimal("total"),
                        rs.getInt("id_usuario")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }

        return lista;
    }

    public Venta buscarPorId(int idVenta) {
        String sql = "SELECT * FROM ventas WHERE id_venta = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp fecha = rs.getTimestamp("fecha");
                    return new Venta(
                            rs.getInt("id_venta"),
                            fecha != null ? fecha.toLocalDateTime() : null,
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("iva"),
                            rs.getBigDecimal("total"),
                            rs.getInt("id_usuario")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar venta: " + e.getMessage());
        }
        return null;
    }
}
