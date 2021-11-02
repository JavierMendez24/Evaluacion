package personal.app.ejercicio_guia_7.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import personal.app.ejercicio_guia_7.models.ImagenModel;

@Dao
public interface ImagenDao {

    @Query("select * from imagenes")
    public List<ImagenModel> getAll();

    @Query("select * from imagenes where id = :id")
    public ImagenModel get(int id);

    @Query("select * from imagenes where url = :url")
    public ImagenModel getIdImg(String url);

    @Insert
    public void save(ImagenModel entity);

    @Delete
    public void delete(ImagenModel entity);

    @Update
    public void update(ImagenModel entity);
}


