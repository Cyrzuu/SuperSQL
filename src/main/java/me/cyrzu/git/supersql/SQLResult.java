package me.cyrzu.git.supersql;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLResult {

    @Getter
    @NotNull
    private final ResultSet resultSet;

    public SQLResult(@NotNull ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean next() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean wasNull() {
        try {
            return resultSet.wasNull();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(@NotNull String columnLabel) {
        try {
            return resultSet.getString(columnLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInt(@NotNull String columnLabel) {
        try {
            return resultSet.getInt(columnLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLong(@NotNull String columnLabel) {
        try {
            return resultSet.getLong(columnLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getDouble(@NotNull String columnLabel) {
        try {
            return resultSet.getDouble(columnLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getBytes(@NotNull String columnLabel) {
        try {
            return resultSet.getBytes(columnLabel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
