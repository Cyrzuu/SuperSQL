package me.cyrzu.git.supersql;

import me.cyrzu.git.supersql.sql.SuperSQL;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateBuilder {

    @NotNull
    private final PreparedStatement statement;

    private int index = 1;

    public UpdateBuilder(@NotNull SQLTable table) {
        try {
            this.statement = table.getSuperSQL().getConnection().prepareStatement(table.getINSERT());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(@NotNull String value) {
        try {
            statement.setString(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(int value) {
        try {
            statement.setInt(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(long value) {
        try {
            statement.setLong(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(double value) {
        try {
            statement.setDouble(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(byte[] value) {
        try {
            statement.setBytes(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public PreparedStatement build() {
        return statement;
    }

    public int execute() {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
