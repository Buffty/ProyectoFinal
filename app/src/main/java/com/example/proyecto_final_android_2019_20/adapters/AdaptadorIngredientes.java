package com.example.proyecto_final_android_2019_20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.clases.Ingredientes;
import com.example.proyecto_final_android_2019_20.viewHolders.IngredientesViewHolder;

import java.util.ArrayList;

public class AdaptadorIngredientes extends RecyclerView.Adapter<IngredientesViewHolder> {

    private ArrayList<Ingredientes> listaIngredientes = new ArrayList<Ingredientes>();
    private Context contexto;
    private View.OnClickListener listener;

    public AdaptadorIngredientes(Context context, ArrayList<Ingredientes> ingredientes){
        this.contexto=context;
        this.listaIngredientes=ingredientes;
    }

    @NonNull
    @Override
    public IngredientesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_recycler_detalles,parent,false);
        IngredientesViewHolder viewHolder = new IngredientesViewHolder(view,contexto);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientesViewHolder holder, int position) {
        Ingredientes ingredientes = listaIngredientes.get(position);
        holder.itemView.setSelected(true);
        holder.bindIngredientes(ingredientes);
    }
    @Override
    public int getItemCount(){
        return listaIngredientes.size();
    }
}
