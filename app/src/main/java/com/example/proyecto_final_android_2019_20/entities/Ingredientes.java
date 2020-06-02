package com.example.proyecto_final_android_2019_20.entities;

import java.io.Serializable;

public class Ingredientes implements Serializable{



    private int id, id_receta;
    private String nombre,cantidad;

    public Ingredientes() {
    }

    public Ingredientes(String cantidad, String nombre,int id,int id_receta) {
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.id = id;
        this.id_receta = id_receta;
    }


    public int getId_receta() {
        return id_receta;
    }

    public void setId_receta(int id_receta) {
        this.id_receta = id_receta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
