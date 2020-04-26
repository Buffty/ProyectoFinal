package com.example.proyecto_final_android_2019_20.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyecto_final_android_2019_20.Dialogs.IngreDialFragment;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.activities.AddReceta;
import com.example.proyecto_final_android_2019_20.adapters.AdaptadorIngredientes;
import com.example.proyecto_final_android_2019_20.clases.Ingredientes;
import com.example.proyecto_final_android_2019_20.clases.Recetas;
import com.google.android.material.button.MaterialButton;

public class DetallesFragment extends Fragment implements View.OnClickListener, IngreDialFragment.OnIngreListener{

    public final static String ARG_NOMBRE = "ARG_NOMBRE";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialButton btn_menu;
    private AdaptadorIngredientes adaptador;
    private FragmentManager supportFragment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Recetas receta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistaLayout = inflater.inflate(R.layout.detalles_fragment,container,false);

        // INICIAMOS LAS VARIABLES

        recyclerView = (RecyclerView) vistaLayout.findViewById(R.id.reciclador_ingredientes);
        btn_menu = (MaterialButton) vistaLayout.findViewById(R.id.btn_ingredientes);
        swipeRefreshLayout = (SwipeRefreshLayout) vistaLayout.findViewById(R.id.swipeIngredientes);

        // RELLENAMOS EL ADAPTADOR
        if(receta!=null) {
            adaptador = new AdaptadorIngredientes(vistaLayout.getContext(), receta.getListaIngredientes());
            if(!AddReceta.modificar)
                btn_menu.setVisibility(View.INVISIBLE);
            else
                btn_menu.setVisibility(View.VISIBLE);
        }else {
            adaptador = new AdaptadorIngredientes(vistaLayout.getContext(), AddReceta.listaIngredientes);
        }

        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(vistaLayout.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // ESTO SE EJECUTA CADA VEZ QUE SE REALIZA EL GESTO
                refrescarLista();
                adaptador.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        btn_menu.setOnClickListener(this);

        return vistaLayout;
    }

    public void refrescarLista(){
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setSupportFragment(FragmentManager fragment){
        this.supportFragment=fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        if(arg != null) {
            receta = (Recetas) arg.getSerializable(ARG_NOMBRE);
        }
    }

    public static DetallesFragment newInstance(Recetas receta){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOMBRE,receta);

        DetallesFragment fragment = new DetallesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        IngreDialFragment ingre = new IngreDialFragment(this);
        ingre.show(this.supportFragment,null);
        ingre.setCancelable(false);
    }

    public Recetas getReceta() {
        return receta;
    }

    public void setReceta(Recetas receta) {
        this.receta = receta;
    }

    @Override
    public void onIngrediente(String name_ingre, String cantidad) {
        Ingredientes ingre = new Ingredientes(cantidad,name_ingre);
        if(receta!=null)
            receta.getListaIngredientes().add(ingre);
        else
            AddReceta.listaIngredientes.add(ingre);
        refrescarLista();
    }
}
