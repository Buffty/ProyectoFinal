package com.example.proyecto_final_android_2019_20.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.activities.Login;
import com.example.proyecto_final_android_2019_20.activities.ventanaPrincipal;
import com.example.proyecto_final_android_2019_20.clases.Recetas;
import com.example.proyecto_final_android_2019_20.clases.Utilidades;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class AnyadirFragment extends Fragment implements View.OnClickListener{

    private OnMiPrimerFragmentListener mListener;
    private EditText cont_titulo_nueva_receta,cont_duracion_nueva_receta,cont_descrip_receta;
    private Spinner spinner_type,spinner_difficulty;
    private String[] arrayTipos,arrayDificultades;
    private ImageView cambiarImagen;
    private PackageManager packManag;
    private Recetas receta = null;
    private Activity actividad;
    private View vistaGeneral;
    public final int REQUEST_TAKE_PICTURE = 1;
    private String uri;
    private String ruta_imagen;
    File photo;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vistaLayout = inflater.inflate(R.layout.anyadir_fragment,container,false);
        vistaGeneral = vistaLayout;
        Resources res = vistaLayout.getResources();

        // INICIACIÓN DE LOS FIELDS DE CUANDO NO SE PASA EL INTENT

        cont_titulo_nueva_receta = (EditText) vistaLayout.findViewById(R.id.edit_cont_title_nuevo);
        spinner_difficulty = (Spinner) vistaLayout.findViewById(R.id.spinner_dificultad);
        spinner_type = (Spinner) vistaLayout.findViewById(R.id.spinner_tipo);
        cont_duracion_nueva_receta = (EditText) vistaLayout.findViewById(R.id.edit_cont_duracion_nuevo);
        cambiarImagen = (ImageView) vistaLayout.findViewById(R.id.changeImage);
        cont_descrip_receta = (EditText) vistaLayout.findViewById(R.id.edit_proba);

        arrayDificultades = res.getStringArray(R.array.dificultades);
        arrayTipos = res.getStringArray(R.array.tipos);

        ArrayAdapter<String> spinnerDificultad=new ArrayAdapter<String>(vistaLayout.getContext(),
                R.layout.spinner_addreceta,arrayDificultades);
        spinnerDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_difficulty.setAdapter(spinnerDificultad);

        ArrayAdapter<String> spinnerTipos=new ArrayAdapter<String>(vistaLayout.getContext(),
                R.layout.spinner_addreceta,arrayTipos);
        spinnerTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(spinnerTipos);

        if(receta!=null){
            cont_titulo_nueva_receta.setText(receta.getNombre());
            cont_duracion_nueva_receta.setText(""+receta.getDuracion());
            cont_descrip_receta.setText(receta.getDescripcion());
            for (int i  = 0 ; i < spinner_difficulty.getCount(); i++){
                if(spinner_difficulty.getItemAtPosition(i).equals(receta.getNombre())){
                    spinner_difficulty.setSelection(i);
                }
            }
            for (int i  = 0 ; i < spinner_type.getCount(); i++){
                if(spinner_type.getItemAtPosition(i).equals(receta.getTipo())){
                    spinner_type.setSelection(i);
                }
            }
            cambiarImagen.setImageResource(receta.getImagen());
            cambiarImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        cambiarImagen.setOnClickListener(this);

        return vistaLayout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.changeImage:
                // COMPROBAMOS TODAS LAS RESTRICCIONES
                if(ContextCompat.checkSelfPermission(vistaGeneral.getContext(),
                        Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
                    if (ContextCompat.checkSelfPermission(vistaGeneral.getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        if (ContextCompat.checkSelfPermission(vistaGeneral.getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            photo = createImageFile();
                            if(photo!=null) {
                                Uri photoUri = FileProvider.getUriForFile(vistaGeneral.getContext(),
                                        "com.example.android.fileprovider",photo);

                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                                startActivityForResult(takePicture, REQUEST_TAKE_PICTURE);
                            }
                        }else
                            Utilidades.solicitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    "La aplicación necesita permiso para leer una imagen",1,actividad);

                    else
                        Utilidades.solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                "La aplicación necesita permiso para guardar la imagen",1,actividad);
                else
                    Utilidades.solicitarPermiso(Manifest.permission.CAMERA,
                            "La aplicación necesita permiso para capturar la imagen",1,actividad);
                break;
        }

    }

    @NonNull
    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putSerializable("Receta",receta);
    }



    private File createImageFile(){

        // CREAMOS EL FICHERO DE IMAGEN
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName= "JPEG_"+timeStamp+"_";
        File storageDir = actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File image = File.createTempFile(imageFileName,".jpg",storageDir);

            // GUARDAMOS LA RUTA ABSOLUTA DEL ARCHIVO DE LA IMAGEN PARA USARLO EN OTROS INTENTS
            uri = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
            File prueba;
            if((prueba = actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES))!=null){
                ruta_imagen = prueba.toString()+"/"+photo.getName();
                File image = new File(ruta_imagen);
                Bitmap imageBitmap = BitmapFactory.decodeFile(image.getPath());
                cambiarImagen.setImageBitmap(imageBitmap);
                cambiarImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }

    public void setReceta(Recetas receta){
        this.receta=receta;
    }

    public Recetas getDatos(){
        if(receta==null) {
            Recetas nueva = new Recetas(Login.listaUsuarios.get(ventanaPrincipal.posicion).getListaRecetas().size() + 1,
                    Login.listaUsuarios.get(ventanaPrincipal.posicion).getNombre(),
                    cont_titulo_nueva_receta.getText().toString(),
                    cont_descrip_receta.getText().toString(),
                    R.drawable.hummus,
                    spinner_type.getSelectedItem().toString(),
                    spinner_difficulty.getSelectedItem().toString(),
                    cont_duracion_nueva_receta.getText().toString());
            return nueva;
        }else{
            return receta;
        }
    }

    public void passarPackageManager(PackageManager pack){
        packManag = pack;
    }

    public void passarActividad(Activity act){
        actividad=act;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
