package com.mycompany.polloloco.modelo;

import java.util.Date;

/**
 * Modelo que representa una reserva de mesa en el restaurante.
 */
public class Reserva {

    private int idReserva;
    private String cliente;
    private String contacto;
    private Mesa mesa;
    private Date fechaHora;
    private String estado; // Activa, Cancelada, Atendida, etc.

    public Reserva() {
    }

    public Reserva(String cliente, Mesa mesa, Date fechaHora) {
        this.cliente = cliente;
        this.mesa = mesa;
        this.fechaHora = fechaHora;
        this.estado = "Activa";
    }

    public Reserva(int idReserva, String cliente, String contacto, Mesa mesa, Date fechaHora, String estado) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.contacto = contacto;
        this.mesa = mesa;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Reserva de " + cliente + " para mesa " + mesa.getNumero() + " a las " + fechaHora;
    }
}
