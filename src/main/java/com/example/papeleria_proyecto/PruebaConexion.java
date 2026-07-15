package com.example.papeleria_proyecto;

import com.example.papeleria_proyecto.db.Conexion;
import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection con = Conexion.getConexion();

        if (con != null) {
            System.out.println("La conexión fue exitosa.");
        } else {
            System.out.println("No se pudo conectar.");
        }
    }
}