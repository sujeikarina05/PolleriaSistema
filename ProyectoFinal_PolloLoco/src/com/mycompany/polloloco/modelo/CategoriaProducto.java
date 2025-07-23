package com.mycompany.polloloco.modelo;

/**
 * Modelo que representa una categoría de producto.
 */
public class CategoriaProducto {

    private int id;
    private String nombre;

    public CategoriaProducto() {
    }

    public CategoriaProducto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaProducto(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Para mostrar nombre directamente en JComboBox, JList, etc.
    @Override
    public String toString() {
        return nombre;
    }
}
