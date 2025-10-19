package org.example.Presentation_layer.Controllers;

import org.example.Domain_Layer.Proyecto;
import org.example.Domain_Layer.Tarea;
import org.example.Domain_Layer.Usuario;
import org.example.ServiceLayer.Service;
import org.example.ServiceLayer.ServiceObserver;
import org.example.Utilities.EstadoTarea;
import org.example.Utilities.PrioridadTarea;

import java.util.List;

public class TareaController {
    private final Service<Tarea> service;
    private final Service<Usuario> usuarioService;

    public TareaController(Service<Tarea> service, Service<Usuario> usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    public void agregar(String descripcion, String fechaFinalizacion,
                        PrioridadTarea prioridad, String responsableId, String proyectoCodigo) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (fechaFinalizacion == null || fechaFinalizacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (responsableId == null || responsableId.trim().isEmpty()) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }

        String numero = generarNumero();
        Tarea t = new Tarea(numero, descripcion, fechaFinalizacion,
                prioridad, EstadoTarea.ABIERTA, responsableId, proyectoCodigo);
        service.agregar(t);
    }

    public List<Tarea> leerTodas() {
        List<Tarea> tareas = service.LeerTodo();
        for (Tarea t : tareas) {
            Usuario u = usuarioService.Buscar_porID(t.getResponsableId());
            t.setResponsable(u);
        }
        return tareas;
    }

    public List<Tarea> leerPorProyecto(String proyectoCodigo) {
        return leerTodas().stream()
                .filter(t -> t.getProyectoCodigo().equals(proyectoCodigo))
                .toList();
    }

    public void actualizarTarea(Tarea tarea) {
        service.actualizar(tarea);
    }

    public void Observer(ServiceObserver<Tarea> observer) {
        service.Observer(observer);
    }

    private String generarNumero() {
        return "TSK-" + System.currentTimeMillis();
    }
}
