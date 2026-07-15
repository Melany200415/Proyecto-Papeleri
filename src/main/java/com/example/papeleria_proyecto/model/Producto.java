package com.example.papeleria_proyecto.model;

import java.math.BigDecimal;

public class Producto {

    private int idProducto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private int estado;
    private int idCategoria;

    public Producto() {
    }

    public Producto(int idProducto, String codigo, String nombre, String descripcion,
                     BigDecimal precio, int stock, int estado, int idCategoria) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
        this.idCategoria = idCategoria;
    }

    public Producto(String codigo, String nombre, String descripcion,
                     BigDecimal precio, int stock, int estado, int idCategoria) {
        this(0, codigo, nombre, descripcion, precio, stock, estado, idCategoria);
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
