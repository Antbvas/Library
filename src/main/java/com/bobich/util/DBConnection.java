package com.bobich.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties prop = new Properties();
        try (InputStream dbFile = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (dbFile == null) {
                throw new RuntimeException("Файл db.properties не найден");
            }
            prop.load(dbFile);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка настроек БД ", e);
        }

        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }
}
