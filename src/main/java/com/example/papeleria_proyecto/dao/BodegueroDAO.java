package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BodegueroDAO {

    private final Connection conexion;

    public BodegueroDAO() {
        conexion = Conexion.getConexion();
    }

    public ResultSet listarProductos() {

        try {

            String sql = """
                    SELECT p.id_producto,
                           p.codigo,
                           p.nombre,
                           p.descripcion,
                           p.precio,
                           p.stock,
                           p.estado,
                           c.nombre AS categoria
                    FROM productos p
                    INNER JOIN categorias c
                    ON p.id_categoria=c.id_categoria
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            return ps.executeQuery();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }

    }

    public boolean insertarProducto(String codigo,
                                    String nombre,
                                    String descripcion,
                                    double precio,
                                    int stock,
                                    int estado,
                                    int categoria) {

        try {

            String sql="""
                    INSERT INTO productos
                    (codigo,nombre,descripcion,precio,stock,estado,id_categoria)
                    VALUES(?,?,?,?,?,?,?)
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setString(1,codigo);
            ps.setString(2,nombre);
            ps.setString(3,descripcion);
            ps.setDouble(4,precio);
            ps.setInt(5,stock);
            ps.setInt(6,estado);
            ps.setInt(7,categoria);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    public boolean editarProducto(int id,
                                  String codigo,
                                  String nombre,
                                  String descripcion,
                                  double precio,
                                  int stock,
                                  int estado,
                                  int categoria){

        try{

            String sql="""
                    UPDATE productos
                    SET codigo=?,
                        nombre=?,
                        descripcion=?,
                        precio=?,
                        stock=?,
                        estado=?,
                        id_categoria=?
                    WHERE id_producto=?
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setString(1,codigo);
            ps.setString(2,nombre);
            ps.setString(3,descripcion);
            ps.setDouble(4,precio);
            ps.setInt(5,stock);
            ps.setInt(6,estado);
            ps.setInt(7,categoria);
            ps.setInt(8,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    public boolean eliminarProducto(int id){

        try{

            String sql="DELETE FROM productos WHERE id_producto=?";

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    public ResultSet listarCategorias(){

        try{

            String sql="SELECT * FROM categorias";

            PreparedStatement ps=conexion.prepareStatement(sql);

            return ps.executeQuery();

        }catch(Exception e){

            e.printStackTrace();

            return null;

        }

    }

    public ResultSet listarDetalleVenta(){

        try{

            String sql="""
                    SELECT d.id_detalle,
                           d.id_venta,
                           p.nombre,
                           d.cantidad,
                           d.precio_unitario,
                           d.subtotal
                    FROM detalle_venta d
                    INNER JOIN productos p
                    ON d.id_producto=p.id_producto
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            return ps.executeQuery();

        }catch(Exception e){

            e.printStackTrace();

            return null;

        }

    }

    public ResultSet listarMovimientos(){

        try{

            String sql="""
                    SELECT *
                    FROM movimientos_inventario
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            return ps.executeQuery();

        }catch(Exception e){

            e.printStackTrace();

            return null;

        }

    }

    public boolean insertarMovimiento(int producto,
                                      int usuario,
                                      String tipo,
                                      int cantidad,
                                      String motivo){

        try{

            String sql="""
                    INSERT INTO movimientos_inventario
                    (id_producto,id_usuario,tipo,cantidad,motivo,fecha)
                    VALUES(?,?,?,?,?,NOW())
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setInt(1,producto);
            ps.setInt(2,usuario);
            ps.setString(3,tipo);
            ps.setInt(4,cantidad);
            ps.setString(5,motivo);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    public boolean editarMovimiento(int id,
                                    String tipo,
                                    int cantidad,
                                    String motivo){

        try{

            String sql="""
                    UPDATE movimientos_inventario
                    SET tipo=?,
                        cantidad=?,
                        motivo=?
                    WHERE id_movimiento=?
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setString(1,tipo);
            ps.setInt(2,cantidad);
            ps.setString(3,motivo);
            ps.setInt(4,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

    public boolean eliminarMovimiento(int id){

        try{

            String sql="""
                    DELETE FROM movimientos_inventario
                    WHERE id_movimiento=?
                    """;

            PreparedStatement ps=conexion.prepareStatement(sql);

            ps.setInt(1,id);

            return ps.executeUpdate()>0;

        }catch(Exception e){

            e.printStackTrace();

        }

        return false;

    }

}