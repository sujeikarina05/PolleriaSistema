package com.mycompany.polloloco.modelo;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un par <i>clave → valor</i> de configuración en la base de datos.
 * <p>
 * Ejemplos: <pre>
 *  impresora.default = EPSON-TM20
 *  igv.tasa           = 0.18
 *  moneda.sigla       = PEN
 * </pre>
 * Se asegura que la clave no sea nula ni vacía y admite valores de hasta 255
 * caracteres.  La capa DAO persistirá estos pares en la tabla <b>configuracion</b>
 * con columnas (id, clave UNIQUE, valor, activo).
 */
public class Configuracion implements Serializable {

    private int id;           // PK (autonumerado)
    private String clave;     // UNIQUE, no nula
    private String valor;     // valor de texto (hasta 255)
    private boolean activo = true; // para soft‑delete

    /* ---------------- Constructors ---------------- */
    public Configuracion() { }

    public Configuracion(String clave, String valor) {
        setClave(clave);
        setValor(valor);
    }

    public Configuracion(int id, String clave, String valor, boolean activo) {
        this.id = id;
        setClave(clave);
        setValor(valor);
        this.activo = activo;
    }

    /* ---------------- Getters / Setters ---------------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getClave() { return clave; }
    public void setClave(String clave) {
        if (clave == null || clave.trim().length() < 2)
            throw new IllegalArgumentException("La clave no puede ser nula ni tan corta.");
        this.clave = clave.trim();
    }

    public String getValor() { return valor; }
    public void setValor(String valor) {
        this.valor = valor == null ? "" : valor.trim();
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /* ---------------- Utility ---------------- */
    @Override
    public String toString() { return clave + " = " + valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuracion that)) return false;
        return Objects.equals(clave, that.clave);
    }

    @Override
    public int hashCode() { return Objects.hash(clave); }

    /* ---------------- Builder pattern ---------------- */
    public static Builder builder(String clave) { return new Builder(clave); }

    public static class Builder {
        private final Configuracion cfg;
        private Builder(String clave) { cfg = new Configuracion(); cfg.setClave(clave); }
        public Builder valor(String v) { cfg.setValor(v); return this; }
        public Builder activo(boolean b) { cfg.setActivo(b); return this; }
        public Configuracion build() { return cfg; }
    }
}
