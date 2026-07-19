package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.model.Proveedor;
import com.example.papeleria_proyecto.db.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public List<Proveedor> listarTodos() {

        List<Proveedor> lista = new ArrayList<>();

        String sql = """
                SELECT id_proveedor,
                       nombre,
                       telefono,
                       correo,
                       direccion
                FROM proveedores
                ORDER BY id_proveedor DESC
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Proveedor proveedor = new Proveedor();

                proveedor.setIdProveedor(
                        rs.getInt("id_proveedor")
                );

                proveedor.setNombre(
                        rs.getString("nombre")
                );

                proveedor.setTelefono(
                        rs.getString("telefono")
                );

                proveedor.setCorreo(
                        rs.getString("correo")
                );

                proveedor.setDireccion(
                        rs.getString("direccion")
                );

                lista.add(proveedor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean insertar(Proveedor proveedor) {

        String sql = """
                INSERT INTO proveedores
                (nombre, telefono, correo, direccion)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Proveedor proveedor) {

        String sql = """
                UPDATE proveedores
                SET nombre = ?,
                    telefono = ?,
                    correo = ?,
                    direccion = ?
                WHERE id_proveedor = ?
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTelefono());
            ps.setString(3, proveedor.getCorreo());
            ps.setString(4, proveedor.getDireccion());
            ps.setInt(5, proveedor.getIdProveedor());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idProveedor) {

        String sql = """
                DELETE FROM proveedores
                WHERE id_proveedor = ?
                """;

        try (Connection conexion = Conexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}