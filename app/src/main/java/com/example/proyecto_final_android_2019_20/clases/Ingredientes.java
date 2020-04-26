package com.example.proyecto_final_android_2019_20.clases;

import java.io.Serializable;

public class Ingredientes implements Serializable {
    private int id_receta;
    private String nombre,cantidad;

    public Ingredientes() {
    }

    public Ingredientes(String cantidad, String nombre) {
        this.cantidad = cantidad;
        this.nombre = nombre;
    }

    public int getId_receta() {
        return id_receta;
    }

    public void setId_receta(int id_receta) {
        this.id_receta = id_receta;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
