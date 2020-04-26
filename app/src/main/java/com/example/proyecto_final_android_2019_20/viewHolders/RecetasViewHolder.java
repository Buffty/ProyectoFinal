package com.example.proyecto_final_android_2019_20.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.activities.AddReceta;
import com.example.proyecto_final_android_2019_20.clases.ExpandAndCollapseViewUtil;
import com.example.proyecto_final_android_2019_20.clases.Recetas;
import com.google.android.material.button.MaterialButton;

import java.io.Serializable;
import java.util.ArrayList;

public class RecetasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private MaterialButton material_abrir;
    private TextView titulo,dificultad,conten_dificultad;
    private TextView txtduracion,txttipo,cont_duracion,cont_tipo;
    private ImageView imagen;
    private Context contexto;
    private static final int DURATION = 250;
    private ViewGroup linearLayoutDetails;
    private LinearLayout princi;
    private ImageView imageViewExpand;
    private boolean expandido;
    private Animation animation;
    private Recetas receta;
    private Bundle bundle = new Bundle();
    Intent intent ;

    public RecetasViewHolder(View itemView, Context context){

        super(itemView);
        expandido = false;
        titulo = (TextView) itemView.findViewById(R.id.titulo_recycler);
        dificultad = (TextView) itemView.findViewById(R.id.dificultad_recycler);
        conten_dificultad = (TextView) itemView.findViewById(R.id.txt_cont_dificultad);
        cont_duracion = (TextView) itemView.findViewById(R.id.con_duracion);
        cont_tipo = (TextView) itemView.findViewById(R.id.con_tipo);
        imagen = (ImageView) itemView.findViewById(R.id.recyclerImagen);
        txtduracion = (TextView) itemView.findViewById(R.id.txt_duracion);
        txttipo = (TextView) itemView.findViewById(R.id.txt_tipo);
        linearLayoutDetails = (LinearLayout) itemView.findViewById(R.id.layout_detalle);
        imageViewExpand = (ImageView) itemView.findViewById(R.id.imagen_expandir);
        princi = (LinearLayout) itemView.findViewById(R.id.principal);
        material_abrir = (MaterialButton) itemView.findViewById(R.id.btn_abrir);


        imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contexto = context;

        material_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(contexto.getApplicationContext(),AddReceta.class);
                bundle.putSerializable("Receta",receta);
                intent.putExtras(bundle);
                contexto.startActivity(intent);
            }
        });

        imageViewExpand.setOnClickListener(this);
    }

    public void bindRecetas(Recetas r){
        receta = r;
        titulo.setText(Html.fromHtml(r.nombre));
        dificultad.setText("Dificultad:");
        conten_dificultad.setText(r.dificultad);
        imagen.setImageResource(r.getImagen());
        imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
        txtduracion.setText("Duracion:");
        txttipo.setText("Tipo:");
        cont_duracion.setText(r.duracion+" minutos");
        cont_tipo.setText(r.tipo);

    }

    @Override
    public void onClick(View view){
        if (linearLayoutDetails.getVisibility() == View.GONE) {
            ExpandAndCollapseViewUtil.expand(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.drawable.ic_expand_more_black_24dp);
            rotate(-180.0f);
        } else {
            ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
            imageViewExpand.setImageResource(R.drawable.ic_expand_less_black_24dp);
            rotate(180.0f);
        }

    }
    private void rotate(float angle){
        animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(DURATION);
        imageViewExpand.startAnimation(animation);
    }

}
