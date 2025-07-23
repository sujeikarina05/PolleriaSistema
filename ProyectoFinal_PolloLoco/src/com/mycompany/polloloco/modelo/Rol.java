package com.mycompany.polloloco.modelo;

/**
 * Modelo que representa un rol de usuario en el sistema.
 */
public class Rol {

    private int idRol;
    private String nombre;

    public Rol() {
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Rol(int idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Mostrar el nombre directamente en ComboBox o List
    @Override
    public String toString() {
        return nombre;
    }
}
