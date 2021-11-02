package personal.app.ejercicio_guia_7;

import static personal.app.ejercicio_guia_7.MainActivity.sharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import personal.app.ejercicio_guia_7.dao.ImagenDao;
import personal.app.ejercicio_guia_7.dao.ImagenDaoImpRoom;
import personal.app.ejercicio_guia_7.dao.JugadorDao;
import personal.app.ejercicio_guia_7.dao.JugadorDaoImpRoom;
import personal.app.ejercicio_guia_7.models.ImagenModel;
import personal.app.ejercicio_guia_7.models.JugadorModel;
import personal.app.ejercicio_guia_7.utils.Utils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MiPerfilActivity extends AppCompatActivity {

    private EditText etUsuarioMiPerfil, etContraseniaMiPerfil;
    private JugadorDao jugadorDao;
    private JugadorModel jugador;
    private ImagenDao imagenDao;
    private ImagenModel imagen;
    private ImageView ivMiPerfil;
    private Bitmap img;
    private String path;

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();
                    if( result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImage = data.getData();
                        Bitmap bmp = null;
                        try{
                            bmp = getBitMapFromURI(selectedImage);
                            if ( bmp != null ) {
                                img = bmp;
                                ivMiPerfil.setImageBitmap(bmp);
                            }

                        } catch (IOException ioException) {
                            Toast.makeText(MiPerfilActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                            ioException.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        centerTitle();
        setTitle("Mi Perfil");
        etUsuarioMiPerfil = findViewById(R.id.etUsuarioMiPerfil);
        etContraseniaMiPerfil = findViewById(R.id.etContraseniaMiPerfil);
        ivMiPerfil = findViewById(R.id.ivMiPerfil);
        jugadorDao = new JugadorDaoImpRoom(getApplicationContext());
        imagenDao = new ImagenDaoImpRoom(getApplicationContext());
        cargarDatos();
    }

    public void cargarDatos(){
        try {
            etUsuarioMiPerfil.setText(sharedPreferences.getString("USER", ""));
            etContraseniaMiPerfil.setText(sharedPreferences.getString("PASS", ""));
            imagen = imagenDao.get(Integer.parseInt(sharedPreferences.getString("IDIMG", "")));
            ivMiPerfil.setImageURI(Uri.fromFile(new File(imagen.getUrl())));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void onClickbtnCarImgMiPerfil(View view) {
        if(EasyPermissions.hasPermissions(MiPerfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(MiPerfilActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResult.launch(intent);
        }else{
            String[] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            EasyPermissions.requestPermissions(MiPerfilActivity.this, "Dar acceso a la galería y a la escritura de archivos", 101, permisos);
        }
    }

    public void onClickbtnActualizarMiPerfil(View view) {
        if(Utils.verifyEditText(etUsuarioMiPerfil) && Utils.verifyEditText(etContraseniaMiPerfil)){
            if(EasyPermissions.hasPermissions(MiPerfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(MiPerfilActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String etUsuarioMiPerfilaux = etUsuarioMiPerfil.getText().toString();
                String sharedPreferencesaux = sharedPreferences.getString("USER", "").toString();
                if(!etUsuarioMiPerfilaux.equals(sharedPreferencesaux)){
                    jugador = jugadorDao.getIdUsuario(etUsuarioMiPerfil.getText().toString());
                }
                if(jugador == null) {
                    try {
                        imagen = imagenDao.get(Integer.parseInt(sharedPreferences.getString("IDIMG", "")));
                        saveToInternalStorage();
                        imagen.setUrl(path);
                        imagen.setId(Integer.parseInt(sharedPreferences.getString("IDIMG", "")));
                        imagenDao.update(imagen);
                        jugador = new JugadorModel();
                        jugador.setId(Integer.parseInt(sharedPreferences.getString("ID", "")));
                        jugador.setIdImg(sharedPreferences.getString("IDIMG", "").toString());
                        jugador.setUsuario(etUsuarioMiPerfil.getText().toString());
                        jugador.setContrasenia(etContraseniaMiPerfil.getText().toString());
                        jugador.setPuntaje(sharedPreferences.getString("PUNTAJE", "").toString());
                        jugador.setIntentos(sharedPreferences.getString("INTENTOS", "").toString());
                        jugadorDao.update(jugador);
                        SharedPreferences.Editor editorConfig = sharedPreferences.edit();
                        editorConfig.putString("USER", jugador.getUsuario());
                        editorConfig.putString("PASS", jugador.getContrasenia());
                        editorConfig.commit();
                        Toast.makeText(MiPerfilActivity.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MiPerfilActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{
                    Toast.makeText(MiPerfilActivity.this, "Nombre de usuario ya registrado", Toast.LENGTH_SHORT).show();
                }
            }else{
                String[] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                EasyPermissions.requestPermissions(MiPerfilActivity.this, "Dar acceso a la galería y a la escritura de archivos", 101, permisos);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch(requestCode){
            case 101:
                Intent intent101 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResult.launch(intent101);
                break;
        }
    }

    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }else{
            Toast.makeText(MiPerfilActivity.this, "Permisos denegados", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitMapFromURI(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private Boolean saveToInternalStorage() {
        Boolean saved = false;
        try {
            File file = new File(imagen.getUrl());
            file.delete();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        path = "";
        Bitmap bitmap = ((BitmapDrawable) ivMiPerfil.getDrawable()).getBitmap();
        String dir = getApplicationInfo().dataDir + "/" + "imgs";
        File directory = new File(dir);
        directory.mkdirs();
        FileOutputStream fileOutputStream = null;
        String img = "IMG" + String.format("%d.png", System.currentTimeMillis());
        path = dir+"/"+img;
        try{
            fileOutputStream = new FileOutputStream(new File(directory, img));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
                saved = true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return saved;
    }

    public void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();
        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);
        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }
            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }

}