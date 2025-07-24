package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad que representa un producto del menú de la pollería.
 */
public class Producto implements Serializable {

    private int id;               // PK autoincrement
    private String nombre;        // ≥ 3 caracteres
    private String descripcion;   // opcional
    private double precio;        // ≥ 0.01
    private int stock;            // ≥ 0
    private int idCategoria;      // FK -> categoria_producto(id)

    private boolean activo = true; // soft‑delete

    /* ------------------- Constructores ------------------- */
    public Producto() {}

    public Producto(int id, String nombre, String descripcion,
                    double precio, int stock, int idCategoria) {
        this.id = id;
        setNombre(nombre);
        this.descripcion = descripcion;
        setPrecio(precio);
        setStock(stock);
        this.idCategoria = idCategoria;
    }

    public static Producto builder() { return new Producto(); }

    /* ------------------- Getters / Setters ------------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        this.nombre = nombre.trim();
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor que cero");
        this.precio = precio;
    }

    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        this.stock = stock;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ------------------- Utilidades ------------------- */
    public void incrementarStock(int cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("Cantidad negativa");
        stock += cantidad;
    }

    public void decrementarStock(int cantidad) {
        if (cantidad < 0 || cantidad > stock) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        stock -= cantidad;
    }

    @Override public String toString() {
        return nombre + " – S/. " + String.format("%.2f", precio);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto p)) return false;
        return id == p.id;
    }

    @Override public int hashCode() { return Objects.hash(id); }
}
