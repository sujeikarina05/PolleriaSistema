package com.mycompany.polloloco;

/**
 * Clase de configuración general del sistema Pollo Loco S.A.C.
 * Contiene constantes de nombre, versión y datos globales.
 */
public final class POLLOLOCO {

    // Previene instanciación
    private POLLOLOCO() {
        throw new UnsupportedOperationException("Clase de constantes - no instanciable.");
    }

    // Constantes del sistema
    public static final String NOMBRE_EMPRESA = "Pollo Loco S.A.C.";
    public static final String VERSION = "v1.0";
    public static final String AUTOR = "Equipo de Desarrollo UCV 2025";
    public static final int AÑO = 2025;

    // Mensajes o rutas comunes
    public static final String TITULO_APP = NOMBRE_EMPRESA + " - Sistema de Gestión";
    public static final String COPYRIGHT = "© " + AÑO + " " + NOMBRE_EMPRESA + ". Todos los derechos reservados.";
    public static final String RUTA_LOGO = "/recursos/logo.png"; // ejemplo si usas imágenes
    public static final String RUTA_EXPORTACIONES = "C:/exportaciones/"; // ruta por defecto

    // Método utilitario (si se desea imprimir en consola el nombre)
    public static String getNombreSistema() {
        return NOMBRE_EMPRESA;
    }
}
