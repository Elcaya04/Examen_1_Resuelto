package org.example.ServiceLayer;

import org.example.Data_Acess_Layer.FileStore;
import org.example.Domain_Layer.Usuario;
import org.example.Utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService implements Service<Usuario>{
    private final FileStore<Usuario> fileStore;
    private final List<ServiceObserver<Usuario>> observers = new ArrayList<>();

    public UsuarioService(FileStore<Usuario> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void agregar(Usuario entity) {}

    @Override
    public void eliminar(String id) {}

    @Override
    public void actualizar(Usuario entity) {}

    @Override
    public List<Usuario> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Usuario LeerID(String id) {
        return null;
    }

    @Override
    public void buscar(String nombre) {};
    @Override
    public Usuario Buscar_porID(String id) {
        return fileStore.Leer().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Usuario> observer) {
        if (observer != null) observers.add(observer);
    }

    private void notifyObservers(ChangeType type, Usuario entity) {
        for (ServiceObserver<Usuario> o : observers) o.DataChanged(type, entity);
    }
}
