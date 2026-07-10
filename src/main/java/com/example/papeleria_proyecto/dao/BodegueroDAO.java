package com.example.papeleria_proyecto.dao;

import com.example.papeleria_proyecto.conexion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BodegueroDAO {

    private final Connection conexion;

    public BodegueroDAO() {
        conexion = Conexion.getConexion();
    }


    public ResultSet listarProductos() {

        try {

            String sql = """
                    SELECT
                        p.id_producto,
                        p.codigo,
                        p.nombre,
                        p.descripcion,
                        p.precio,
                        p.stock,
                        p.estado,
                        c.nombre AS categoria
                    FROM productos p
                    INNER JOIN categorias c
                        ON p.id_categoria = c.id_categoria
                    ORDER BY p.nombre
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            return ps.executeQuery();

        } catch (SQLException e) {

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

            String sql = """
                    INSERT INTO productos
                    (codigo,nombre,descripcion,precio,stock,estado,id_categoria)
                    VALUES(?,?,?,?,?,?,?)
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            ps.setInt(6, estado);
            ps.setInt(7, categoria);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean editarProducto(int id,
                                  String codigo,
                                  String nombre,
                                  String descripcion,
                                  double precio,
                                  int stock,
                                  int estado,
                                  int categoria) {

        try {

            String sql = """
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

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, codigo);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            ps.setDouble(4, precio);
            ps.setInt(5, stock);
            ps.setInt(6, estado);
            ps.setInt(7, categoria);
            ps.setInt(8, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    public boolean eliminarProducto(int id) {

        try {

            String sql = "DELETE FROM productos WHERE id_producto=?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }


    public ResultSet listarCategorias() {

        try {

            String sql = """
                    SELECT
                        id_categoria,
                        nombre,
                        descripcion
                    FROM categorias
                    ORDER BY nombre
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            return ps.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }

    }


    public ResultSet listarDetalleVentas() {

        try {

            String sql = """
                    SELECT
                        v.id_venta,
                        v.fecha,
                        p.codigo,
                        p.nombre,
                        d.cantidad,
                        d.precio_unitario,
                        d.subtotal
                    FROM ventas v
                    INNER JOIN detalle_venta d
                        ON v.id_venta = d.id_venta
                    INNER JOIN productos p
                        ON p.id_producto = d.id_producto
                    ORDER BY v.fecha DESC
                    """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            return ps.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();
            return null;

        }

    }
    public int obtenerIdCategoria(String nombreCategoria) {

        try {

            String sql = "SELECT id_categoria FROM categorias WHERE nombre = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreCategoria);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_categoria");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return -1;

    }

}