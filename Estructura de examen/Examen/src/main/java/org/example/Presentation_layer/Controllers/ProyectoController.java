package org.example.Presentation_layer.Controllers;

import org.example.Domain_Layer.Proyecto;
import org.example.Domain_Layer.Usuario;
import org.example.ServiceLayer.Service;
import org.example.ServiceLayer.ServiceObserver;


import java.util.List;

public class ProyectoController {
    private final Service<Proyecto> service;
    private final Service<Usuario> usuarioService;

    public ProyectoController(Service<Proyecto> service, Service<Usuario> usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    public void agregar(String descripcion, String encargadoId) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (encargadoId == null || encargadoId.trim().isEmpty()) {
            throw new IllegalArgumentException("El encargado es obligatorio");
        }

        String codigo = generarCodigo();
        Proyecto p = new Proyecto(codigo, descripcion, encargadoId);
        service.agregar(p);
    }

    public List<Proyecto> leerTodos() {
        List<Proyecto> proyectos = service.LeerTodo();
        for (Proyecto p : proyectos) {
            Usuario u = usuarioService.Buscar_porID(p.getEncargadoId());
            p.setEncargado(u);
        }
        return proyectos;
    }

    public void Observer(ServiceObserver<Proyecto> observer) {
        service.Observer(observer);
    }

    private String generarCodigo() {
        return "PRY-" + System.currentTimeMillis();
    }
}
