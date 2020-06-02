package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.os.AsyncTask;

import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.activities.ventanaPrincipal;
import com.example.proyecto_final_android_2019_20.bbdd.ConnectionData;
import com.example.proyecto_final_android_2019_20.bbdd.OdooConnect;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.example.proyecto_final_android_2019_20.entities.Utility.Utilidades;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargarListaRecetas extends AsyncTask<Void, ArrayList<String>,Boolean> {

    public static ConnectionData cd = new ConnectionData();
    public static OdooConnect oc = null;

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            oc = OdooConnect.getOdooConnect(cd.getUrl(), cd.getPort(), cd.getDb(), cd.getUser(), cd.getPasswd());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<Recetas> listaRecetario = new ArrayList<Recetas>();

        List<HashMap<String, Object>> listaRecetas = oc.search_read("programa_recetario.recetas", new Object[]{new Object[]{new Object[]{"usuarios_id", "=", Login.listaUsuarios.get(ventanaPrincipal.posicion).getNombre()}}}, "name", "descripcion", "tipo", "dificultad", "duracion", "image", "ingredientes_id","usuarios_id");

        for(Map mapa: listaRecetas){
            listaRecetario.add(Utilidades.rellenarCamposDeRecetas(mapa));
        }
        Login.listaUsuarios.get(ventanaPrincipal.posicion).setListaRecetas(listaRecetario);
        return true;
    }



}
