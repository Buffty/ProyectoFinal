package com.example.proyecto_final_android_2019_20.ASyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.clases.Usuarios;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ConectarServidores extends AsyncTask<Boolean, String, Boolean> {

    private Context context;
    private static final String TAG = "ERROR";

    public ConectarServidores(Context context){
        this.context=context;
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        while(booleans[0]){
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // RECORRERÁ TODOS LOS USUARIOS DE LA BASE DA DATOS Y LOS AÑADIRÁ A LA LISTA DE USUARIOS
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Login.listaUsuarios.add(postSnapshot.getValue(Usuarios.class));
                        if(isCancelled()){
                            break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // [START_EXCLUDE]
                    Toast.makeText(context, "Failed to load post.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            Login.usuarioDatabase.addValueEventListener(postListener);
            booleans[0]=false;
        }

        return true;
    }
}
