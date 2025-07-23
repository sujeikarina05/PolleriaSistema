package com.mycompany.polloloco.modelo;

/**
 * Modelo para representar configuraciones clave-valor del sistema.
 */
public class Configuracion {

    private String clave;
    private String valor;

    public Configuracion() {
    }

    public Configuracion(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    // Getters
    public String getClave() {
        return clave;
    }

    public String getValor() {
        return valor;
    }

    // Setters
    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return clave + " = " + valor;
    }
}
