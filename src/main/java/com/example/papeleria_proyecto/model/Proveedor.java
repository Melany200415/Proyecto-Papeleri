package com.example.papeleria_proyecto.model;

public class Proveedor extends Persona {

    private int idProveedor;
    private String direccion;

    // Constructor para insertar
    public Proveedor(
            String nombre,
            String telefono,
            String correo,
            String direccion
    ) {

        super(nombre, telefono, correo);

        this.direccion = direccion;
    }

    // Constructor para listar
    public Proveedor(
            int idProveedor,
            String nombre,
            String telefono,
            String correo,
            String direccion
    ) {

        super(nombre, telefono, correo);

        this.idProveedor = idProveedor;
        this.direccion = direccion;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return getNombre();
    }
}