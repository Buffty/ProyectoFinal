package com.example.proyecto_final_android_2019_20.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proyecto_final_android_2019_20.R;

public class DialLogin extends DialogFragment {

    ProgressBar progres;

    public interface OnDialLogin{
        public void OnDialLogin();
    }

    private OnDialLogin listener;

    public DialLogin(OnDialLogin listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dial_login,null));

        return builder.create();
    }
}
