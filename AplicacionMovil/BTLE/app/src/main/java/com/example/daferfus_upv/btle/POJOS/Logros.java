package com.example.daferfus_upv.btle.POJOS;

public class Logros {

    public int idLogro;
    public int dificultadCompletar;
    public String nombreLogro;
    public String imagenLogro;
    public String tipo;

    public Logros() {
    }

    public Logros(int idLogro, int dificultadCompletar, String nombreLogro, String imagenLogro, String tipo) {
        this.idLogro = idLogro;
        this.dificultadCompletar = dificultadCompletar;
        this.nombreLogro = nombreLogro;
        this.imagenLogro = imagenLogro;
        this.tipo = tipo;
    }

    public int getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(int idLogro) {
        this.idLogro = idLogro;
    }

    public int getDificultadCompletar() {
        return dificultadCompletar;
    }

    public void setDificultadCompletar(int dificultadCompletar) {
        this.dificultadCompletar = dificultadCompletar;
    }

    public String getNombreLogro() {
        return nombreLogro;
    }

    public void setNombreLogro(String nombreLogro) {
        this.nombreLogro = nombreLogro;
    }

    public String getImagenLogro() {
        return imagenLogro;
    }

    public void setImagenLogro(String imagenLogro) {
        this.imagenLogro = imagenLogro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Logros{" +
                "idLogro=" + idLogro +
                ", dificultadCompletar=" + dificultadCompletar +
                ", nombreLogro='" + nombreLogro + '\'' +
                ", imagenLogro='" + imagenLogro + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
