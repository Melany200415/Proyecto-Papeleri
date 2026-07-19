package com.example.papeleria_proyecto.model;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String usuario;
    private String contrasena;
    private String telefono;
    private String correo;
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

        this.idUsuario = idUsuario;

        this.nombre = nombre;

        this.apellido = apellido;

        this.usuario = usuario;

        this.contrasena = contrasena;

        this.telefono = telefono;

        this.correo = correo;

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


    public String getNombre() {

        return nombre;

    }


    public void setNombre(String nombre) {

        this.nombre = nombre;

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


    public String getTelefono() {

        return telefono;

    }


    public void setTelefono(String telefono) {

        this.telefono = telefono;

    }


    public String getCorreo() {

        return correo;

    }


    public void setCorreo(String correo) {

        this.correo = correo;

    }


    public int getEstado() {

        return estado;

    }


    public void setEstado(int estado) {

        this.estado = estado;

    }


    public String getEstadoTexto() {

        return estado == 1

                ? "Activo"

                : "Inactivo";

    }


    public int getIdRol() {

        return idRol;

    }


    public void setIdRol(int idRol) {

        this.idRol = idRol;

    }


    @Override
    public String toString() {

        return nombre + " " + apellido;

    }

}