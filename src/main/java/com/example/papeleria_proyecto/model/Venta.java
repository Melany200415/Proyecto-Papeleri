package com.example.papeleria_proyecto.model;

import java.time.LocalDateTime;

public class Venta {
    private int idVenta;
    private int idUsuario;
    private LocalDateTime fecha;
    private double total;
    private String estado;

    public Venta() {}

    public Venta(int idVenta, int idUsuario, LocalDateTime fecha, double total, String estado) {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}