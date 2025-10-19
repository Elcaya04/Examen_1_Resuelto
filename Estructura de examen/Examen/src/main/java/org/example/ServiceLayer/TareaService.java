package org.example.ServiceLayer;

import org.example.Data_Acess_Layer.FileStore;
import org.example.Domain_Layer.Tarea;
import org.example.Utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class TareaService implements Service<Tarea> {
    private final FileStore<Tarea> fileStore;
    private final List<ServiceObserver<Tarea>> observers = new ArrayList<>();

    public TareaService(FileStore<Tarea> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String nombre) {

    }

    @Override
    public void agregar(Tarea entity) {
        validar(entity);
        List<Tarea> tareas = fileStore.Leer();
        tareas.add(entity);
        fileStore.Escribir(tareas);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String id) {
        List<Tarea> tareas = fileStore.Leer();
        tareas.removeIf(t -> t.getNumero().equals(id));
        fileStore.Escribir(tareas);
    }

    @Override
    public void actualizar(Tarea entity) {
        validar(entity);
        List<Tarea> tareas = fileStore.Leer();
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).getNumero().equals(entity.getNumero())) {
                tareas.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(tareas);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public Tarea LeerID(String id) {
        return null;
    }

    @Override
    public List<Tarea> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Tarea Buscar_porID(String id) {
        return fileStore.Leer().stream()
                .filter(t -> t.getNumero().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Tarea> observer) {
        if (observer != null) observers.add(observer);
    }

    private void notifyObservers(ChangeType type, Tarea entity) {
        for (ServiceObserver<Tarea> o : observers) o.DataChanged(type, entity);
    }

    private void validar(Tarea t) {
        if (t.getDescripcion() == null || t.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (t.getFechaFinalizacion() == null || t.getFechaFinalizacion().trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (t.getResponsableId() == null || t.getResponsableId().trim().isEmpty()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        if (t.getProyectoCodigo() == null || t.getProyectoCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El proyecto es obligatorio");
        }
    }
}
