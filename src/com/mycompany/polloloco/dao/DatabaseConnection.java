package com.mycompany.polloloco.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:mem:polloloco";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
