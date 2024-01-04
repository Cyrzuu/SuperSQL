package me.cyrzu.git.supersql;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SuperSQL {

    @Getter
    @NotNull
    protected final Connection connection;

    private final Map<String, SQLTable> tables = new HashMap<>();

    public SuperSQL(@NotNull Connection connection) {
        this.connection = connection;
    }

    public void createTable(@NotNull SQLTable sqlTable) {
        createTable(sqlTable, null);
    }

    public void createTable(@NotNull SQLTable sqlTable, @Nullable Consumer<SQLException> exception) {
        try(PreparedStatement statement = sqlTable.getCreateStatement(this.connection)) {
           statement.executeUpdate();
        } catch (SQLException e) {
            if(exception != null) {
                exception.accept(e);
            }
        }
    }

    @Nullable
    public SQLTable getTable(@NotNull String tableName) {
        return tables.get(tableName);
    }

}