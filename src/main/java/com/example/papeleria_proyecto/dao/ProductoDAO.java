package com.example.papeleria_proyecto.dao;


import com.example.papeleria_proyecto.conexion.Conexion;
import com.example.papeleria_proyecto.objetos.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {

    public boolean insertar(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, descripcion, precio, stock, estado, id_categoria) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getEstado());
            ps.setInt(7, p.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, descripcion=?, precio=?, stock=?, "
                + "estado=?, id_categoria=? WHERE id_producto=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getEstado());
            ps.setInt(7, p.getIdCategoria());
            ps.setInt(8, p.getIdProducto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idProducto) {
        String sql = "UPDATE productos SET estado = 0 WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Producto> listarTodos() {
        ObservableList<Producto> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM productos";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        rs.getInt("estado"),
                        rs.getInt("id_categoria")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }

        return lista;
    }

    public Producto buscarPorId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock"),
                            rs.getInt("estado"),
                            rs.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }
}
