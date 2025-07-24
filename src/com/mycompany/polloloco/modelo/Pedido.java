package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cabecera y detalle de un pedido.
 */
public class Pedido implements Serializable {

    /* ---------------- Enum Estado ---------------- */
    public enum Estado {
        PENDIENTE,
        PREPARACION,
        ENTREGADO,
        PAGADO,
        ANULADO;

        @Override public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    /* ---------------- Atributos ---------------- */
    private int id;
    private int idMesa;
    private int idUsuario;
    private LocalDateTime fechaPedido = LocalDateTime.now();
    private Estado estado = Estado.PENDIENTE;
    private double total;
    private final List<DetallePedido> detalle = new ArrayList<>();

    /* ---------------- Constructores ---------------- */
    public Pedido() { }

    public Pedido(int idMesa, int idUsuario) {
        this.idMesa = idMesa;
        this.idUsuario = idUsuario;
    }

    /* ---------------- Builder ---------------- */
    public static Pedido builder(int idMesa, int idUsuario) {
        return new Pedido(idMesa, idUsuario);
    }

    /* ---------------- Métodos de negocio ---------------- */
    public void addDetalle(DetallePedido d) {
        Objects.requireNonNull(d, "Detalle null");
        detalle.add(d);
        total += d.getSubtotal();
    }

    public void recalcularTotal() {
        total = detalle.stream().mapToDouble(DetallePedido::getSubtotal).sum();
    }

    /* ---------------- Getters / Setters ---------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<DetallePedido> getDetalle() { return List.copyOf(detalle); }

    /* ---------------- toString / equals ---------------- */
    @Override public String toString() {
        return "Pedido #" + id + " Mesa=" + idMesa + " Total=" + total;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;
        return id == pedido.id;
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
