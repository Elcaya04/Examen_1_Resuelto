package org.example.Presentation_layer.Controllers;

import org.example.Domain_Layer.Usuario;
import org.example.ServiceLayer.Service;

import java.util.List;

public class UsuarioController {
    private final Service<Usuario> service;

    public UsuarioController(Service<Usuario> service) {
        this.service = service;
    }

    public List<Usuario> leerTodos() {
        return service.LeerTodo();
    }

    public Usuario buscarPorId(String id) {
        return service.Buscar_porID(id);
    }
}
