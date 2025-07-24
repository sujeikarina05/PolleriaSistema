package com.mycompany.polloloco.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Pedido (cabecera + detalle).
 */
public class Pedido {

    private int id;
    private int idMesa;
    private int idUsuario;
    private LocalDateTime fechaPedido;
    private double total;

    /** Detalle de productos solicitados. */
    private List<DetallePedido> detalle = new ArrayList<>();

    /* ---------- Constructores ---------- */

    public Pedido() {
        this.fechaPedido = LocalDateTime.now();
    }

    public Pedido(int idMesa, int idUsuario, List<DetallePedido> detalle) {
        this();
        this.idMesa     = idMesa;
        this.idUsuario  = idUsuario;
        this.detalle    = detalle;
        recalcularTotal();
    }

    /* ---------- Getters & Setters ---------- */

    public int getId()                 { return id; }
    public void setId(int id)          { this.id = id; }

    public int getIdMesa()             { return idMesa; }
    public void setIdMesa(int idMesa)  { this.idMesa = idMesa; }

    public int getIdUsuario()                { return idUsuario; }
    public void setIdUsuario(int idUsuario)  { this.idUsuario = idUsuario; }

    public LocalDateTime getFechaPedido()              { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fecha)    { this.fechaPedido = fecha; }

    public double getTotal()             { return total; }
    public void setTotal(double total)   { this.total = total; }

    public List<DetallePedido> getDetalle()                 { return detalle; }
    public void setDetalle(List<DetallePedido> detalle)     {
        this.detalle = detalle;
        recalcularTotal();
    }

    /* ---------- Lógica ---------- */

    /** Agrega una línea al detalle y actualiza el total. */
    public void addDetalle(DetallePedido det) {
        detalle.add(det);
        total += det.getSubtotal();
    }

    /** Recalcula el valor total a partir de las líneas de detalle. */
    public void recalcularTotal() {
        total = 0;
        for (DetallePedido d : detalle) {
            total += d.getSubtotal();
        }
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " | mesa=" + idMesa + " | total=" + total;
    }
}
