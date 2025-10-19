package org.example.ServiceLayer;

import org.example.Data_Acess_Layer.FileStore;
import org.example.Domain_Layer.Proyecto;
import org.example.Utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class ProyectoService implements Service<Proyecto> {
    private final FileStore<Proyecto> fileStore;
    private final List<ServiceObserver<Proyecto>> observers = new ArrayList<>();

    public ProyectoService(FileStore<Proyecto> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String nombre) {

    }

    @Override
    public void agregar(Proyecto entity) {
        validar(entity);
        List<Proyecto> proyectos = fileStore.Leer();
        proyectos.add(entity);
        fileStore.Escribir(proyectos);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String id) {
        List<Proyecto> proyectos = fileStore.Leer();
        proyectos.removeIf(p -> p.getCodigo().equals(id));
        fileStore.Escribir(proyectos);
    }

    @Override
    public void actualizar(Proyecto entity) {
        validar(entity);
        List<Proyecto> proyectos = fileStore.Leer();
        for (int i = 0; i < proyectos.size(); i++) {
            if (proyectos.get(i).getCodigo().equals(entity.getCodigo())) {
                proyectos.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(proyectos);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<Proyecto> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Proyecto LeerID(String id) {
        return null;
    }

    @Override
    public Proyecto Buscar_porID(String id) {
        return fileStore.Leer().stream()
                .filter(p -> p.getCodigo().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Proyecto> observer) {
        if (observer != null) observers.add(observer);
    }

    private void notifyObservers(ChangeType type, Proyecto entity) {
        for (ServiceObserver<Proyecto> o : observers) o.DataChanged(type, entity);
    }

    private void validar(Proyecto p) {
        if (p.getDescripcion() == null || p.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (p.getEncargadoId() == null || p.getEncargadoId().trim().isEmpty()) {
            throw new IllegalArgumentException("El encargado es obligatorio");
        }
    }
}
