package com.example.papeleria_proyecto.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://hayabusa.proxy.rlwy.net:41041/";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "kkpPlTfoygJzQYNzhfStiDmKqpBkSbLG";

    public static Connection getConexion() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("Conectado a la base de datos");
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}