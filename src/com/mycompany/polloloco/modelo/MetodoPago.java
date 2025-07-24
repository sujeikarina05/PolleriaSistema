package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa los métodos de pago admitidos por la pollería.
 */
public class MetodoPago implements Serializable {

    /* ---------------- Enum embebido ---------------- */
    public enum Tipo {
        EFECTIVO,
        TARJETA,
        YAPE,
        PLIN,
        TRANSFERENCIA,
        PAYPAL,
        OTRO;

        @Override public String toString() {
            return switch (this) {
                case EFECTIVO      -> "Efectivo";
                case TARJETA       -> "Tarjeta";
                case YAPE          -> "Yape";
                case PLIN          -> "Plin";
                case TRANSFERENCIA -> "Transferencia";
                case PAYPAL        -> "PayPal";
                case OTRO          -> "Otro";
            };
        }
    }

    /* ---------------- Atributos ---------------- */
    private int id;          // PK autoincrement
    private Tipo tipo;       // Enum en lugar de String
    private boolean activo = true;

    /* ---------------- Constructores ---------------- */
    public MetodoPago() { }

    public MetodoPago(Tipo tipo) {
        this.tipo = Objects.requireNonNull(tipo);
    }

    public MetodoPago(int id, Tipo tipo, boolean activo) {
        this.id = id;
        this.tipo = Objects.requireNonNull(tipo);
        this.activo = activo;
    }

    /* ---------------- Setters / Getters ---------------- */
    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public Tipo getTipo()            { return tipo; }
    public void setTipo(Tipo tipo)   { this.tipo = Objects.requireNonNull(tipo); }

    public boolean isActivo()        { return activo; }
    public void setActivo(boolean a) { this.activo = a; }

    /* ---------------- Helpers ---------------- */
    public MetodoPago desactivar() { this.activo = false; return this; }
    public MetodoPago reactivar()  { this.activo = true;  return this; }

    @Override
    public String toString() {
        return tipo.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetodoPago m)) return false;
        return id == m.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}