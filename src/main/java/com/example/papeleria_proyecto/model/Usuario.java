package com.example.papeleria_proyecto.model;

public class Usuario extends Persona {

    private int idUsuario;
    private String apellido;
    private String usuario;
    private String contrasena;
    private int estado;
    private int idRol;

    public Usuario(
            int idUsuario,
            String nombre,
            String apellido,
            String usuario,
            String contrasena,
            String telefono,
            String correo,
            int estado,
            int idRol
    ) {

        super(nombre, telefono, correo);

        this.idUsuario = idUsuario;
        this.apellido = apellido;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
        this.idRol = idRol;
    }

    public Usuario(
            String nombre,
            String apellido,
            String usuario,
            String contrasena,
            String telefono,
            String correo,
            int estado,
            int idRol
    ) {

        this(
                0,
                nombre,
                apellido,
                usuario,
                contrasena,
                telefono,
                correo,
                estado,
                idRol
        );
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getEstadoTexto() {
        return estado == 1 ? "Activo" : "Inactivo";
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    @Override
    public String toString() {
        return getNombre() + " " + apellido;
    }
}