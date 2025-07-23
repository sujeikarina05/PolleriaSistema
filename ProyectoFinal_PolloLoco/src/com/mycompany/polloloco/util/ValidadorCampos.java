package com.mycompany.polloloco.util;

import java.util.regex.Pattern;

/** Utilidades de validación de cadenas para formularios. */
public final class ValidadorCampos {

    /*--- Expresiones pre‑compiladas (↑ performance) ---*/
    private static final Pattern PAT_DECIMAL      = Pattern.compile("^-?\\d+(?:[\\.,]\\d+)?$");
    private static final Pattern PAT_CORREO       = Pattern.compile("^[\\w.-]+@[\\w-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PAT_DNI          = Pattern.compile("^\\d{8}$");
    private static final Pattern PAT_RUC          = Pattern.compile("^\\d{11}$");
    private static final Pattern PAT_SOLO_LETRAS  = Pattern.compile("[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+");
    private static final Pattern PAT_SOLO_NUM     = Pattern.compile("\\d+");
    private static final Pattern PAT_TELEFONO     = Pattern.compile("^\\d{6,9}$");   // fijo o anexo
    private static final Pattern PAT_CEL_PERU     = Pattern.compile("^9\\d{8}$");    // 9 dígitos, empieza en 9
    private static final Pattern PAT_FECHA_ISO    = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private ValidadorCampos() {}  // impedir instancias

    public static boolean esVacio(String txt)               { return txt == null || txt.trim().isEmpty(); }
    public static boolean esNumero(String txt)              { return PAT_SOLO_NUM.matcher(txt).matches(); }
    public static boolean esDecimal(String txt)             { return PAT_DECIMAL.matcher(txt).matches(); }
    public static boolean esPrecioPositivo(String txt)      { return esDecimal(txt) && Double.parseDouble(txt.replace(',', '.')) >= 0; }
    public static boolean esCorreo(String txt)              { return PAT_CORREO.matcher(txt).matches(); }
    public static boolean esDni(String txt)                 { return PAT_DNI.matcher(txt).matches(); }
    public static boolean esRuc(String txt)                 { return PAT_RUC.matcher(txt).matches(); }
    public static boolean esTelefono(String txt)            { return PAT_TELEFONO.matcher(txt).matches(); }
    public static boolean esCelularPeru(String txt)         { return PAT_CEL_PERU.matcher(txt).matches(); }
    public static boolean esFechaISO(String txt)            { return PAT_FECHA_ISO.matcher(txt).matches(); }
    public static boolean tieneLongitudEntre(String t,int min,int max){ return t!=null && t.length()>=min && t.length()<=max; }
    public static boolean contieneSoloLetras(String txt)    { return PAT_SOLO_LETRAS.matcher(txt).matches(); }
    public static boolean contieneSoloNumeros(String txt)   { return PAT_SOLO_NUM.matcher(txt).matches(); }
}
