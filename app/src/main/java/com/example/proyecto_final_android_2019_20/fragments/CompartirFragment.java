package com.example.proyecto_final_android_2019_20.fragments;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto_final_android_2019_20.R;



public class CompartirFragment extends Fragment {

    private Activity actividad;
    private View vistaGeneral;
    private ImageView img_face,img_twitter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistaLayout = inflater.inflate(R.layout.compartir_fragment,container,false);
        vistaGeneral = vistaLayout;
        Resources res = vistaLayout.getResources();

        img_face = (ImageView) vistaLayout.findViewById(R.id.imagen_facebook);
        img_twitter = (ImageView) vistaLayout.findViewById(R.id.imagen_twitter);

        return vistaLayout;
    }

    public void passarActividad(Activity act){
        actividad=act;
    }
}
