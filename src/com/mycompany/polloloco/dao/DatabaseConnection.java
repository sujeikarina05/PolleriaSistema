package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.POLLOLOCO;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Proveedor centralizado de conexiones a la base de datos MySQL.
 * <p>
 * <b>Modo pool (HikariCP)</b> – si la librería está presente en el classpath.<br>
 * <b>Modo directo</b> – usa {@link DriverManager} como respaldo.
 * <p>
 * Credenciales y URL se leen desde {@link POLLOLOCO#dbUrl()} y asociados.
 */
public final class DatabaseConnection {

    private static final Logger LOG = Logger.getLogger(DatabaseConnection.class.getName());
    private static final HikariDataSource DS;

    static {
        HikariDataSource temp = null;
        try {
            /* ---------- Configuración del pool ---------- */
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(POLLOLOCO.dbUrl());
            cfg.setUsername(POLLOLOCO.dbUser());
            cfg.setPassword(POLLOLOCO.dbPassword());
            cfg.setMaximumPoolSize(10);
            cfg.setMinimumIdle(2);
            cfg.setIdleTimeout(60_000);          // 1 min inactivo
            cfg.setConnectionTimeout(15_000);    // 15 seg máx espera
            cfg.setPoolName("POLLOLOCO-Hikari");
            temp = new HikariDataSource(cfg);
            LOG.info("Pool HikariCP inicializado ✔");
        } catch (Throwable ex) {
            /* Si falla (por falta de la lib)… */
            LOG.log(Level.WARNING, "HikariCP no disponible, se usará DriverManager (motivo: {0})", ex.getMessage());
        }
        DS = temp;
        /* Pre‑carga del driver MySQL para modo DriverManager */
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "Driver MySQL no encontrado", e);
        }
    }

    private DatabaseConnection() { /* utility class */ }

    /**
     * Obtiene una {@link Connection} válida.
     * @return conexión del pool o directa.
     * @throws SQLException si no se puede abrir conexión.
     */
    public static Connection getConnection() throws SQLException {
        if (DS != null) {
            return DS.getConnection();
        }
        // Respaldo simple
        return DriverManager.getConnection(
                POLLOLOCO.dbUrl(),
                POLLOLOCO.dbUser(),
                POLLOLOCO.dbPassword());
    }

    /** Cierra el pool al terminar la aplicación (invocar desde shutdown hook). */
    public static void shutdown() {
        if (DS != null && !DS.isClosed()) {
            DS.close();
            LOG.info("HikariCP cerrado correctamente");
        }
    }
}
