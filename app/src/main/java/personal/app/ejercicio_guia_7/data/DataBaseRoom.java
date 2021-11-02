package personal.app.ejercicio_guia_7.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import personal.app.ejercicio_guia_7.dao.ImagenDao;
import personal.app.ejercicio_guia_7.dao.JugadorDao;
import personal.app.ejercicio_guia_7.models.ImagenModel;
import personal.app.ejercicio_guia_7.models.JugadorModel;

@Database(entities = {JugadorModel.class, ImagenModel.class}, version = 1)
public  abstract  class DataBaseRoom extends RoomDatabase {
    public abstract JugadorDao jugadorDao();
    public abstract ImagenDao imagenDao();
}
