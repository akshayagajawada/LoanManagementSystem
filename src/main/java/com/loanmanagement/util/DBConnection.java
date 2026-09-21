package com.loanmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC";

    private static final String USERNAME =
            System.getenv().getOrDefault("LMS_DB_USERNAME", "root");

    private static final String PASSWORD =
            System.getenv("LMS_DB_PASSWORD");

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException(
                    "LMS_DB_PASSWORD environment variable is not configured."
            );
        }

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}