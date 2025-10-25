package ru.kpfu.itis.semester_work.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionWrapper {
    private static Connection connection;
    public static Connection getConnection(){
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                try {
                    connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/semesterwork1_oris","postgres","hkkkkjv");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
