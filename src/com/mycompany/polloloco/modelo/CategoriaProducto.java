package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad que representa las categorías o familias de productos del sistema.
 * <p>
 * Ejemplos: «Pollo a la brasa», «Bebidas», «Guarniciones», «Postres».
 * </p>
 */
public class CategoriaProducto implements Serializable {

    private int id;
    private String nombre;
    private String descripcion;
    private boolean activo = true;

    /* ------------------------------
     *  Constructores
     * ------------------------------ */

    public CategoriaProducto() { }

    public CategoriaProducto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaProducto(int id, String nombre, String descripcion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    /* ------------------------------
     *  Getters & Setters
     * ------------------------------ */

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ------------------------------
     *  Métodos utilitarios
     * ------------------------------ */

    /** Devuelve el nombre — útil para <code>JComboBox</code>. */
    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaProducto that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /* ------------------------------
     *  Builder (opcional)
     * ------------------------------ */

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private final CategoriaProducto c = new CategoriaProducto();
        public Builder id(int id) { c.id = id; return this; }
        public Builder nombre(String n) { c.nombre = n; return this; }
        public Builder descripcion(String d) { c.descripcion = d; return this; }
        public Builder activo(boolean a) { c.activo = a; return this; }
        public CategoriaProducto build() { return c; }
    }
}
