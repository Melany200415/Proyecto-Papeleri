package com.example.papeleria_proyecto.dao;


import com.example.papeleria_proyecto.db.Conexion;
import com.example.papeleria_proyecto.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, usuario, contrasena, telefono, correo, estado, id_rol) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            ps.setInt(7, u.getEstado());
            ps.setInt(8, u.getIdRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, usuario=?, contrasena=?, telefono=?, "
                + "correo=?, estado=?, id_rol=? WHERE id_usuario=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            ps.setInt(7, u.getEstado());
            ps.setInt(8, u.getIdRol());
            ps.setInt(9, u.getIdUsuario());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // Baja lógica (no borra el registro, solo cambia el estado a 0)
    public boolean eliminar(int idUsuario) {
        String sql = "UPDATE usuarios SET estado = 0 WHERE id_usuario = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Usuario> listarTodos() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM usuarios";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getInt("estado"),
                        rs.getInt("id_rol")
                ));
            }
            System.out.println(lista.size());

        }catch(SQLException e){
            e.printStackTrace();
        }

        return lista;
    }

    public Usuario buscarPorId(int idUsuario) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("usuario"),
                            rs.getString("contrasena"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getInt("estado"),
                            rs.getInt("id_rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }
}
