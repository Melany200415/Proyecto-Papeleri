package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.db.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CajeroDAO {

    private final Connection conexion;

    public CajeroDAO() {
        conexion = Conexion.getConexion();
    }

    // Buscar producto por código
    public ResultSet buscarProducto(String codigo) {

        try {

            String sql = """
                    SELECT id_producto,
                           codigo,
                           nombre,
                           precio,
                           stock
                    FROM productos
                    WHERE codigo = ?
                    AND estado='Activo'
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, codigo);

            return ps.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Registrar venta
    public int registrarVenta(double total, int idUsuario) {

        try {

            String sql = """
                    INSERT INTO ventas(fecha,total,id_usuario)
                    VALUES(NOW(),?,?)
                    """;

            PreparedStatement ps = conexion.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            ps.setDouble(1, total);
            ps.setInt(2, idUsuario);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){

                return rs.getInt(1);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return -1;
    }

    // Registrar detalle
    public boolean registrarDetalleVenta(int idVenta,
                                         int idProducto,
                                         int cantidad,
                                         double subtotal){

        try{

            String sql="""
                    INSERT INTO detalle_venta
                    (id_venta,id_producto,cantidad,subtotal)
                    VALUES(?,?,?,?)
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setInt(1,idVenta);
            ps.setInt(2,idProducto);
            ps.setInt(3,cantidad);
            ps.setDouble(4,subtotal);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;
    }

    // Descontar stock
    public boolean actualizarStock(int idProducto,int cantidad){

        try{

            String sql="""
                    UPDATE productos
                    SET stock=stock-?
                    WHERE id_producto=?
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setInt(1,cantidad);
            ps.setInt(2,idProducto);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;
    }

    // Historial de ventas
    public ResultSet listarVentas(){

        try{

            String sql="""
                    SELECT
                    id_venta,
                    fecha,
                    total
                    FROM ventas
                    ORDER BY fecha DESC
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            return ps.executeQuery();

        }catch(Exception e){

            e.printStackTrace();

        }

        return null;
    }
    public int obtenerIdProducto(String codigo){

        try{

            String sql="""
                SELECT id_producto
                FROM productos
                WHERE codigo=?
                """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setString(1,codigo);

            ResultSet rs=ps.executeQuery();

            if(rs.next()){

                return rs.getInt("id_producto");

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return -1;
    }
    public boolean hayStock(String codigo,int cantidad){

        try{

            String sql="""
                SELECT stock
                FROM productos
                WHERE codigo=?
                """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setString(1,codigo);

            ResultSet rs=ps.executeQuery();

            if(rs.next()){

                return rs.getInt("stock")>=cantidad;

            }

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;
    }
}