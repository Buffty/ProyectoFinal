package com.example.proyecto_final_android_2019_20.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Recetas implements Serializable {

    public String nombre, descripcion, tipo,dificultad;
    public String duracion, imagen, nameUser;
    public int id;
    public ArrayList<Ingredientes> listaIngredientes = new ArrayList<Ingredientes>();
    public Recetas() {
    }

    public Recetas(int id, String nameUser, String nombre, String descripcion, String imagen, String tipo,String dificultad, String duracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen= imagen;
        this.tipo=tipo;
        this.duracion=duracion;
        this.dificultad=dificultad;
        this.nameUser=nameUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public ArrayList<Ingredientes> getListaIngredientes() {
        return listaIngredientes;
    }

    public void setListaIngredientes(ArrayList<Ingredientes> listaIngredientes) {
        this.listaIngredientes = listaIngredientes;
    }
}
