package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa una reserva de mesa realizada por un cliente.
 */
public class Reserva implements Serializable {

    /* ---------------- Enum Estado ---------------- */
    public enum Estado {
        ACTIVA,
        CANCELADA,
        ATENDIDA;

        @Override public String toString() {
            return name().substring(0, 1) + name().substring(1).toLowerCase();
        }
    }

    /* ---------------- Campos ---------------- */
    private int id;                     // PK autoincrement
    private String cliente;             // nombre del cliente
    private String contacto;            // teléfono o e‑mail
    private Mesa mesa;                  // mesa reservada
    private LocalDateTime fechaHora;    // fecha y hora de la reserva
    private Estado estado = Estado.ACTIVA;
    private boolean activo = true;      // para soft‑delete

    /* ---------------- Constructores ---------------- */
    public Reserva() { }

    public Reserva(String cliente, String contacto, Mesa mesa, LocalDateTime fechaHora) {
        this.cliente = cliente;
        this.contacto = contacto;
        this.mesa = mesa;
        this.fechaHora = fechaHora;
    }

    public Reserva(int id, String cliente, String contacto, Mesa mesa,
                   LocalDateTime fechaHora, Estado estado, boolean activo) {
        this.id = id;
        this.cliente = cliente;
        this.contacto = contacto;
        this.mesa = mesa;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.activo = activo;
    }

    /* ---------------- Getters & Setters ---------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }

    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ---------------- Lógica ---------------- */
    public void cancelar() { this.estado = Estado.CANCELADA; }
    public void atender()  { this.estado = Estado.ATENDIDA;  }

    /* ---------------- Overrides ---------------- */
    @Override
    public String toString() {
        return "Reserva de " + cliente + " | Mesa " + mesa.getNumero() + " | " + fechaHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva reserva)) return false;
        return id == reserva.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    /* ---------------- Builder ---------------- */
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Reserva r = new Reserva();
        public Builder cliente(String c) { r.cliente = c; return this; }
        public Builder contacto(String c) { r.contacto = c; return this; }
        public Builder mesa(Mesa m) { r.mesa = m; return this; }
        public Builder fechaHora(LocalDateTime f) { r.fechaHora = f; return this; }
        public Reserva build() { return r; }
    }
}
