package com.mycompany.polloloco.modelo;

/**
 * Modelo que representa un método de pago (efectivo, tarjeta, Yape, etc.).
 */
public class MetodoPago {

    private int idMetodoPago;
    private String descripcion;

    public MetodoPago() {
    }

    public MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public MetodoPago(int idMetodoPago, String descripcion) {
        this.idMetodoPago = idMetodoPago;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
