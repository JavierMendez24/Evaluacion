package personal.app.ejercicio_guia_7.utils;

import android.widget.EditText;

public class Utils {

    public static boolean verifyEditText(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Campo vacío");
            return false;
        }
        return true;
    }
}
