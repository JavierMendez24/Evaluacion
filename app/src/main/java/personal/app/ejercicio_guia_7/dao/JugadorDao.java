package personal.app.ejercicio_guia_7.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import personal.app.ejercicio_guia_7.models.JugadorModel;

@Dao
public interface JugadorDao {

    @Query("select * from jugadores")
    public List<JugadorModel> getAll();

    @Query("select * from jugadores where id = :id")
    public JugadorModel get(int id);

    @Query("select * from jugadores where usuario = :usuario and contrasenia = :contrasenia")
    public JugadorModel iniciarSesion(String usuario, String contrasenia);

    @Query("select * from jugadores where usuario = :usuario")
    public JugadorModel getIdUsuario(String usuario);

    @Insert
    public void save(JugadorModel entity);

    @Delete
    public void delete(JugadorModel entity);

    @Update
    public void update(JugadorModel entity);
}


