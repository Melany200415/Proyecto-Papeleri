package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.db.Conexion;
import com.example.papeleria_proyecto.model.Proveedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProveedorDAO implements ICRUD<Proveedor> {

    @Override
    public boolean insertar(Proveedor p) {

        String sql = """
                INSERT INTO proveedores
                (nombre, telefono, correo, direccion)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTelefono());
            ps.setString(3, p.getCorreo());
            ps.setString(4, p.getDireccion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al insertar proveedor: "
                    + e.getMessage());

            return false;
        }
    }

    @Override
    public boolean actualizar(Proveedor p) {

        String sql = """
                UPDATE proveedores
                SET nombre=?,
                    telefono=?,
                    correo=?,
                    direccion=?
                WHERE id_proveedor=?
                """;

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTelefono());
            ps.setString(3, p.getCorreo());
            ps.setString(4, p.getDireccion());
            ps.setInt(5, p.getIdProveedor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al actualizar proveedor: "
                    + e.getMessage());

            return false;
        }
    }

    @Override
    public boolean eliminar(int idProveedor) {

        String sql = "DELETE FROM proveedores WHERE id_proveedor=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println("Error al eliminar proveedor: "
                    + e.getMessage());

            return false;
        }
    }

    @Override
    public ObservableList<Proveedor> listarTodos() {

        ObservableList<Proveedor> lista =
                FXCollections.observableArrayList();

        String sql = "SELECT * FROM proveedores";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Proveedor(
                        rs.getInt("id_proveedor"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion")
                ));
            }

        } catch (SQLException e) {

            System.err.println("Error al listar proveedores: "
                    + e.getMessage());
        }

        return lista;
    }

    // Método propio del DAO
    public Proveedor buscarPorId(int idProveedor) {

        String sql =
                "SELECT * FROM proveedores WHERE id_proveedor=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Proveedor(
                            rs.getInt("id_proveedor"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getString("direccion")
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println("Error al buscar proveedor: "
                    + e.getMessage());
        }

        return null;
    }
}