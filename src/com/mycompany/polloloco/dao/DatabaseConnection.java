package com.mycompany.polloloco.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Utility class that provides a single point for obtaining a JDBC
 *  connection to the polloloco_db database.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/polloloco_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // update if needed
    private static Connection conn;

    /**
     * Returns a Connection to the MySQL database. The connection is lazily
     * created and reused for subsequent calls.
     */
    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
