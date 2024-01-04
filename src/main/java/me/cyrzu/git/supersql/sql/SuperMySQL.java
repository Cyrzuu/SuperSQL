package me.cyrzu.git.supersql.sql;

import me.cyrzu.git.supersql.Types;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;

public class SuperMySQL extends SuperSQL {

    public SuperMySQL(String host, String port, String database, String user, String password) {
        super(getConnection(host, port, database, user, password), Types.MYSQL);
    }

    @NotNull
    private static Connection getConnection(String host, String port, String database, String user, String password) {
        String url = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true", host, port, database);
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
