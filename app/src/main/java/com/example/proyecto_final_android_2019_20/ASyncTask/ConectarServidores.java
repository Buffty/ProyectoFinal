package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.bbdd.ConnectionData;
import com.example.proyecto_final_android_2019_20.bbdd.OdooConnect;
import com.example.proyecto_final_android_2019_20.entities.Ingredientes;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.example.proyecto_final_android_2019_20.entities.Usuarios;
import com.example.proyecto_final_android_2019_20.entities.Utility.Utilidades;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ConectarServidores extends AsyncTask<Void, ArrayList<String>, Boolean> {

    private Context context;
    private static final String TAG = "ERROR";
    public static ConnectionData cd = new ConnectionData();

    public ConectarServidores(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        OdooConnect oc = null;
        try {
            oc = OdooConnect.getOdooConnect(cd.getUrl(), cd.getPort(), cd.getDb(), cd.getUser(), cd.getPasswd());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Se hace un new Object[]{} para que coja un Objecto, otro new dentro del mismo para
        // un array de Objectos y luego el 3 new Object es para hacer el filtrado
        // para que cumpla esa condición

        List<HashMap<String, Object>> listaUsuarios = oc.search_read("res.partner", new Object[]{new Object[]{new Object[]{"is_app", "=", true}}}, "name","is_app", "passwd", "email", "country_id","recetas_id","image");

        for (Map mapa : listaUsuarios) {
            rellenarCamposDeUsuario(mapa);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    public void rellenarCamposDeUsuario(Map mapa) {
        Usuarios usuarios = new Usuarios();
        ArrayList<String> rellenarDatos = Utilidades.rellenarArrayDelMapping(mapa);
        try {
            for (int i = 0; i < mapa.keySet().toArray().length; i++) {
                switch (mapa.keySet().toArray()[i].toString()) {
                    case "passwd":
                        usuarios.setPassword(rellenarDatos.get(i));
                        break;
                    case "name":
                        usuarios.setNombre(rellenarDatos.get(i));
                        break;
                    case "id":
                        usuarios.setId(Integer.parseInt(rellenarDatos.get(i)));
                        break;
                    case "email":
                        usuarios.setEmail(rellenarDatos.get(i));
                        break;
                    case "image":
                        usuarios.setImagen(rellenarDatos.get(i));
                    case "country_id":
                        usuarios.setPais(rellenarDatos.get(i));
                        break;
                }
            }
            Login.listaUsuarios.add(usuarios);
            usuarios = new Usuarios();
        }finally{
            usuarios = null;
            rellenarDatos = null;
        }

    }
}
