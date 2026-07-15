package com.example.papeleria_proyecto.objetos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venta {

    private int idVenta;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private int idUsuario;

    public Venta() {
    }

    public Venta(int idVenta, LocalDateTime fecha, BigDecimal subtotal, BigDecimal iva,
                  BigDecimal total, int idUsuario) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.idUsuario = idUsuario;
    }

    public Venta(BigDecimal subtotal, BigDecimal iva, BigDecimal total, int idUsuario) {
        this(0, null, subtotal, iva, total, idUsuario);
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
