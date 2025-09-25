package com.example.louishotelmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=HotelDB";
    private static final String USER = "sa";
    private static final String PASS = "SApassword@123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
