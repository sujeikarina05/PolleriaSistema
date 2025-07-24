package com.mycompany.polloloco;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Properties;

/**
 * Configuración global y constantes de la aplicación <b>Pollería Pollo Loco S.A.C.</b>
 * <p>
 * Centraliza nombre, versión, rutas, formatos y parámetros de conexión a BD para
 * que cualquier módulo pueda consultarlos sin recurrir a cadenas mágicas.
 */
public final class POLLOLOCO {

    /* ─────────────────── Prevent instantiation ─────────────────── */
    private POLLOLOCO() {
        throw new UnsupportedOperationException("Clase de utilidades – no instanciable");
    }

    /* ─────────────────── Datos básicos de la aplicación ─────────────────── */
    public static final String EMPRESA  = "Pollo Loco S.A.C.";
    public static final String VERSION  = "1.0.0";
    public static final String AUTOR    = "Equipo de Desarrollo UCV 2025";
    public static final int    ANIO     = 2025;

    public static final String APP_TITLE  = EMPRESA + " – Sistema de Gestión";
    public static final String COPYRIGHT  = "© " + ANIO + " " + EMPRESA + ". Todos los derechos reservados.";

    /* ─────────────────── Rutas y archivos ─────────────────── */
    public static final Path DIR_RECURSOS   = Paths.get("resources");
    public static final Path DIR_EXPORT     = Paths.get("exportaciones");
    public static final String LOGO_NOMBRE  = "logo-pollo-loco.png";

    /* ─────────────────── Formatos comunes ─────────────────── */
    public static final Locale LOCALE_PE = new Locale("es", "PE");
    public static final NumberFormat FORMATO_MONEDA = NumberFormat.getCurrencyInstance(LOCALE_PE);

    public static final DateTimeFormatter FMT_FECHA_CORTA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE_PE);
    public static final DateTimeFormatter FMT_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", LOCALE_PE);

    /* ─────────────────── Propiedades de base de datos ─────────────────── */
    private static final Properties DB_PROPS = new Properties();

    static {
        /* Crea carpetas esenciales si no existen */
        try {
            Files.createDirectories(DIR_EXPORT);
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio de exportaciones: " + e.getMessage());
        }

        /* Carga archivo de configuración (classpath:/config/db.properties) */
        try (InputStream is = POLLOLOCO.class.getResourceAsStream("/config/db.properties")) {
            if (is != null) {
                DB_PROPS.load(is);
            } else {
                System.err.println("Advertencia: no se encontró config/db.properties; se usarán valores por defecto.");
            }
        } catch (IOException ex) {
            System.err.println("Error al leer db.properties: " + ex.getMessage());
        }
    }

    /* ─────────────────── Getters de conexión ─────────────────── */
    public static String dbUrl() {
        return DB_PROPS.getProperty("db.url",
                "jdbc:mysql://localhost:3306/polloloco_db?serverTimezone=UTC");
    }

    public static String dbUser() {
        return DB_PROPS.getProperty("db.user", "root");
    }

    public static String dbPassword() {
        return DB_PROPS.getProperty("db.password", "");
    }

    /* ─────────────────── Utilidades varias ─────────────────── */
    /** Devuelve la ruta absoluta del logo de la empresa. */
    public static Path getLogoPath() {
        return DIR_RECURSOS.resolve(LOGO_NOMBRE);
    }

    /** Devuelve el directorio donde se guardan los reportes/exportaciones. */
    public static Path getExportDir() {
        return DIR_EXPORT;
    }
}
