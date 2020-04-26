package com.example.proyecto_final_android_2019_20.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proyecto_final_android_2019_20.R;

public class IngreDialFragment extends DialogFragment {

    EditText cantidad,nom_ingre;

    public interface OnIngreListener{
        public void onIngrediente(String name_ingre, String cantidad);
    }

    private OnIngreListener listener;

    public IngreDialFragment(OnIngreListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dial_lay_ingre,null);

        cantidad = v.findViewById(R.id.edit_cant_ingre);
        nom_ingre = v.findViewById(R.id.edit_nom_ingre);

        builder.setView(v)
                .setPositiveButton("AÑADIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onIngrediente(nom_ingre.getText().toString(),cantidad.getText().toString());
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IngreDialFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
