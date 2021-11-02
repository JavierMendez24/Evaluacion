package personal.app.ejercicio_guia_7.dao;

import android.content.Context;

import androidx.room.Room;

import java.util.List;

import personal.app.ejercicio_guia_7.data.DataBaseRoom;
import personal.app.ejercicio_guia_7.models.JugadorModel;

public class JugadorDaoImpRoom implements JugadorDao {

    DataBaseRoom db;

    JugadorDao dao;

    public JugadorDaoImpRoom(Context context){
        db= Room.databaseBuilder(context,DataBaseRoom.class,"db")
                .allowMainThreadQueries().build();
        dao = db.jugadorDao();
    }

    @Override
    public List<JugadorModel> getAll() {
        return dao.getAll();
    }

    @Override
    public JugadorModel get(int id) {
        return dao.get(id);
    }

    @Override
    public JugadorModel iniciarSesion(String usuario, String contrasenia) {
        return dao.iniciarSesion(usuario, contrasenia);
    }

    @Override
    public JugadorModel getIdUsuario(String usuario) {
        return dao.getIdUsuario(usuario);
    }

    @Override
    public void save(JugadorModel entity) {
        dao.save(entity);
    }

    @Override
    public void delete(JugadorModel entity) {
        dao.delete(entity);
    }

    @Override
    public void update(JugadorModel entity) {
        dao.update(entity);
    }

}
