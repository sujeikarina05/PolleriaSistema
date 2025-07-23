package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/** Modelo que representa un turno de trabajo (ej. mañana, tarde, noche). */
public class Turno implements Comparable<Turno>, Serializable {

    private int idTurno;
    private String descripcion;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean activo = true;

    public Turno() {}

    public Turno(int idTurno, String descripcion,
                 LocalTime horaInicio, LocalTime horaFin) {
        this.idTurno = idTurno;
        this.descripcion = descripcion;
        setHoraInicio(horaInicio);
        setHoraFin(horaFin);
    }

    /* ---------- getters / setters ---------- */
    public int getIdTurno() { return idTurno; }
    public void setIdTurno(int idTurno) { this.idTurno = idTurno; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
        validarHoras();
    }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
        validarHoras();
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ---------- utilidades ---------- */
    /** Duración del turno. */
    public Duration duracion() {
        return Duration.between(horaInicio, horaFin);
    }

    /** Orden natural por hora de inicio. */
    @Override public int compareTo(Turno o) {
        return this.horaInicio.compareTo(o.horaInicio);
    }

    @Override public String toString() {
        return descripcion + " (" + horaInicio + "‑" + horaFin + ")";
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Turno t) && t.idTurno == idTurno;
    }
    @Override public int hashCode() { return Objects.hash(idTurno); }

    /* ---------- validación interna ---------- */
    private void validarHoras() {
        if (horaInicio != null && horaFin != null &&
            !horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("horaFin debe ser posterior a horaInicio");
        }
    }
}
