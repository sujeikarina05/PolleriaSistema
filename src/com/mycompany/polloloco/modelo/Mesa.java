package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa una mesa física dentro del restaurante.
 */
public class Mesa implements Serializable {

    /* ---------------- Enum ---------------- */
    public enum Estado {
        LIBRE,
        OCUPADA,
        RESERVADA,
        INACTIVA;

        @Override public String toString() {
            return switch (this) {
                case LIBRE -> "Libre";
                case OCUPADA -> "Ocupada";
                case RESERVADA -> "Reservada";
                case INACTIVA -> "Inactiva";
            };
        }
    }

    /* -------------- Campos -------------- */
    private int id;           // PK autoincrement
    private int numero;       // número visible para el mozo
    private int capacidad;    // nº de comensales
    private Estado estado;    // estado actual

    /* -------------- Constructores -------------- */
    public Mesa() { this.estado = Estado.LIBRE; }

    public Mesa(int numero) {
        this.numero = numero;
        this.capacidad = 4;
        this.estado = Estado.LIBRE;
    }

    public Mesa(int id, int numero, int capacidad, Estado estado) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado == null ? Estado.LIBRE : estado;
    }

    /* -------------- Getters/Setters -------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    /* -------------- Utilidades -------------- */
    public boolean estaLibre() { return estado == Estado.LIBRE; }
    public boolean estaOcupada() { return estado == Estado.OCUPADA; }
    public boolean estaReservada() { return estado == Estado.RESERVADA; }

    @Override
    public String toString() {
        return "Mesa " + numero + " (" + estado + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mesa mesa)) return false;
        return id == mesa.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
