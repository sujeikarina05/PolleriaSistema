package com.mycompany.polloloco.util;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para validar campos de texto.
 */
public class ValidadorCampos {

    // Evita instanciación
    private ValidadorCampos() {}

    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esNumero(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esDecimal(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esCorreo(String texto) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, texto);
    }

    public static boolean esDni(String texto) {
        return texto != null && texto.matches("\\d{8}");
    }

    public static boolean esRuc(String texto) {
        return texto != null && texto.matches("\\d{11}");
    }

    public static boolean tieneLongitudEntre(String texto, int min, int max) {
        return texto != null && texto.length() >= min && texto.length() <= max;
    }

    public static boolean contieneSoloLetras(String texto) {
        return texto != null && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

    public static boolean contieneSoloNumeros(String texto) {
        return texto != null && texto.matches("\\d+");
    }
}
