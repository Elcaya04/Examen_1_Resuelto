package org.example.Domain_Layer;

import jakarta.xml.bind.annotation.*;
import org.example.Utilities.Encargados;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="proyecto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Proyecto {
    @XmlElement(name = "codigo")
    private String codigo;

    @XmlElement(name = "descripcion")
    private String descripcion;

    @XmlElement(name = "encargadoId")
    private String encargadoId;

    @XmlTransient
    private Usuario encargado;

    @XmlTransient
    private List<Tarea> tareas = new ArrayList<>();

    public Proyecto() {}

    public Proyecto(String codigo, String descripcion, String encargadoId) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.encargadoId = encargadoId;
        this.tareas = new ArrayList<>();
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEncargadoId() { return encargadoId; }
    public void setEncargadoId(String encargadoId) { this.encargadoId = encargadoId; }

    public Usuario getEncargado() { return encargado; }
    public void setEncargado(Usuario encargado) { this.encargado = encargado; }

    public List<Tarea> getTareas() { return new ArrayList<>(tareas); }
    public void setTareas(List<Tarea> tareas) {
        this.tareas = new ArrayList<>(tareas != null ? tareas : List.of());
    }

    @Override
    public String toString() { return codigo + " - " + descripcion; }

}
