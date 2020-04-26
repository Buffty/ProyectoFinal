package com.example.proyecto_final_android_2019_20.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.PointerIcon;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_final_android_2019_20.ASyncTask.ConectarServidores;
import com.example.proyecto_final_android_2019_20.Dialogs.DialLogin;
import com.example.proyecto_final_android_2019_20.Dialogs.IngreDialFragment;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.clases.Usuarios;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener, DialLogin.OnDialLogin{

    MaterialButton inicio,registrar;
    TextView textoOlvidado;
    EditText usuario,password;
    Switch mostrar_passwd;
    Bundle retorno = new Bundle();
    public static ConectarServidores conectar;
    public static ArrayList<Usuarios> listaUsuarios = new ArrayList<Usuarios>();
    public static FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    public static DatabaseReference usuarioDatabase;
    public static DatabaseReference recetasDatabase;
    public static DatabaseReference ingredientesDatabase;
    private ValueEventListener value;
    private static final String TAG = "ERROR";
    private boolean nuevo_login=true;
    private SharedPreferences pref;
    private DialLogin dial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuarioDatabase = firebase.getReference("Usuarios");
        pref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        nuevo_login = pref.getBoolean("login", true);

        if(!nuevo_login) {
            dial = new DialLogin(this);
            dial.show(getSupportFragmentManager(), null);
            dial.setCancelable(false);
        }
        conectar = new ConectarServidores(this);
        conectar.execute(true);

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

        usuarioDatabase.onDisconnect().cancel(new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (!nuevo_login) {
                    dial.dismiss();
                    retorno.putInt("Posicion", pref.getInt("posicion",-1));
                    Intent intent = new Intent(Login.this, ventanaPrincipal.class);
                    intent.putExtras(retorno);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if(conectar!=null){
            conectar.cancel(true);
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
                String usu, passw;
                usu = usuario.getText().toString();
                passw = password.getText().toString();
                int posicion = encontrarUsuario(new Usuarios(usu, "", passw, ""));
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
                } else
                    usuario.setError("Los datos pasados no son los correctos.");
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
