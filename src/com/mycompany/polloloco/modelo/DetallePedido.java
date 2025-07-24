package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la línea de detalle de un {@link Pedido}: un producto, su cantidad
 * y el precio unitario al momento de la venta.
 */
public class DetallePedido implements Serializable {

    private int id;            // PK autoincrement
    private int idPedido;      // FK -> pedido(id)
    private Producto producto; // Producto asociado
    private int cantidad;
    private double precioUnitario;

    /* --------------- Constructores --------------- */
    public DetallePedido() { }

    public DetallePedido(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    /* --------------- Getter / Setter --------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser > 0");
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) {
        if (precioUnitario < 0) throw new IllegalArgumentException("Precio no puede ser negativo");
        this.precioUnitario = precioUnitario;
    }

    /* --------------- Lógica de dominio --------------- */
    public double getSubtotal() { return cantidad * precioUnitario; }

    /* --------------- Utilidades --------------- */
    @Override public String toString() {
        return producto.getNombre() + " × " + cantidad + " = S/ " + getSubtotal();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetallePedido that)) return false;
        return id == that.id;
    }

    @Override public int hashCode() { return Objects.hash(id); }
}
