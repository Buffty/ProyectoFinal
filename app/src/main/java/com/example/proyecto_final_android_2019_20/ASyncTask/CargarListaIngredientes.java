package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.os.AsyncTask;

import com.example.proyecto_final_android_2019_20.bbdd.ConnectionData;
import com.example.proyecto_final_android_2019_20.bbdd.OdooConnect;
import com.example.proyecto_final_android_2019_20.entities.Ingredientes;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.example.proyecto_final_android_2019_20.entities.Utility.Utilidades;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.proyecto_final_android_2019_20.activities.AddReceta.receta;

public class CargarListaIngredientes extends AsyncTask<Void, ArrayList<String>,Boolean> {

    private static ConnectionData cd = new ConnectionData();
    private static OdooConnect oc = null;

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            oc = OdooConnect.getOdooConnect(cd.getUrl(), cd.getPort(), cd.getDb(), cd.getUser(), cd.getPasswd());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        List<HashMap<String, Object>> listaReceta = oc.search_read("programa_recetario.recetas", new Object[]{new Object[]{new Object[]{"id", "=", receta.getId()}}}, "image");
        ArrayList<Recetas> recetas = new ArrayList<>();
        for(Map map : listaReceta){
            recetas.add(Utilidades.rellenarCamposDeRecetas(map));
        }
        receta.setImagen(recetas.get(0).getImagen());


        List<HashMap<String, Object>> listaIngredientes = oc.search_read("programa_recetario.ingredientes", new Object[]{new Object[]{new Object[]{"recetas_id", "=", receta.getNombre()}}}, "name", "quantity");
        ArrayList<Ingredientes> listaIngre = new ArrayList<>();
        for (Map mapeo :
                listaIngredientes) {
            listaIngre.add(Utilidades.rellenarCamposDeIngredientes(mapeo));
        }
        receta.setListaIngredientes(listaIngre);
        return true;
    }

}
