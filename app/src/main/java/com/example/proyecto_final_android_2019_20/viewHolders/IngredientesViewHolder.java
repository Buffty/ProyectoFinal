package com.example.proyecto_final_android_2019_20.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.clases.Ingredientes;

public class IngredientesViewHolder extends RecyclerView.ViewHolder{


    private TextView num,nombre_ingred;
    private Bundle bundle = new Bundle();
    Intent intent ;

    public IngredientesViewHolder(View itemView, Context context){
        super(itemView);
        num = (TextView) itemView.findViewById(R.id.txt_prueba);
        nombre_ingred = (TextView) itemView.findViewById(R.id.txt_nombre_ingr);

    }

    public void bindIngredientes(Ingredientes i){
        num.setText(""+i.getCantidad());
        nombre_ingred.setText(i.getNombre());
    }
}
