package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.os.AsyncTask;

import com.example.proyecto_final_android_2019_20.bbdd.ConnectionData;
import com.example.proyecto_final_android_2019_20.bbdd.OdooConnect;
import com.example.proyecto_final_android_2019_20.entities.Recetas;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class BorrarListaRecetas extends AsyncTask<Recetas, ArrayList<String>,Boolean> {

    private static ConnectionData cd = new ConnectionData();
    private static OdooConnect oc = null;
    @Override
    protected Boolean doInBackground(Recetas... recetas) {
        try {
            oc = OdooConnect.getOdooConnect(cd.getUrl(), cd.getPort(), cd.getDb(), cd.getUser(), cd.getPasswd());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Recetas receta = recetas[0];
        Object[]ids = new Object[1];
        ids[0]=receta.getId();
        oc.unlink("programa_recetario.recetas",ids);
        return true;
    }
}
