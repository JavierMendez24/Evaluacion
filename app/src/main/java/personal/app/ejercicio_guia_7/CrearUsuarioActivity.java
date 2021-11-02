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
import android.widget.Button;
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

public class CrearUsuarioActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button btnCarImgCrearUsuario, btnCrearUsCrearUs;
    private EditText etUsuarioCrearUs, etContraseniaCrearUs;
    private ImageView ivCrearUs;
    private Bitmap img;
    private JugadorDao jugadorDao;
    private JugadorModel jugador;
    private ImagenDao imagenDao;
    private ImagenModel imagen;
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
                                ivCrearUs.setImageBitmap(bmp);
                            }

                        } catch (IOException ioException) {
                            Toast.makeText(CrearUsuarioActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                            ioException.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        centerTitle();
        setTitle("Crear Usuario");
        btnCarImgCrearUsuario = findViewById(R.id.btnCarImgCrearUsuario);
        etUsuarioCrearUs = findViewById(R.id.etUsuarioCrearUs);
        etContraseniaCrearUs = findViewById(R.id.etContraseniaCrearUs);
        ivCrearUs = findViewById(R.id.ivCrearUs);
        img = ((BitmapDrawable) ivCrearUs.getDrawable()).getBitmap();
        jugadorDao = new JugadorDaoImpRoom(getApplicationContext());
        imagenDao = new ImagenDaoImpRoom(getApplicationContext());
    }

    public void onClickbtnCarImgCrearUsuario(View view) {
        if(EasyPermissions.hasPermissions(CrearUsuarioActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(CrearUsuarioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResult.launch(intent);
        }else{
            String[] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            EasyPermissions.requestPermissions(CrearUsuarioActivity.this, "Dar acceso a la galería y a la escritura de archivos", 101, permisos);
        }
    }

    public void onClickbtnCrearUsCrearUs(View view) {
        if(Utils.verifyEditText(etUsuarioCrearUs) && Utils.verifyEditText(etContraseniaCrearUs)){
            if(EasyPermissions.hasPermissions(CrearUsuarioActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) && EasyPermissions.hasPermissions(CrearUsuarioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                jugador = jugadorDao.getIdUsuario(etUsuarioCrearUs.getText().toString());
                if(jugador == null) {
                    imagen = new ImagenModel();
                    saveToInternalStorage();
                    imagen.setUrl(path);
                    imagenDao.save(imagen);
                    imagen = imagenDao.getIdImg(path);
                    jugador = new JugadorModel();
                    jugador.setIdImg(String.valueOf(imagen.getId()));
                    jugador.setUsuario(etUsuarioCrearUs.getText().toString());
                    jugador.setContrasenia(etContraseniaCrearUs.getText().toString());
                    jugador.setPuntaje("10");
                    jugador.setIntentos("0");
                    jugadorDao.save(jugador);
                    jugadorDao = new JugadorDaoImpRoom(getApplicationContext());
                    jugador = new JugadorModel();
                    jugador = jugadorDao.iniciarSesion(etUsuarioCrearUs.getText().toString(), etContraseniaCrearUs.getText().toString());
                    SharedPreferences.Editor editorConfig = sharedPreferences.edit();
                    editorConfig.putString("ID", String.valueOf(jugador.getId()));
                    editorConfig.putString("IDIMG", jugador.getIdImg());
                    editorConfig.putString("USER", jugador.getUsuario());
                    editorConfig.putString("PASS", jugador.getContrasenia());
                    editorConfig.putString("PUNTAJE", jugador.getPuntaje());
                    editorConfig.putString("INTENTOS", jugador.getIntentos());
                    editorConfig.commit();
                    Toast.makeText(CrearUsuarioActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearUsuarioActivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }else{
                    Toast.makeText(CrearUsuarioActivity.this, "Nombre de usuario ya registrado", Toast.LENGTH_SHORT).show();
                }
            }else{
                String[] permisos = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                EasyPermissions.requestPermissions(CrearUsuarioActivity.this, "Dar acceso a la galería y a la escritura de archivos", 101, permisos);
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
            Toast.makeText(CrearUsuarioActivity.this, "Permisos denegados", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitMapFromURI(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private Boolean saveToInternalStorage(){
        path = "";
        Boolean saved = false;
        Bitmap bitmap = ((BitmapDrawable) ivCrearUs.getDrawable()).getBitmap();
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