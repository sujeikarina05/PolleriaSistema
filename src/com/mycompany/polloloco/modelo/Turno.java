package com.mycompany.polloloco.modelo;

import java.time.LocalTime;

/**
 * Modelo que representa un turno de trabajo (ej. mañana, tarde, noche).
 */
public class Turno {

    private int idTurno;
    private String descripcion;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Turno() {
    }

    public Turno(int idTurno, String descripcion, LocalTime horaInicio, LocalTime horaFin) {
        this.idTurno = idTurno;
        this.descripcion = descripcion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Turno(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return descripcion + " (" + horaInicio + " - " + horaFin + ")";
    }
}
