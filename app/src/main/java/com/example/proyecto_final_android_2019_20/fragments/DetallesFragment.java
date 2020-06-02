package com.example.proyecto_final_android_2019_20.fragments;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyecto_final_android_2019_20.Dialogs.IngreDialFragment;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.activities.AddReceta;
import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.activities.ventanaPrincipal;
import com.example.proyecto_final_android_2019_20.adapters.AdaptadorIngredientes;
import com.example.proyecto_final_android_2019_20.entities.Ingredientes;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
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
            if(!AddReceta.modificar && !AddReceta.nuevo)
                btn_menu.setVisibility(View.INVISIBLE);
            else
                btn_menu.setVisibility(View.VISIBLE);
        }else {
            adaptador = new AdaptadorIngredientes(vistaLayout.getContext(), AddReceta.listaIngredientes);
        }

        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(vistaLayout.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        // PARA CUANDO DESPLAZAMOS CON EL DEDO A LA DERECHA DEL RECYCLER VIEW SE ELIMINE, SOLO TENEMOS LA OPCIÓN DE LA DERECHA

        final ItemTouchHelper.SimpleCallback myCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch(direction){
                    case ItemTouchHelper.RIGHT:
                        for( int i = 0 ; i < Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().size();i++){
                            if(Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().get(i).getId() == receta.getId()){
                                Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().get(i).getListaIngredientes().remove(viewHolder.getAdapterPosition());
                            }
                        }
                        adaptador.notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                    case ItemTouchHelper.LEFT:
                        adaptador.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                int margen = (int)getResources().getDimension(R.dimen.margen);
                Drawable icono;
                Paint pincel = new Paint();
                pincel.setColor(getResources().getColor(R.color.colorGrisBlanca));
                int sizetext = getResources().getDimensionPixelSize(R.dimen.tamanyoEliminar);
                pincel.setTextSize(sizetext);

                if(dX > 0){
                    c.clipRect(viewHolder.itemView.getLeft(),viewHolder.itemView.getTop(),dX,viewHolder.itemView.getBottom());

                    if(dX < recyclerView.getWidth() / 3)
                        c.drawColor(Color.GRAY);
                    else
                        c.drawColor(getResources().getColor(R.color.colorBorrarGranate));

                    icono = DetallesFragment.super.getResources().getDrawable(R.drawable.ic_delete_sweep_black_24dp);
                    icono.setBounds(new Rect(viewHolder.itemView.getLeft()+margen-15, viewHolder.itemView.getTop()+margen, viewHolder.itemView.getHeight()-margen, viewHolder.itemView.getBottom()-margen));
                    icono.draw(c);
                    pincel.setTextAlign(Paint.Align.LEFT);
                    c.drawText(getResources().getString(R.string.eliminar),viewHolder.itemView.getHeight(),viewHolder.itemView.getBottom()-viewHolder.itemView.getHeight()/2+sizetext/2,pincel);

                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(myCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        Ingredientes ingre = new Ingredientes(cantidad,name_ingre,1,1);
        if(receta!=null)
            receta.getListaIngredientes().add(ingre);
        else
            AddReceta.listaIngredientes.add(ingre);
        refrescarLista();
    }
}
