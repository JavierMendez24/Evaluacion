package personal.app.ejercicio_guia_7;

import static personal.app.ejercicio_guia_7.MainActivity.NAME_FILE;
import static personal.app.ejercicio_guia_7.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

import personal.app.ejercicio_guia_7.dao.JugadorDao;
import personal.app.ejercicio_guia_7.dao.JugadorDaoImpRoom;
import personal.app.ejercicio_guia_7.models.JugadorModel;
import personal.app.ejercicio_guia_7.utils.Utils;

public class ConfiguracionActivity extends AppCompatActivity {

    private EditText etUsuario, etContrasenia;
    private JugadorDao dao;
    private JugadorModel jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        centerTitle();
        setTitle("Configuración");
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        dao = new JugadorDaoImpRoom(getApplicationContext());
    }

    public void onClickbtnInSeConfiguracion(View view) {
        if(Utils.verifyEditText(etUsuario) && Utils.verifyEditText(etContrasenia)) {
            jugador = dao.iniciarSesion(etUsuario.getText().toString(), etContrasenia.getText().toString());
            if(!(jugador == null)) {
                sharedPreferences = getSharedPreferences(NAME_FILE, MODE_PRIVATE);
                SharedPreferences.Editor editorConfig = sharedPreferences.edit();
                editorConfig.putString("ID", String.valueOf(jugador.getId()));
                editorConfig.putString("IDIMG", String.valueOf(jugador.getIdImg()));
                editorConfig.putString("USER", jugador.getUsuario());
                editorConfig.putString("PASS", jugador.getContrasenia());
                editorConfig.putString("PUNTAJE", jugador.getPuntaje());
                editorConfig.putString("INTENTOS", jugador.getIntentos());
                editorConfig.commit();
                Toast.makeText(ConfiguracionActivity.this, "Iniciaste sesión como " + sharedPreferences.getString("USER", ""), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfiguracionActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }else{
                Toast.makeText(ConfiguracionActivity.this, "Error al iniciar sesión, usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickbtnCrearUsConfiguracion(View view) {
        Intent intent = new Intent(ConfiguracionActivity.this, CrearUsuarioActivity.class);
        startActivity(intent);
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