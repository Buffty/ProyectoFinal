package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.os.AsyncTask;

import com.example.proyecto_final_android_2019_20.activities.AddReceta;
import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.activities.ventanaPrincipal;
import com.example.proyecto_final_android_2019_20.bbdd.ConnectionData;
import com.example.proyecto_final_android_2019_20.bbdd.OdooConnect;
import com.example.proyecto_final_android_2019_20.entities.Ingredientes;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.example.proyecto_final_android_2019_20.entities.Usuarios;
import com.example.proyecto_final_android_2019_20.entities.Utility.Utilidades;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateInServer extends AsyncTask<String, ArrayList<String>, Boolean> {

    private ConnectionData cd = new ConnectionData();
    private OdooConnect oc;

    public CreateInServer() {
    }

    @Override
    protected Boolean doInBackground(String... cadena) {
        try {
            oc = OdooConnect.getOdooConnect(cd.getUrl(), cd.getPort(), cd.getDb(), cd.getUser(), cd.getPasswd());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HashMap<String,Object> usuario = rellenarUsuario();

        String frase = cadena[0];

        if(frase.equals("Crear Usuario")){
            oc.create("res.partner",usuario);
        }else if(frase.equals("Modificar") || frase.equals("Guardar")){
            introducirRecetas();
            List<HashMap<String, Object>> listaRecetas = oc.search_read("programa_recetario.recetas", new Object[]{new Object[]{new Object[]{"usuarios_id", "=", Login.listaUsuarios.get(ventanaPrincipal.posicion).getNombre()}}}, "name", "descripcion", "tipo", "dificultad", "duracion", "image", "ingredientes_id","usuarios_id");
            ArrayList<Recetas> listaRec = new ArrayList<>();

            for (Map map : listaRecetas) {
                listaRec.add(Utilidades.rellenarCamposDeRecetas(map));
            }

            ArrayList<Ingredientes> listaIngredientes = new ArrayList<>();
            Login.listaUsuarios.get(ventanaPrincipal.posicion).setListaRecetas(listaRec);

            for (int i = 0; i < Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().size(); i++) {
                Recetas recetario = Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().get(i);
                if (recetario.getNombre().equals(AddReceta.receta.getNombre())) {
                    for (Ingredientes ingredientes : AddReceta.receta.getListaIngredientes()) {
                        ingredientes.setId_receta(recetario.getId());
                        listaIngredientes.add(ingredientes);
                        crearIngredientes(ingredientes);
                    }
                    Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().get(i).setListaIngredientes(listaIngredientes);
                }
            }
        }else if(frase.equals("Contrasenya") || frase.equals("Cerrar sesion")){
            HashMap<String, Object> usuarios = rellenarUsuarioEspecifico();
            escribirUsuario(usuarios,Login.listaUsuarios.get(ventanaPrincipal.posicion).getId());
        }
        return true;
    }

    private void introducirRecetas(){
        for (Recetas receta: Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas()) {
            crearReceta(receta);
        }
    }
    private void escribirUsuario(HashMap<String,Object> user,int id){
        Object[]ids = new Object[1];
        try{
            ids[0] = id;
            oc.write("res.partner",ids,user);
        }finally {
            ids = null;
        }
    }

    private HashMap<String,Object> rellenarUsuario(){
        HashMap<String,Object> mapeo = new HashMap<>();
        Usuarios usuarios = Login.listaUsuarios.get(Login.listaUsuarios.size()-1);
        try {
            mapeo.put("passwd", usuarios.getPassword());
            mapeo.put("name", usuarios.getNombre());
            mapeo.put("id", usuarios.getId());
            mapeo.put("email", usuarios.getEmail());
            mapeo.put("country_id", Utilidades.sacarIdPais(usuarios.getPais()));
            mapeo.put("image",usuarios.getImagen());
            mapeo.put("is_app", true);
            return mapeo;
        }finally {
            mapeo = null;
            usuarios = null;
        }
    }

    private HashMap<String,Object> rellenarUsuarioEspecifico(){
        HashMap<String,Object> mapeo = new HashMap<>();
        Usuarios usuarios = Login.listaUsuarios.get(ventanaPrincipal.posicion);
        try {
            mapeo.put("passwd", usuarios.getPassword());
            mapeo.put("name", usuarios.getNombre());
            mapeo.put("id", usuarios.getId());
            mapeo.put("email", usuarios.getEmail());
            mapeo.put("country_id", Utilidades.sacarIdPais(usuarios.getPais()));
            mapeo.put("image",usuarios.getImagen());
            mapeo.put("is_app", true);
            return mapeo;
        }finally {
            mapeo = null;
            usuarios = null;
        }
    }



    private void crearReceta (Recetas receta){
        HashMap<String,Object> recetas = new HashMap<>();
        Object[] ids = new Object[1];
        try {
            recetas.put("descripcion", receta.getDescripcion());
            recetas.put("image", receta.getImagen());
            recetas.put("tipo", receta.getTipo());
            recetas.put("name", receta.getNombre());
            recetas.put("duracion", receta.getDuracion());
            for (Usuarios user:
                    Login.listaUsuarios) {
                if(user.getNombre().equals(receta.getNameUser()))
                    recetas.put("usuarios_id",user.getId());
            }
            ids[0] = receta.getId();
            recetas.put("dificultad", receta.getDificultad());
            if (!buscarRecetas(receta))
                oc.create("programa_recetario.recetas", recetas);
            else
                oc.write("programa_recetario.recetas", ids, recetas);
        }finally {
            recetas = null;
            ids = null;
        }
    }

    private void crearIngredientes(Ingredientes ingredientes){
        HashMap<String,Object> ingrediente = new HashMap<>();
        Object[] ids = new Object[1];
        try {
            ingrediente.put("id", ingredientes.getId());
            ingrediente.put("name", ingredientes.getNombre());
            ingrediente.put("quantity", ingredientes.getCantidad());
            ingrediente.put("recetas_id", ingredientes.getId_receta());
            ids[0] = ingredientes.getId();
            if (!buscarIngrediente(ingredientes))
                oc.create("programa_recetario.ingredientes", ingrediente);
            else
                oc.write("programa_recetario.ingredientes", ids, ingrediente);
        }finally {
            ids = null;
            ingrediente = null;
        }

    }

    private boolean buscarRecetas(Recetas receta){
        int numero = oc.search_read("programa_recetario.recetas", new Object[]{new Object[]{new Object[]{"name", "=", receta.getNombre()}}}, "name").toArray().length;
        if(numero>0)
            return true;
        else
            return false;
    }

    private boolean buscarIngrediente(Ingredientes ingredientes){
        int numero = oc.search_read("programa_recetario.ingredientes", new Object[]{new Object[]{new Object[]{"name", "=", ingredientes.getNombre()}}}, "name").toArray().length;
        if(numero>0)
            return true;
        else
            return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
