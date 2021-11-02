package personal.app.ejercicio_guia_7;

import static personal.app.ejercicio_guia_7.MainActivity.intentos;
import static personal.app.ejercicio_guia_7.MainActivity.puntaje;
import static personal.app.ejercicio_guia_7.MainActivity.randomNum;
import static personal.app.ejercicio_guia_7.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Random;

import personal.app.ejercicio_guia_7.dao.JugadorDao;
import personal.app.ejercicio_guia_7.dao.JugadorDaoImpRoom;
import personal.app.ejercicio_guia_7.models.JugadorModel;

public class JugarActivity extends AppCompatActivity {

    private TextView tvNombreUsuarioJugar, tvPuntajeTotal, tvIntentosNumeroJugar;
    private EditText etNumeroJugar;
    private JugadorDao jugadorDao;
    private JugadorModel jugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);
        centerTitle();
        setTitle("Jugar");
        tvNombreUsuarioJugar = findViewById(R.id.tvNombreUsuarioJugar);
        tvPuntajeTotal = findViewById(R.id.tvPuntajeTotal);
        tvIntentosNumeroJugar = findViewById(R.id.tvIntentosNumeroJugar);
        etNumeroJugar = findViewById(R.id.etNumeroJugar);
        tvNombreUsuarioJugar.setText(sharedPreferences.getString("USER", ""));
        tvPuntajeTotal.setText("Puntaje total: "+sharedPreferences.getString("PUNTAJE", ""));
        tvIntentosNumeroJugar.setText(sharedPreferences.getString("INTENTOS", ""));
        jugadorDao = new JugadorDaoImpRoom(getApplicationContext());
    }

    public void onClickAdivinar(View view) {
        try {

            SharedPreferences.Editor editorConfig = sharedPreferences.edit();
            if (String.valueOf(randomNum).equals(etNumeroJugar.getText().toString())) {
                puntaje = puntaje + 10;
                intentos++;
                editorConfig.putString("PUNTAJE", String.valueOf(puntaje));
                editorConfig.putString("INTENTOS", String.valueOf(intentos));
                editorConfig.commit();
                newRandomNumber();
                actualizarUsuario();
                Toast.makeText(JugarActivity.this, "Intento acertado", Toast.LENGTH_SHORT).show();
            } else {
                puntaje = puntaje - 1;
                intentos++;
                editorConfig.putString("PUNTAJE", String.valueOf(puntaje));
                editorConfig.putString("INTENTOS", String.valueOf(intentos));
                editorConfig.commit();
                actualizarUsuario();
                Toast.makeText(JugarActivity.this, "Intento fallido", Toast.LENGTH_SHORT).show();
            }
            tvPuntajeTotal.setText("Puntaje total: " + sharedPreferences.getString("PUNTAJE", ""));
            tvIntentosNumeroJugar.setText(sharedPreferences.getString("INTENTOS", ""));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void newRandomNumber(){
        Random rand = new Random();
        randomNum = rand.nextInt((10 - 1) + 1) + 1;
    }

    public void actualizarUsuario(){
        jugador = new JugadorModel();
        jugador.setId(Integer.parseInt(sharedPreferences.getString("ID", "")));
        jugador.setIdImg(sharedPreferences.getString("IDIMG", ""));
        jugador.setUsuario(sharedPreferences.getString("USER", ""));
        jugador.setContrasenia(sharedPreferences.getString("PASS", ""));
        jugador.setPuntaje(sharedPreferences.getString("PUNTAJE", ""));
        jugador.setIntentos(sharedPreferences.getString("INTENTOS", ""));
        jugadorDao.update(jugador);
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