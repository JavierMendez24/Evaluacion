package personal.app.ejercicio_guia_7.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "jugadores")
public class JugadorModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String idImg, usuario, contrasenia, puntaje, intentos;

    public JugadorModel() {
    }

    public JugadorModel(int id, String idImg, String usuario, String contrasenia, String puntaje, String intentos) {
        this.id = id;
        this.idImg = idImg;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.puntaje = puntaje;
        this.intentos = intentos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdImg() {
        return idImg;
    }

    public void setIdImg(String idImg) {
        this.idImg = idImg;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    public String getIntentos() {
        return intentos;
    }

    public void setIntentos(String intentos) {
        this.intentos = intentos;
    }
}
