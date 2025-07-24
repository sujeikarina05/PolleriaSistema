package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entidad de usuario del sistema (administrador, cajero, mozo, etc.).
 */
public class Usuario implements Serializable {

    private int id;              // PK autoincrement
    private String nombre;       // nombre completo
    private String usuario;      // username único
    private String claveHash;    // SHA‑256 hash
    private String clave;        // contraseña en texto plano (transient)
    private int idRol;           // FK -> rol(id)
    private boolean activo = true; // soft‑delete

    /* ---------------- Constructores ---------------- */
    public Usuario() {}

    public Usuario(String nombre, String usuario, String claveHash, int idRol) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.claveHash = claveHash;
        this.idRol = idRol;
    }

    public Usuario(int id, String nombre, String usuario, String claveHash, int idRol, boolean activo) {
        this(nombre, usuario, claveHash, idRol);
        this.id = id;
        this.activo = activo;
    }

    /* ---------------- Getters / Setters ---------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().length() < 3)
            throw new IllegalArgumentException("Nombre muy corto");
        this.nombre = nombre.trim();
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) {
        if (usuario == null || usuario.trim().length() < 3)
            throw new IllegalArgumentException("Usuario muy corto");
        this.usuario = usuario.trim().toLowerCase();
    }

    public String getClaveHash() { return claveHash; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    /** Nombre del rol según su id. */
    public String getRol() {
        return switch (idRol) {
            case 1 -> "Administrador";
            case 2 -> "Cajero";
            case 3 -> "Mozo";
            default -> "";
        };
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ---------------- Utilidades ---------------- */
    public void desactivar() { this.activo = false; }
    public void reactivar()  { this.activo = true;  }

    @Override public String toString() { return nombre + " (" + usuario + ")"; }

    @Override public boolean equals(Object o) {
        return (o instanceof Usuario u) && u.id == id;
    }
    @Override public int hashCode() { return Objects.hash(id); }

    /* ---------------- Builder ---------------- */
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Usuario u = new Usuario();
        public Builder nombre(String n){u.setNombre(n); return this;}
        public Builder usuario(String u1){u.setUsuario(u1); return this;}
        public Builder clave(String c){u.setClave(c); return this;}
        public Builder claveHash(String c){u.setClaveHash(c); return this;}
        public Builder idRol(int r){u.setIdRol(r); return this;}
        public Usuario build(){ return u; }
    }
}
