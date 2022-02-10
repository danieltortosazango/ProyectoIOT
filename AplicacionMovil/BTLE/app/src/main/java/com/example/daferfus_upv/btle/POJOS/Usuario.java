package com.example.daferfus_upv.btle.POJOS;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String apellidos;
    private String contrasenya;
    private String desviacion;
    private String Ciudad;

    public Usuario(String idUsuario, String nombre, String apellidos, String contrasenya, String desviacion, String Ciudad) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.contrasenya = contrasenya;
        this.desviacion = desviacion;
        this.Ciudad = Ciudad;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getDesviacion() {
        return desviacion;
    }

    public void setDesviacion(String desviacion) {
        this.desviacion = desviacion;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        this.Ciudad = ciudad;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", desviacion='" + desviacion + '\'' +
                ", ciudad='" + Ciudad + '\'' +
                '}';
    }
}
