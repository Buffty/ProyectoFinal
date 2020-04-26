package com.example.proyecto_final_android_2019_20.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Address;
import android.os.Bundle;
import android.view.View;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.clases.Ingredientes;
import com.example.proyecto_final_android_2019_20.clases.Recetas;
import com.example.proyecto_final_android_2019_20.fragments.AnyadirFragment;
import com.example.proyecto_final_android_2019_20.fragments.CompartirFragment;
import com.example.proyecto_final_android_2019_20.fragments.DetallesFragment;
import com.example.proyecto_final_android_2019_20.fragments.ModificarFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddReceta extends AppCompatActivity implements View.OnClickListener{

    // VARIABLES INICIADAS

    TabLayout tab_layout;
    private Bundle retorno = new Bundle();
    MaterialButton btn_modify;
    int contador=0;
    Boolean nuevo = false;
    public static Boolean modificar=false;
    Recetas receta;
    AnyadirFragment addFragment;
    ModificarFragment modFragment;
    DetallesFragment detallesFragment;
    CompartirFragment compartirFragment;
    FragmentManager supportFragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
    public static ArrayList<Ingredientes> listaIngredientes = new ArrayList<Ingredientes>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receta);

        if(this.getIntent().getExtras()!=null){
            retorno = this.getIntent().getExtras();
            receta = (Recetas) retorno.getSerializable("Receta");
        }
        tab_layout = (TabLayout) findViewById(R.id.tabs);
        btn_modify = (MaterialButton) findViewById(R.id.btn_modificar);

        // SE COMPRUEBA SI HA SIDO PULSADO EL BOTÓN DE AÑADIR O EL DE MODIFICAR, PASANDO UNA RECETA O NO

        if(receta!=null) {
            modFragment = ModificarFragment.newInstance(receta);
            transaction.replace(R.id.layout_fragment,modFragment).addToBackStack(null).commit();
        }else{
            nuevo=true;
            addFragment = new AnyadirFragment();
            addFragment.passarPackageManager(getPackageManager());
            addFragment.passarActividad(this);
            transaction.replace(R.id.layout_fragment,addFragment).addToBackStack(null).commit();
            btn_modify.setText(R.string.txt_siguiente);
        }


        // LISTENER DE TAB_LAYOUT CON EL QUE PODER IR CAMBIANDO DE FRAGMENTS DE LAS PESTAÑAS

        tab_layout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                transaction = getSupportFragmentManager().beginTransaction();
                if(tab_layout.getSelectedTabPosition()==0){
                    transaction.hide(comprobarFragment());
                    if(nuevo){
                        transaction.show(addFragment).commit();
                        btn_modify.setText(R.string.txt_siguiente);
                    }else{
                        if(addFragment!=null){
                            transaction.show(addFragment).commit();
                            btn_modify.setText(R.string.txt_siguiente);
                        }else{
                            transaction.show(modFragment).commit();
                            btn_modify.setText(R.string.btn_txt_modificar);
                        }
                    }
                    contador=0;
                }else if(tab_layout.getSelectedTabPosition()==1){
                    transaction.hide(comprobarFragment());
                    if(detallesFragment== null){
                        if(modificar){
                            detallesFragment = DetallesFragment.newInstance(receta);
                            btn_modify.setText(R.string.txt_guardar);
                        }else{
                            detallesFragment = DetallesFragment.newInstance(receta);
                            btn_modify.setText(R.string.txt_guardar);
                        }
                        detallesFragment.setSupportFragment(getSupportFragmentManager());
                        transaction.add(R.id.layout_fragment,detallesFragment).commit();
                    }else{
                        transaction.show(detallesFragment).commit();
                        btn_modify.setText(R.string.txt_guardar);
                    }
                    contador=1;
                }else if(tab_layout.getSelectedTabPosition()==2){
                    transaction.hide(comprobarFragment());
                    if(compartirFragment== null){
                        compartirFragment = new CompartirFragment();
                        transaction.add(R.id.layout_fragment,compartirFragment).commit();
                    }else{
                        transaction.show(compartirFragment).commit();
                    }
                    btn_modify.setText(R.string.txt_compartir);
                    contador=2;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // EL BOTÓN DE GUARDAR SIEMPRE A LA ESCUCHA

        btn_modify.setOnClickListener(this);
    }

    // MÉTODO PARA COMPROBAR QUE FRAGMENT ESTA ACTIVO EN ESE MOMENTO

    public Fragment comprobarFragment(){
        if(addFragment!=null && addFragment.isVisible())
            return addFragment;
        else if(modFragment!=null && modFragment.isVisible())
            return modFragment;
        else if(detallesFragment!=null && detallesFragment.isVisible())
            return detallesFragment;
        else
            return compartirFragment;
    }

    // SOBREESCRIBIMOS EL MÉTODO DEL BOTÓN DE ATRÁS DEL MÓVIL

    @Override
    public void onBackPressed() {
        int postTab = tab_layout.getSelectedTabPosition();
        transaction = getSupportFragmentManager().beginTransaction();
        switch (postTab){
            case 0:
                this.finish();
                break;
            case 1:
                tab_layout.selectTab(tab_layout.getTabAt(postTab - 1));
                contador--;
                break;
            case 2:
               if(detallesFragment!=null){
                    tab_layout.selectTab(tab_layout.getTabAt(postTab-1));
                    contador--;
                }else {
                    tab_layout.selectTab(tab_layout.getTabAt(postTab-2));
                    contador-=2;
                }
                break;
        }
    }

    // EL CAOS DESATADO EN UN ON CLICK

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_modificar:

                // EN PRUCES ; SECCIÓN DE MODIFICAR

                if(btn_modify.getText().equals(getResources().getString(R.string.btn_txt_modificar))){
                    if(modFragment.isVisible()) {
                        transaction = getSupportFragmentManager().beginTransaction();
                        addFragment = new AnyadirFragment();
                        addFragment.passarPackageManager(getPackageManager());
                        addFragment.passarActividad(this);
                        addFragment.setReceta(modFragment.devolverReceta());
                        transaction.replace(R.id.layout_fragment, addFragment).commit();
                        btn_modify.setText(R.string.txt_siguiente);
                        detallesFragment=null;
                        modificar = true;
                        contador=0;
                    }

                // SECCIÓN SIGUIENTE

                }else if(btn_modify.getText().toString().equals(getResources().getString(R.string.txt_siguiente))){
                    int postTab = tab_layout.getSelectedTabPosition();
                    switch(contador){
                        case 2:
                            tab_layout.selectTab(tab_layout.getTabAt(0));
                            transaction = getSupportFragmentManager().beginTransaction();
                            if(compartirFragment==null){
                                compartirFragment = new CompartirFragment();
                                transaction.add(R.id.layout_fragment,compartirFragment);
                            }else{
                                transaction.show(compartirFragment);
                            }
                            transaction.hide(comprobarFragment()).commit();
                            break;
                        case 1:
                            tab_layout.selectTab(tab_layout.getTabAt(postTab+1));
                            btn_modify.setText(R.string.txt_guardar);
                            contador++;
                            break;
                        case 0:

                            tab_layout.selectTab(tab_layout.getTabAt(postTab+1));
                            contador++;
                            break;

                    }

                // SECCIÓN DE GUARDAR

                }else if(btn_modify.getText().toString().equals(getResources().getString(R.string.txt_guardar))){

                    if(modificar){
                        for(int i = 0 ; i < Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().size();i++){
                            if(Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().get(i).getId()==detallesFragment.getReceta().getId()){
                                Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().set(i,detallesFragment.getReceta());
                            }
                        }
                    }else {
                        Recetas nuevaReceta = addFragment.getDatos();
                        for (int i = 0; i < AddReceta.listaIngredientes.size(); i++) {
                            listaIngredientes.get(i).setId_receta(nuevaReceta.getId());
                        }
                        Login.ingredientesDatabase.setValue(listaIngredientes);
                        Login.listaUsuarios.get(ventanaPrincipal.posicion).addRecetas(nuevaReceta);
                        Login.recetasDatabase.setValue(Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas());
                    }
                    this.finish();
                }
                break;
        }
    }
}
