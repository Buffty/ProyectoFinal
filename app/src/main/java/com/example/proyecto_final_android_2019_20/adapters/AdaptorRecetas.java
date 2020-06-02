package com.example.proyecto_final_android_2019_20.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.entities.Recetas;
import com.example.proyecto_final_android_2019_20.viewHolders.RecetasViewHolder;

import java.util.ArrayList;

public class AdaptorRecetas extends RecyclerView.Adapter<RecetasViewHolder>{

    private ArrayList<Recetas> listaRecetas;
    private Context contexto;

    public AdaptorRecetas(Context context, ArrayList<Recetas> recetas){
        this.contexto=context;
        this.listaRecetas=recetas;
    }

    @NonNull
    @Override
    public RecetasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista,parent,false);
        RecetasViewHolder viewHolder = new RecetasViewHolder(view,contexto);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecetasViewHolder holder, int position) {
            Recetas receta = listaRecetas.get(position);
            holder.itemView.setSelected(true);
            holder.bindRecetas(receta);
    }
    @Override
    public int getItemCount(){
        return listaRecetas.size();
    }

}
