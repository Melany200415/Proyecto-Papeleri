package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    private Connection conexion;

    public LoginDAO() {
        conexion = Conexion.getConexion();
    }

    public ResultSet iniciarSesion(String usuario, String password) {

        try {

            String sql = """
                    SELECT
                        u.id_usuario,
                        u.usuario,
                        r.nombre AS rol
                    FROM usuarios u
                    INNER JOIN roles r
                        ON u.id_rol = r.id_rol
                    WHERE u.usuario = ?
                      AND u.contrasena = ?
                      AND u.estado = 1
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, password);

            return ps.executeQuery();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }

    }

}