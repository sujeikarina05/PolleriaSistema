package com.mycompany.polloloco.modelo;

public class Reserva {
    private String cliente;
    private Mesa mesa;

    public Reserva(String cliente, Mesa mesa) {
        this.cliente = cliente;
        this.mesa = mesa;
    }

    public String getCliente() { return cliente; }
    public Mesa getMesa() { return mesa; }
}
