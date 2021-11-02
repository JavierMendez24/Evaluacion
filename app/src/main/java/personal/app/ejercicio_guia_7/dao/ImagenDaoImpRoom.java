package personal.app.ejercicio_guia_7.dao;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import personal.app.ejercicio_guia_7.data.DataBaseRoom;
import personal.app.ejercicio_guia_7.models.ImagenModel;

public class ImagenDaoImpRoom implements ImagenDao {

    DataBaseRoom db;

    ImagenDao dao;

    public ImagenDaoImpRoom(Context context){
        db= Room.databaseBuilder(context,DataBaseRoom.class,"db")
                .allowMainThreadQueries().build();
        dao = db.imagenDao();
    }

    @Override
    public List<ImagenModel> getAll() {
        return dao.getAll();
    }

    @Override
    public ImagenModel get(int id) {
        return dao.get(id);
    }

    @Override
    public ImagenModel getIdImg(String url) {
        return dao.getIdImg(url);
    }

    @Override
    public void save(ImagenModel entity) {
            dao.save(entity);
    }

    @Override
    public void delete(ImagenModel entity) {
        dao.delete(entity);
    }

    @Override
    public void update(ImagenModel entity) {
        dao.update(entity);
    }

}
