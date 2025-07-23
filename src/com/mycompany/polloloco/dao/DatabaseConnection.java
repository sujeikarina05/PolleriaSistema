package com.mycompany.polloloco.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar la conexión a la base de datos MySQL en XAMPP.
 */
public class DatabaseConnection {

    // URL de conexión para XAMPP (localhost:3306)
    private static final String URL = "jdbc:mysql://localhost:3306/polloloco_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // sin contraseña por defecto en XAMPP

    /**
     * Retorna una conexión activa a la base de datos.
     * @return objeto Connection
     * @throws SQLException si la conexión falla
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Registrar el driver de MySQL
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
