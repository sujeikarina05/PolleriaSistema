package com.mycompany.polloloco.modelo;

public class Configuracion {
    private String clave;
    private String valor;

    public Configuracion(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public String getClave() { return clave; }
    public String getValor() { return valor; }
}
