package com.example.proyecto_final_android_2019_20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.clases.Usuarios;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class registrarUsuario extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText usuario,password,confirmar_password,correo;
    TextInputLayout lay_usu,lay_password,lay_conf_passwd,lay_correo;
    MaterialButton register;
    Spinner pais;
    Bundle retorno = new Bundle();

    String[] paises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        Resources res = this.getBaseContext().getResources();

        // RELLENAMOS EL SPINNER CON LOS PAISES

        paises = res.getStringArray(R.array.paises);

        // INICIAMOS LAS VARIABLES

        usuario = (TextInputEditText) findViewById(R.id.editable_registrar_user);
        password = (TextInputEditText) findViewById(R.id.editable_registrar_password);
        confirmar_password = (TextInputEditText) findViewById(R.id.editable_registrar_compr_passw);
        correo = (TextInputEditText) findViewById(R.id.correo_usuario);

        lay_usu = (TextInputLayout) findViewById(R.id.layout_registrar_user);
        lay_password = (TextInputLayout) findViewById(R.id.layout_registrar_password);
        lay_conf_passwd = (TextInputLayout) findViewById(R.id.layout_registrar_conf_passw);
        lay_correo = (TextInputLayout) findViewById(R.id.layout_registrar_correo);

        register = (MaterialButton) findViewById(R.id.btn_register);

        pais = (Spinner) findViewById(R.id.spinner_pais);

        // ADAPTAMOS EL SPINNER CON UN ESTILO CUSTOM

        ArrayAdapter<String> spinnerHead=new ArrayAdapter<String>(this,
                R.layout.spinner_estilo,paises);
        spinnerHead.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pais.setAdapter(spinnerHead);

        // PONEMOS EL BOTÓN DE REGISTRAR A ESCUCHAR

        register.setOnClickListener(this);

        // CUANDO SE MODIFIQUE EL CONTENIDO DEL INPUTTEXT SE VOLVERÁ A MOSTRAR EL ICONO

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lay_password.setEndIconVisible(true);
            }
        });

        confirmar_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                lay_conf_passwd.setEndIconVisible(true);
            }
        });


        // PARA VALIDAR QUE EL CORREO ES EL CORRECTO
        // if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())

    }

    @Override
    public void onClick(View view) {
        if(!password.getText().toString().equals(confirmar_password.getText().toString())){

            // OCULTAREMOS EL ICONO PARA QUE NO INTERFIERA CON EL ERROR

            lay_password.setEndIconVisible(false);
            lay_conf_passwd.setEndIconVisible(false);
            password.setError("Las contraseñas no coinciden");
            confirmar_password.setError("Las contraseñas no coinciden");
        }else{
            Usuarios user = crearUsuario();
            if(!user.getNombre().isEmpty() && !user.getPassword().isEmpty() && !user.getEmail().isEmpty()){
                if(comprobarUsuario(user)) {
                    Login.listaUsuarios.add(user);
                    Login.usuarioDatabase.setValue(Login.listaUsuarios);
                    Intent intent = new Intent(registrarUsuario.this, Login.class);
                    startActivity(intent);
                }else{
                    usuario.setError("El usuario ya existe!");
                }
            }
        }
    }

    public boolean comprobarUsuario(Usuarios usu){
        for(Usuarios user : Login.listaUsuarios){
            if(user.getNombre().equals(usu.getNombre())){
                return false;
            }
        }
        return true;
    }

    public Usuarios crearUsuario(){
        String nombre, contrasenya, email,txtPais;
        nombre = usuario.getText().toString();
        contrasenya = password.getText().toString();
        email = correo.getText().toString();
        txtPais = paises[pais.getSelectedItemPosition()];

        return new Usuarios(nombre,email,contrasenya,txtPais);
    }
}
