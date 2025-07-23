package com.mycompany.polloloco.util;

public class ValidadorCampos {
    public boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
