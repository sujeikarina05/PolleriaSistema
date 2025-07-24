package com.mycompany.polloloco;

import com.mycompany.polloloco.dao.DatabaseConnection;
import com.mycompany.polloloco.vista.LoginFrame;
import com.mycompany.polloloco.util.Splash;

import javax.swing.*;
import java.sql.Connection;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Punto de entrada de la aplicación «Pollería Pollo Loco».
 * <p>
 * Responsabilidades principales:
 * <ul>
 *   <li>Inicializar configuración regional y Look & Feel.</li>
 *   <li>Cargar propiedades de la aplicación (config.properties).</li>
 *   <li>Verificar la conexión a la base de datos al inicio.</li>
 *   <li>Mostrar un splash screen breve mientras se preparan recursos.</li>
 *   <li>Disparar la ventana de Login en el EDT.</li>
 *   <li>Registrar un manejador global de excepciones para capturar fallos inesperados.</li>
 * </ul>
 */
public final class Main {

    /** Logger de la aplicación */
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    /** Archivo de propiedades */
    private static final String CONFIG_FILE = "/config.properties";

    /**
     * Método {@code main}. Configura el entorno y muestra la pantalla de inicio de sesión.
     */
    public static void main(String[] args) {
        // 1) Configuración regional: forzar español (Perú) por defecto
        Locale.setDefault(new Locale("es", "PE"));

        // 2) Cargar configuraciones
        Properties appProps = cargarPropiedades();

        // 3) Inicializar Look & Feel
        inicializarLookAndFeel();

        // 4) Instalar manejador global de excepciones
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            LOG.log(Level.SEVERE, "Excepción no controlada en hilo " + t.getName(), e);
            SwingUtilities.invokeLater(() -> mostrarErrorFatal(e));
        });

        // 5) Verificar conexión a BD antes de lanzar la GUI
        if (!probarConexionBD()) {
            mostrarErrorFatal(new IllegalStateException("No se pudo establecer conexión con la base de datos"));
            return;
        }

        // 6) Mostrar breve splash mientras carga la GUI
        Splash.mostrarSplash(1500); // 1.5 s

        // 7) Lanzar LoginFrame en el EDT
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(appProps).setVisible(true);
        });
    }

    // ---------------------------------------------------------------------
    // Métodos privados auxiliares
    // ---------------------------------------------------------------------

    private static Properties cargarPropiedades() {
        Properties p = new Properties();
        try {
            p.load(Main.class.getResourceAsStream(CONFIG_FILE));
            LOG.info("Propiedades cargadas correctamente");
        } catch (Exception e) {
            LOG.log(Level.WARNING, "No se encontró config.properties; se usará configuración por defecto", e);
        }
        return p;
    }

    private static void inicializarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Falló LookAndFeel nativo, intentando FlatLaf", e);
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "No se pudo iniciar ningún LookAndFeel", ex);
            }
        }
    }

    private static boolean probarConexionBD() {
        try (Connection ignored = DatabaseConnection.getConnection()) {
            LOG.info("Conexión a BD exitosa");
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error de conexión a BD", e);
            return false;
        }
    }

    private static void mostrarErrorFatal(Throwable e) {
        JOptionPane.showMessageDialog(null,
                "Ocurrió un error crítico y la aplicación debe cerrarse:\n" + e.getMessage(),
                "Error fatal", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
