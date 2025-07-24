package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un rol de acceso dentro del sistema (Administrador, Cajero, Mozo…).
 */
public class Rol implements Serializable {

    private int id;          // PK autoincrement
    private String nombre;   // único, ≥ 3 caracteres
    private boolean activo = true; // soft‑delete

    /* -------------------------------------------------- */
    /* Constructores                                      */
    /* -------------------------------------------------- */
    public Rol() { }

    public Rol(String nombre) {
        this.setNombre(nombre);
    }

    public Rol(int id, String nombre) {
        this.id = id;
        this.setNombre(nombre);
    }

    /* -------------------------------------------------- */
    /* Getters & Setters                                  */
    /* -------------------------------------------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre del rol debe tener al menos 3 caracteres.");
        }
        this.nombre = nombre.trim();
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* -------------------------------------------------- */
    /* Utilitarios                                         */
    /* -------------------------------------------------- */
    public Rol desactivar() { this.activo = false; return this; }
    public Rol activar()    { this.activo = true;  return this; }

    /* -------------------------------------------------- */
    /* equals, hashCode, toString                          */
    /* -------------------------------------------------- */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol)) return false;
        Rol rol = (Rol) o;
        return id == rol.id;
    }

    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() { return nombre; }

    /* -------------------------------------------------- */
    /* Builder pattern                                     */
    /* -------------------------------------------------- */
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Rol r = new Rol();
        public Builder id(int id)            { r.id = id; return this; }
        public Builder nombre(String nombre) { r.setNombre(nombre); return this; }
        public Builder activo(boolean a)     { r.activo = a; return this; }
        public Rol build() { return r; }
    }
}
