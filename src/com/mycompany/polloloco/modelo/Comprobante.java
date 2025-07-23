package com.mycompany.polloloco.modelo;

import java.util.Date;

/**
 * Modelo que representa un comprobante de pago.
 */
public class Comprobante {

    private int idComprobante;
    private int numero;
    private Date fechaEmision;
    private String tipo; // Boleta, Factura, etc.
    private int idPedido;
    private double total;
    private String nombreCliente;
    private String rucDni;
    private String metodoPago;

    public Comprobante() {
    }

    public Comprobante(int numero, Date fechaEmision, String tipo, int idPedido, double total,
                       String nombreCliente, String rucDni, String metodoPago) {
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.tipo = tipo;
        this.idPedido = idPedido;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.rucDni = rucDni;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters
    public int getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(int idComprobante) {
        this.idComprobante = idComprobante;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getRucDni() {
        return rucDni;
    }

    public void setRucDni(String rucDni) {
        this.rucDni = rucDni;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
