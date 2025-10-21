package org.example;

import org.example.configuration.SessionFactoryUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        SessionFactoryUtil.getSessionFactory().openSession();
    }
}