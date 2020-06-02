package com.example.proyecto_final_android_2019_20.entities.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.core.app.ActivityCompat;

import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.entities.Ingredientes;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utilidades {

    public static void solicitarPermiso(final String permiso, String explicacion, final int codigoDePeticion, final Activity actividad){
        if(ActivityCompat.shouldShowRequestPermissionRationale(actividad,permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(explicacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(actividad,new String[]{permiso},codigoDePeticion);
                        }
                    }).show();
        }else{
            ActivityCompat.requestPermissions(actividad,new String[]{permiso},codigoDePeticion);

        }
    }


    public static ArrayList<String> rellenarArrayDelMapping(Map mapa) {
        ArrayList<String> stringsLista = new ArrayList<String>();
        try{
            for (Object valor :
                    mapa.values()) {
                stringsLista.add(valor.toString());
            }
            return stringsLista;
        }finally {
            stringsLista = null;
        }

    }

    public static Recetas rellenarCamposDeRecetas(Map mapa) {
        Recetas recetas = new Recetas();
        ArrayList<String> rellenarDatos = Utilidades.rellenarArrayDelMapping(mapa);

        try {
            for (int i = 0; i < mapa.keySet().toArray().length; i++) {
                switch (mapa.keySet().toArray()[i].toString()) {
                    case "descripcion":
                        recetas.setDescripcion(rellenarDatos.get(i));
                        break;
                    case "image":
                        recetas.setImagen(rellenarDatos.get(i));
                        break;
                    case "tipo":
                        recetas.setTipo(rellenarDatos.get(i));
                        break;
                    case "name":
                        recetas.setNombre(rellenarDatos.get(i));
                        break;
                    case "duracion":
                        recetas.setDuracion(rellenarDatos.get(i));
                        break;
                    case "usuarios_id":
                        recetas.setNameUser(rellenarDatos.get(i));
                        break;
                    case "id":
                        recetas.setId(Integer.parseInt(rellenarDatos.get(i)));
                        break;
                    case "dificultad":
                        recetas.setDificultad(rellenarDatos.get(i));
                        break;
                }
                System.out.println(recetas);
            }
            return recetas;
        } finally {
            rellenarDatos = null;
            recetas = null;
        }
    }


    public static int sacarIdPais(String pais){
        for (int i = 0 ; i < Login.paises.length; i++)
            if(Login.paises[i].equals(pais))
                return Integer.parseInt(Login.id[i]);
        return -1;
    }



    public static Ingredientes rellenarCamposDeIngredientes(Map mapa) {
        Ingredientes ingredientes = new Ingredientes();
        ArrayList<String> rellenarDatos = Utilidades.rellenarArrayDelMapping(mapa);
        try {
            for (int i = 0; i < mapa.keySet().toArray().length; i++) {
                switch (mapa.keySet().toArray()[i].toString()) {
                    case "id":
                        ingredientes.setId(Integer.parseInt(rellenarDatos.get(i)));
                        break;
                    case "name":
                        ingredientes.setNombre(rellenarDatos.get(i));
                        break;
                    case "quantity":
                        ingredientes.setCantidad(rellenarDatos.get(i));
                        break;
                }
            }
            return ingredientes;
        } finally {
            rellenarDatos = null;
            ingredientes = null;
        }
    }


    /*public static HashMap<String,Object> mapearRecetas(Recetas receta){
        HashMap<String,Object> recetas = new HashMap<>();
        HashMap<String,Object> ingredientes = new HashMap<>();

        recetas.put("descripcion",receta.getDescripcion());
        recetas.put("image",receta.getImagen());
        recetas.put("tipo",receta.getTipo());
        recetas.put("name",receta.getNombre());
        recetas.put("duracion",receta.getDuracion());
        recetas.put("id",receta.getId());
        recetas.put("dificultad",receta.getDificultad());
        Gson gson = new Gson();
        for (Ingredientes ingrediente :
                receta.getListaIngredientes()) {
            ingredientes.put("name",ingrediente.getNombre());
            ingredientes.put("id",ingrediente.getId());
        }
        recetas.put("ingredientes_id", gson.toJson(ingredientes));

        return recetas;
    }*/

    /*public static HashMap<String,Object> mapearIngredientes(Ingredientes ingredientes){
        HashMap<String,Object> ingrediente = new HashMap<>();

        ingrediente.put("id",ingredientes.getId());
        ingrediente.put("name",ingredientes.getNombre());
        ingrediente.put("quantity",ingredientes.getCantidad());

        return ingrediente;
    }*/

}
