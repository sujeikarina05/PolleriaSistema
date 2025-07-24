package com.mycompany.polloloco.util;

import java.util.regex.Pattern;

/**
 * Utilidades de validación de cadenas para formularios del sistema.
 */
public final class ValidadorCampos {

    /* -------------------- Expresiones pre‑compiladas -------------------- */
    private static final Pattern PAT_DECIMAL       = Pattern.compile("^-?\\d+(?:[\\.,]\\d+)?$");
    private static final Pattern PAT_CORREO        = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PAT_DNI           = Pattern.compile("^\\d{8}$");
    private static final Pattern PAT_RUC           = Pattern.compile("^\\d{11}$");
    private static final Pattern PAT_SOLO_LETRAS   = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$");
    private static final Pattern PAT_SOLO_NUM      = Pattern.compile("^\\d+$");
    private static final Pattern PAT_TELEFONO      = Pattern.compile("^\\d{6,9}$");   // fijo
    private static final Pattern PAT_CEL_PERU      = Pattern.compile("^9\\d{8}$");    // celular PE
    private static final Pattern PAT_FECHA_ISO     = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern PAT_HORA_24       = Pattern.compile("^(?:[01]\\d|2[0-3]):[0-5]\\d$");

    private ValidadorCampos() {}

    /* -------------------------- Métodos básicos ------------------------- */
    public static boolean esVacio(String t)              { return t == null || t.trim().isEmpty(); }
    public static boolean tieneLongitud(String t,int m)  { return t != null && t.length() >= m; }
    public static boolean tieneLongitudEntre(String t,int min,int max){ return t!=null && t.length()>=min && t.length()<=max; }

    public static boolean esNumero(String t)             { return PAT_SOLO_NUM.matcher(t).matches(); }
    public static boolean esDecimal(String t)            { return PAT_DECIMAL.matcher(t).matches(); }
    public static boolean esPrecioPositivo(String t)     { return esDecimal(t) && Double.parseDouble(t.replace(',','.')) >= 0; }

    public static boolean esCorreo(String t)             { return PAT_CORREO.matcher(t).matches(); }
    public static boolean esDni(String t)                { return PAT_DNI.matcher(t).matches(); }
    public static boolean esRuc(String t)                { return PAT_RUC.matcher(t).matches(); }

    public static boolean esTelefono(String t)           { return PAT_TELEFONO.matcher(t).matches(); }
    public static boolean esCelularPeru(String t)        { return PAT_CEL_PERU.matcher(t).matches(); }

    public static boolean esFechaISO(String t)           { return PAT_FECHA_ISO.matcher(t).matches(); }
    public static boolean esHora24(String t)             { return PAT_HORA_24.matcher(t).matches(); }

    public static boolean contieneSoloLetras(String t)   { return PAT_SOLO_LETRAS.matcher(t).matches(); }
    public static boolean contieneSoloNumeros(String t)  { return PAT_SOLO_NUM.matcher(t).matches(); }

    /* -------------------- Métodos utilitarios avanzados ----------------- */

    /**
     * Devuelve <code>true</code> si el texto representa un número decimal
     * dentro del rango especificado (inclusive).
     */
    public static boolean decimalEnRango(String t,double min,double max){
        if (!esDecimal(t)) return false;
        double v = Double.parseDouble(t.replace(',','.'));
        return v >= min && v <= max;
    }

    /** Trunca o rellena el texto para que tenga exactamente la longitud dada. */
    public static String ajustarLongitud(String t,int len,char relleno){
        if (t == null) t = "";
        if (t.length() > len) return t.substring(0,len);
        return String.format("%1$-"+len+"s", t).replace(' ', relleno);
    }
}
