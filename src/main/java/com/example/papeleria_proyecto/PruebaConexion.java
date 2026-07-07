package com.example.papeleria_proyecto;

import com.example.papeleria_proyecto.conexion.Conexion;
import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {
        try {
            Connection con = Conexion.getConexion();

            if (con != null) {
                System.out.println("✅ Conexión exitosa");
                con.close();
            } else {
                System.out.println("❌ La conexión es nula");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}