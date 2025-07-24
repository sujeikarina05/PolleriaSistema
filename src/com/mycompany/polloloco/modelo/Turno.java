package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa un turno de trabajo (mañana, tarde, noche) dentro del restaurante.
 */
public class Turno implements Comparable<Turno>, Serializable {

    private int id;                 // PK autoincrement
    private String descripcion;     // "Turno Mañana", etc.
    private LocalTime horaInicio;   // inclusive
    private LocalTime horaFin;      // exclusive
    private boolean activo = true;  // soft‑delete

    /* ---------------- Constructores ---------------- */
    public Turno() {}

    public Turno(int id, String descripcion, LocalTime horaInicio, LocalTime horaFin) {
        this.id = id;
        this.descripcion = descripcion;
        setHoraInicio(horaInicio);
        setHoraFin(horaFin);
    }

    /* ---------------- Builder pattern ---------------- */
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Turno t = new Turno();
        public Builder id(int id) { t.setId(id); return this; }
        public Builder descripcion(String d) { t.setDescripcion(d); return this; }
        public Builder inicio(LocalTime h) { t.setHoraInicio(h); return this; }
        public Builder fin(LocalTime h) { t.setHoraFin(h); return this; }
        public Builder activo(boolean a) { t.setActivo(a); return this; }
        public Turno build() { return t; }
    }

    /* ---------------- Getters / setters ---------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().length() < 3) {
            throw new IllegalArgumentException("La descripción debe tener al menos 3 caracteres");
        }
        this.descripcion = descripcion.trim();
    }

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

    /* ---------------- Utilidades ---------------- */
    public Duration duracion() { return Duration.between(horaInicio, horaFin); }

    public boolean cubre(LocalTime hora) {
        return !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
    }

    public Turno desactivar() { this.activo = false; return this; }

    /* Orden natural por hora de inicio */
    @Override public int compareTo(Turno o) { return this.horaInicio.compareTo(o.horaInicio); }

    /* ---------------- Overrides ---------------- */
    @Override public String toString() { return descripcion + " (" + horaInicio + "‑" + horaFin + ")"; }

    @Override public boolean equals(Object o) { return (o instanceof Turno t) && t.id == id; }

    @Override public int hashCode() { return Objects.hash(id); }

    /* ---------------- Validación interna ---------------- */
    private void validarHoras() {
        if (horaInicio != null && horaFin != null && !horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("horaFin debe ser posterior a horaInicio");
        }
    }
}
