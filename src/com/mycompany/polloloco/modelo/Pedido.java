package com.mycompany.polloloco.modelo;

import java.util.Date;
import java.util.List;

/**
 * Modelo que representa un pedido de restaurante.
 */
public class Pedido {

    private int id;
    private int idMesa;
    private int idUsuario;
    private Date fecha;
    private double total;
    private List<DetallePedido> detalle;

    public Pedido() {
    }

    public Pedido(int id, int idMesa, int idUsuario, Date fecha, double total, List<DetallePedido> detalle) {
        this.id = id;
        this.idMesa = idMesa;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.total = total;
        this.detalle = detalle;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetallePedido> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetallePedido> detalle) {
        this.detalle = detalle;
    }

    public double calcularTotal() {
        double suma = 0.0;
        if (detalle != null) {
            for (DetallePedido d : detalle) {
                suma += d.getCantidad() * d.getPrecioUnitario();
            }
        }
        return suma;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - Mesa " + idMesa + " - Total: S/. " + total;
    }
}
