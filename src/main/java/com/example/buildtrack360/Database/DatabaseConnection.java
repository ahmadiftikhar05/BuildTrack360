package com.example.buildtrack360.Database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseConnection {
    private static final String Url = "jdbc:mysql://localhost:3306/buildtrack360";
    private static final String Username = "root";
    private static final String Password = "Ahmad123";

    public static Connection GetConnection() throws SQLException {
        return DriverManager.getConnection(Url, Username, Password);
    }
}
