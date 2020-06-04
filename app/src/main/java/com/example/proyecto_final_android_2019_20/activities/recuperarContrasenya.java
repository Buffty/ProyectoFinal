package com.example.proyecto_final_android_2019_20.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.proyecto_final_android_2019_20.ASyncTask.CreateInServer;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.entities.Usuarios;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutionException;

public class recuperarContrasenya extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout txt_layout_contr,txt_layout_contr_compr,txt_layout_usu;
    TextInputEditText txt_edit_contr,txt_edit_contr_compr,txt_edit_usua;
    MaterialButton btnRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenya);

        // INICIAR VARIABLES

        txt_layout_contr = (TextInputLayout) findViewById(R.id.layout_contr);
        txt_layout_contr_compr = (TextInputLayout) findViewById(R.id.layout_contr_compr);
        txt_layout_usu = (TextInputLayout) findViewById(R.id.layout_usuario);

        txt_edit_contr =(TextInputEditText) findViewById(R.id.recuperar_contraseña) ;
        txt_edit_contr_compr = (TextInputEditText) findViewById(R.id.recuperar_contraseña_compr);
        txt_edit_usua = (TextInputEditText) findViewById(R.id.recuperar_usuario);

        btnRecuperar = (MaterialButton) findViewById(R.id.btn_recuperar);

        // PONEMOS LA ESCUCHA

        btnRecuperar.setOnClickListener(this);


        // CUANDO SE MODIFIQUE EL CONTENIDO DEL INPUTTEXT SE VOLVERÁ A MOSTRAR EL ICONO

        txt_edit_contr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txt_layout_contr.setEndIconVisible(true);
            }
        });

        txt_edit_contr_compr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txt_layout_contr_compr.setEndIconVisible(true);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(!txt_edit_contr.getText().toString().equals(txt_edit_contr_compr.getText().toString())){

            // OCULTAREMOS EL ICONO PARA QUE NO INTERFIERA CON EL ERROR

            txt_layout_contr.setEndIconVisible(false);
            txt_layout_contr_compr.setEndIconVisible(false);
            txt_edit_contr.setError("Las contraseñas no coinciden");
            txt_edit_contr_compr.setError("Las contraseñas no coinciden");
        }else{

            Usuarios user = comprobarUsuario(new Usuarios(1,txt_edit_usua.getText().toString(),"","",""));

            if(user!=null){
                for(int i = 0 ; i < Login.listaUsuarios.size() ; i ++)
                    if(Login.listaUsuarios.get(i).equals(user))
                        Login.listaUsuarios.get(i).setPassword(txt_edit_contr.getText().toString());

                CreateInServer createInServer = new CreateInServer();
                while(true) {
                    try {
                        if(createInServer.execute("Contrasenya").get()) break;

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(recuperarContrasenya.this,Login.class);
                startActivity(intent);

            }else{

                txt_edit_usua.setError("El Usuario no es el correcto");

            }

        }
    }

    public Usuarios comprobarUsuario(Usuarios usu){
        for (Usuarios user : Login.listaUsuarios){
            if(user.getNombre().equals(usu.getNombre())){
                return user;
            }
        }
        return null;
    }
}
