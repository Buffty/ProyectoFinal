package com.example.proyecto_final_android_2019_20.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_final_android_2019_20.ASyncTask.ConectarServidores;
import com.example.proyecto_final_android_2019_20.Dialogs.DialLogin;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.entities.Usuarios;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity implements View.OnClickListener, DialLogin.OnDialLogin{

    MaterialButton inicio,registrar;
    TextView textoOlvidado;
    EditText usuario,password;
    Switch mostrar_passwd;
    Bundle retorno = new Bundle();

    public static String[] paises;
    public static String[] id;
    public static List<HashMap<String,Object>> listaPaises;
    public static ArrayList<Usuarios> listaUsuarios = new ArrayList<Usuarios>();

    private ConectarServidores conectar;
    private boolean nuevo_login=true;
    private SharedPreferences pref;
    private DialLogin dial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        nuevo_login = pref.getBoolean("login", true);

        if(!nuevo_login) {
            dial = new DialLogin(this);
            dial.show(getSupportFragmentManager(), null);
            dial.setCancelable(false);
        }

        Resources res = this.getBaseContext().getResources();
        paises = res.getStringArray(R.array.paises);
        id = res.getStringArray(R.array.id_paises);

        // INICIAR VARIABLES

        inicio = (MaterialButton) findViewById(R.id.botonInicio);
        registrar = (MaterialButton) findViewById(R.id.botonRegistrar);
        usuario = (EditText) findViewById(R.id.editor_texto_usuario);
        password = (EditText) findViewById(R.id.edit_password);
        textoOlvidado = (TextView) findViewById(R.id.forget_password);
        mostrar_passwd = (Switch) findViewById(R.id.passwd_switch);

        // PONER ESCUCHAS

        mostrar_passwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    password.setInputType(129);
                }
            }
        });

        inicio.setOnClickListener(this);
        registrar.setOnClickListener(this);
        textoOlvidado.setOnClickListener(this);

        this.conectar = new ConectarServidores(this);

        while(true) {
            try {
                if (conectar.execute().get()) {
                    if(!nuevo_login) {
                        dial.dismiss();
                        retorno.putInt("Posicion", pref.getInt("posicion", -1));
                        Intent intent = new Intent(Login.this, ventanaPrincipal.class);
                        intent.putExtras(retorno);
                        startActivity(intent);
                    }
                    break;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onPause() {
        if(this.conectar!=null){
            this.conectar.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if(conectar!=null){
            conectar.cancel(true);
        }
        super.onDestroy();
    }



    @Override
    public void onClick(View view) {
        Intent intent= null;
        switch(view.getId()){
            case R.id.botonInicio:
                dial = new DialLogin(this);
                dial.show(getSupportFragmentManager(), null);
                dial.setCancelable(false);
                String usu, passw;
                usu = usuario.getText().toString();
                passw = password.getText().toString();
                int posicion = encontrarUsuario(new Usuarios(0,usu, "", passw, ""));
                if (posicion != -1) {
                    SharedPreferences pref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = pref.edit();
                    prefsEditor.putString("username",usuario.getText().toString());
                    prefsEditor.putString("password",password.getText().toString());
                    prefsEditor.putInt("posicion",posicion);
                    prefsEditor.putBoolean("login",false);
                    prefsEditor.apply();
                    prefsEditor.commit();
                    retorno.putInt("Posicion", posicion);
                    intent = new Intent(Login.this, ventanaPrincipal.class);
                    intent.putExtras(retorno);
                } else {
                    usuario.setError("Los datos pasados no son los correctos.");
                    dial.dismiss();
                }
                break;
            case R.id.botonRegistrar:
                intent = new Intent(Login.this,registrarUsuario.class);
                break;
            case R.id.forget_password:
                intent = new Intent(Login.this, recuperarContrasenya.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    public int encontrarUsuario(Usuarios usu){
        if(!listaUsuarios.isEmpty())
            for (int i = 0 ; i < listaUsuarios.size() ; i++)
                if(listaUsuarios.get(i).getNombre().equals(usu.getNombre()) && listaUsuarios.get(i).getPassword().equals(usu.getPassword()))
                    return i;
        return -1;
    }

    @Override
    public void OnDialLogin() {

    }
}
