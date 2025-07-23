package com.mycompany.polloloco.modelo;

/**
 * Modelo que representa una mesa del restaurante.
 */
public class Mesa {

    private int idMesa;
    private int numero;
    private int capacidad;
    private String estado; // Ejemplo: Disponible, Ocupada, Reservada

    public Mesa() {
    }

    public Mesa(int numero) {
        this.numero = numero;
        this.capacidad = 4; // valor por defecto
        this.estado = "Disponible";
    }

    public Mesa(int idMesa, int numero, int capacidad, String estado) {
        this.idMesa = idMesa;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Mesa " + numero + " (" + estado + ")";
    }
}
