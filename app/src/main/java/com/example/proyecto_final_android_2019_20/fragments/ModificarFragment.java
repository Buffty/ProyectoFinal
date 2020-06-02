package com.example.proyecto_final_android_2019_20.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.entities.Recetas;


public class ModificarFragment extends Fragment implements View.OnClickListener{

    public final static String ARG_NOMBRE = "ARG_NOMBRE";
    private Recetas receta;
    private OnMiPrimerFragmentListener mListener;
    private TextView cont_title_receta,cont_dificultad_receta,cont_type_receta,duracion_de_la_receta,cont_descripcion;
    private ImageView imagen;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistaLayout = inflater.inflate(R.layout.modificar_recetas,container,false);

        // INICIACIÓN DE LOS FIELDS DE CUANDO LE PASAS UN INTENT

        cont_title_receta = (TextView) vistaLayout.findViewById(R.id.txt_cont_title_receta);
        cont_dificultad_receta =  (TextView) vistaLayout.findViewById(R.id.txt_cont_dificultad_receta);
        cont_type_receta = (TextView) vistaLayout.findViewById(R.id.txt_cont_tipo_receta);
        duracion_de_la_receta = (TextView) vistaLayout.findViewById(R.id.descripcion_dur_receta);
        cont_descripcion = (TextView) vistaLayout.findViewById(R.id.descripcion_receta);
        imagen = (ImageView) vistaLayout.findViewById(R.id.changeImage);

        // RELLENAMOS LAS VARIABLES

        cont_title_receta.setText(receta.getNombre());
        cont_dificultad_receta.setText(receta.getDificultad());
        cont_type_receta.setText(receta.getTipo());
        duracion_de_la_receta.setText(""+receta.getDuracion());
        cont_descripcion.setText(receta.getDescripcion());

        byte[] decodedString = Base64.decode(receta.getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        imagen.setImageBitmap(decodedByte);
        imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return vistaLayout;
    }

    @Override
    public void onClick(View view) {
        mListener.onMiPrimerFragmentClick();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arg = getArguments();
        if(arg != null){
            receta = (Recetas) arg.getSerializable(ARG_NOMBRE);

        }
    }

    public Recetas devolverReceta(){
        return receta;
    }

    public static ModificarFragment newInstance(Recetas receta){
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOMBRE,receta);

        ModificarFragment fragment = new ModificarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMiPrimerFragmentListener){
            mListener = (OnMiPrimerFragmentListener) context;
        }
    }

    public interface OnMiPrimerFragmentListener{
        public void onMiPrimerFragmentClick();
    }
}
