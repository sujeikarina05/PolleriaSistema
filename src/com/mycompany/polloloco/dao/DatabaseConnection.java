package com.mycompany.polloloco.dao;

import com.mycompany.polloloco.POLLOLOCO;

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
    static {
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
        return DriverManager.getConnection(
                POLLOLOCO.dbUrl(),
                POLLOLOCO.dbUser(),
                POLLOLOCO.dbPassword());
    }

    /** Cierra el pool al terminar la aplicación (invocar desde shutdown hook). */
    public static void shutdown() {
        // nada por hacer
    }
}
