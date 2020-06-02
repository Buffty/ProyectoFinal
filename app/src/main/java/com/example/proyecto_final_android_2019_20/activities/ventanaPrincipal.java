package com.example.proyecto_final_android_2019_20.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.proyecto_final_android_2019_20.ASyncTask.BorrarListaRecetas;
import com.example.proyecto_final_android_2019_20.ASyncTask.CargarListaRecetas;
import com.example.proyecto_final_android_2019_20.R;
import com.example.proyecto_final_android_2019_20.adapters.AdaptorRecetas;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ventanaPrincipal extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    public static int posicion;

    private MaterialButton material_abrir;
    private CargarListaRecetas cargarListaRecetas;
    private Bundle retorno = new Bundle();
    private FloatingActionButton botonAdd,botonAcercaDe,botonForo,botonPerfil;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptorRecetas adaptador;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txt_usuario;
    private Uri mImage;
    private static final String TAG = "ERROR";
    private static final int ACTIVITY_IMAGE_SELECT = 2;
    private NavigationView navigator;
    private DrawerLayout drawer;
    private ImageView imagenUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventana_principal);

        if(this.getIntent().getExtras()!=null){
            retorno = this.getIntent().getExtras();
            posicion = (Integer) retorno.getInt("Posicion");
        }

        // INICIAR VARIABLES

        drawer = (DrawerLayout) findViewById(R.id.drawer_lay);
        navigator = (NavigationView) findViewById(R.id.nav_view);
        View v  = navigator.getHeaderView(0);
        imagenUsuario = (ImageView) v.findViewById(R.id.imagen_usu);
        txt_usuario = (TextView) v.findViewById(R.id.txt_usu);
        botonAdd = (FloatingActionButton) findViewById(R.id.btn_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_recetas);
        material_abrir = (MaterialButton) findViewById(R.id.btn_abrir);
        botonPerfil = (FloatingActionButton) findViewById(R.id.btn_perfil);
        /*botonAcercaDe = (FloatingActionButton) findViewById(R.id.btn_acercaDe);
        botonForo =(FloatingActionButton) findViewById(R.id.btn_foro);*/

        // ADAPTADOR DEL RECYCLERVIEW
        recyclerView = (RecyclerView) findViewById(R.id.recycler_recetas);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        cargarListaRecetas = new CargarListaRecetas();
        while(true){
            try {
                if (cargarListaRecetas.execute().get()) break;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        cargarDatos();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // ESTO SE EJECUTA CADA VEZ QUE SE REALIZA EL GESTO
                refrescarLista();
                adaptador.notifyDataSetChanged();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);

        // PARA CUANDO DESPLAZAMOS CON EL DEDO A LA DERECHA DEL RECYCLER VIEW SE ELIMINE, SOLO TENEMOS LA OPCIÓN DE LA DERECHA

        final ItemTouchHelper.SimpleCallback myCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch(direction){
                    case ItemTouchHelper.RIGHT:
                        BorrarListaRecetas borrarListaRecetas = new BorrarListaRecetas();
                        while(true) {
                            try {
                                if(borrarListaRecetas.execute(Login.listaUsuarios.get(posicion).getListaRecetas().get(viewHolder.getAdapterPosition())).get()) break;
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Login.listaUsuarios.get(posicion).getListaRecetas().remove(viewHolder.getAdapterPosition());
                        adaptador.notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                    case ItemTouchHelper.LEFT:
                        adaptador.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                int margen = (int)getResources().getDimension(R.dimen.margen);
                Drawable icono;
                Paint pincel = new Paint();
                pincel.setColor(getResources().getColor(R.color.colorGrisBlanca));
                int sizetext = getResources().getDimensionPixelSize(R.dimen.tamanyoEliminar);
                pincel.setTextSize(sizetext);

                if(dX > 0){
                    c.clipRect(viewHolder.itemView.getLeft(),viewHolder.itemView.getTop(),dX,viewHolder.itemView.getBottom());

                    if(dX < recyclerView.getWidth() / 3)
                        c.drawColor(Color.GRAY);
                    else
                        c.drawColor(getResources().getColor(R.color.colorBorrarGranate));

                    icono = getDrawable(R.drawable.ic_delete_sweep_black_24dp);
                    icono.setBounds(new Rect(viewHolder.itemView.getLeft()+margen-15, viewHolder.itemView.getTop()+margen, viewHolder.itemView.getHeight()-margen, viewHolder.itemView.getBottom()-margen));
                    icono.draw(c);
                    pincel.setTextAlign(Paint.Align.LEFT);
                    c.drawText(getResources().getString(R.string.eliminar),viewHolder.itemView.getHeight(),viewHolder.itemView.getBottom()-viewHolder.itemView.getHeight()/2+sizetext/2,pincel);

                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

        /*********************************************************/

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(myCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        botonPerfil.setOnClickListener(this);
        botonAdd.setOnClickListener(this);
        navigator.setNavigationItemSelectedListener(this);
        imagenUsuario.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        SharedPreferences pref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        if(pref.getBoolean("login",true)){
            this.finish();
        }else{
            Snackbar.make(this.recyclerView, "Debes cerrar sesión antes.", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        posicion = posicion;
        super.onPause();
    }

    public void cargarDatos(){

        /* CARGAMOS LOS DATOS DEL ADAPTADOR*/

        adaptador = new AdaptorRecetas(this,Login.listaUsuarios.get(posicion).getListaRecetas());
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        txt_usuario.setText(Login.listaUsuarios.get(posicion).getNombre());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("Posicion",posicion);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if(savedInstanceState.getInt("Posicion")!=0){
            posicion = (Integer) savedInstanceState.getInt("Posicion");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_IMAGE_SELECT){
            mImage = data.getData();
            setPic();
        }else {
            adaptador.notifyDataSetChanged();
            switch (resultCode) {
                case 0:
                    Uri dato = data.getData();
                    posicion = (Integer) data.getExtras().getInt("Posicion");
                    swipeRefreshLayout.setRefreshing(true);
                    break;
            }
        }

    }


    private void setPic() {
        // Get the dimensions of the View
        int targetW = imagenUsuario.getWidth();
        int targetH = imagenUsuario.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = 0;
        if(photoW<photoH)
            scaleFactor = Math.min(photoW/targetH, photoH/targetW);
        else
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = getContentResolver().openFileDescriptor( mImage , "r" );
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            imagenUsuario.setImageBitmap(bitmap);
            imagenUsuario.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refrescarLista(){

        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                    AddReceta.receta = null;
                    Intent intent = new Intent(ventanaPrincipal.this,AddReceta.class);
                    startActivity(intent);
                break;
            case R.id.btn_perfil:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.imagen_usu:
                galleryAddPic();
                break;

        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(mediaScanIntent,ACTIVITY_IMAGE_SELECT);
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opc_close:
                SharedPreferences pref = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = pref.edit();
                prefEditor.putString("username","");
                prefEditor.putString("password","");
                prefEditor.putInt("posicion",-1);
                prefEditor.putBoolean("login",true);
                prefEditor.apply();
                prefEditor.commit();
                this.finish();
                break;
            case R.id.opc_configure:
                break;
        }
        return true;
    }
}
