package personal.app.ejercicio_guia_7.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "imagenes")
public class ImagenModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String url;

    public ImagenModel() {
    }

    public ImagenModel(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
