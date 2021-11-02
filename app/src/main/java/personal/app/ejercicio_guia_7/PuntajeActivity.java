package personal.app.ejercicio_guia_7;

import static personal.app.ejercicio_guia_7.MainActivity.NAME_FILE;
import static personal.app.ejercicio_guia_7.MainActivity.randomNum;
import static personal.app.ejercicio_guia_7.MainActivity.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class PuntajeActivity extends AppCompatActivity {

    private TextView tvUsuarioPuntaje, tvPuntjaNumeroPuntaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntaje);
        centerTitle();
        setTitle("Puntaje");
        tvUsuarioPuntaje = findViewById(R.id.tvUsuarioPuntaje);
        tvPuntjaNumeroPuntaje = findViewById(R.id.tvPuntjaNumeroPuntaje);

        tvUsuarioPuntaje.setText(sharedPreferences.getString("USER", ""));
        tvPuntjaNumeroPuntaje.setText(sharedPreferences.getString("PUNTAJE", ""));
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