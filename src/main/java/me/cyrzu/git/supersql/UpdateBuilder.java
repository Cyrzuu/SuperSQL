package me.cyrzu.git.supersql;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBuilder {

    @NotNull
    private final SuperSQL sql;

    @NotNull
    private final SQLTable table;

    @NotNull
    private final PreparedStatement statement;

    private int index = 1;

    public UpdateBuilder(@NotNull SuperSQL sql, @NotNull SQLTable table) {
        this.sql = sql;
        this.table = table;

        try {
            this.statement = sql.getConnection().prepareStatement(table.getINSERT());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(@NotNull String value) {
        try {
            statement.setString(index++, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(int value) {
        try {
            statement.setInt(index++, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(long value) {
        try {
            statement.setLong(index++, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(double value) {
        try {
            statement.setDouble(index++, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(byte[] value) {
        try {
            statement.setBytes(index++, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull PreparedStatement build() {
        return statement;
    }

}