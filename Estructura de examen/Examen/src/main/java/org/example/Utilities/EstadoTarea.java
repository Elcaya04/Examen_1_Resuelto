package org.example.Utilities;

public enum EstadoTarea {
    ABIERTA("Abierta"),
    EN_PROGRESO("En-progreso"),
    EN_REVISION("En-revisión"),
    RESUELTA("Resuelta");

    private final String descripcion;

    EstadoTarea(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return descripcion; }
}
