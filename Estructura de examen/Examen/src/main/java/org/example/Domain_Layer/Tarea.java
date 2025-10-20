package org.example.Domain_Layer;

import jakarta.xml.bind.annotation.*;
import org.example.Utilities.EstadoTarea;
import org.example.Utilities.PrioridadTarea;

@XmlRootElement(name="tarea")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tarea {
    @XmlElement(name = "numero")
    private String numero;

    @XmlElement(name = "descripcion")
    private String descripcion;

    @XmlElement(name = "fechaFinalizacion")
    private String fechaFinalizacion;

    @XmlElement(name = "prioridad")
    private String prioridad;

    @XmlElement(name = "estado")
    private String estado;

    @XmlElement(name = "responsableId")
    private String responsableId;

    @XmlElement(name = "proyectoCodigo")
    private String proyectoCodigo;

    @XmlTransient
    private Usuario responsable;

    public Tarea() {}

    public Tarea(String numero, String descripcion, String fechaFinalizacion,
                 PrioridadTarea prioridad, EstadoTarea estado, String responsableId, String proyectoCodigo) {
        this.numero = numero;
        this.descripcion = descripcion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.prioridad = prioridad.toString();
        this.estado = estado.toString();
        this.responsableId = responsableId;
        this.proyectoCodigo = proyectoCodigo;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(String fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public PrioridadTarea getPrioridad() {
        if (prioridad == null) return PrioridadTarea.MEDIA;

        for (PrioridadTarea p : PrioridadTarea.values()) {
            if (p.getDescripcion().equals(prioridad) || p.name().equals(prioridad)) {
                return p;
            }
        }
        return PrioridadTarea.MEDIA;
        }


    public void setPrioridad(PrioridadTarea prioridad) {
        this.prioridad = prioridad.toString();
    }

    public EstadoTarea getEstado() {
        if (estado == null) return EstadoTarea.ABIERTA;

        for (EstadoTarea e : EstadoTarea.values()) {
            if (e.getDescripcion().equals(estado) || e.name().equals(estado)) {
                return e;
            }
        }
        return EstadoTarea.ABIERTA;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado.toString();
    }

    public String getResponsableId() { return responsableId; }
    public void setResponsableId(String responsableId) { this.responsableId = responsableId; }

    public Usuario getResponsable() { return responsable; }
    public void setResponsable(Usuario responsable) { this.responsable = responsable; }

    public String getProyectoCodigo() { return proyectoCodigo; }
    public void setProyectoCodigo(String proyectoCodigo) { this.proyectoCodigo = proyectoCodigo; }

    @Override
    public String toString() { return numero + " - " + descripcion; }
}
