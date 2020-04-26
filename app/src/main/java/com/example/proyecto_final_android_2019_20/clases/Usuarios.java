package com.example.proyecto_final_android_2019_20.clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuarios implements Serializable {
    String nombre, email, password,pais;
    ArrayList<Recetas> listaRecetas;

    public Usuarios(){
            listaRecetas = new ArrayList<Recetas>();
    }

    public Usuarios(String nombre, String email, String password, String pais) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.pais=pais;
        this.listaRecetas = new ArrayList<Recetas>();
    }

    public void addRecetas(Recetas receta){
        this.listaRecetas.add(0,receta);
    }

    public Recetas buscarReceta(String titulo){
        for (Recetas rec : listaRecetas)
            if(rec.getNombre().equals(titulo))
                return rec;

        return null;
    }

    public boolean eliminarRecetas(Recetas rec){
        if(buscarReceta(rec.getNombre())!=null) {
            listaRecetas.remove(buscarReceta(rec.getNombre()));
            return true;
        }
        return false;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public ArrayList<Recetas> getListaRecetas() {
        return listaRecetas;
    }

    public void setListaRecetas(ArrayList<Recetas> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
