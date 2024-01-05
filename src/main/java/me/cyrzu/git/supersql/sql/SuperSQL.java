package me.cyrzu.git.supersql.sql;

import lombok.Getter;
import me.cyrzu.git.supersql.SQLTable;
import me.cyrzu.git.supersql.SelectBuilder;
import me.cyrzu.git.supersql.Types;
import me.cyrzu.git.supersql.UpdateBuilder;
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
    private final Connection connection;

    @Getter
    @NotNull
    private final Types type;

    private final Map<String, SQLTable> tables = new HashMap<>();


    public SuperSQL(@NotNull Connection connection, @NotNull Types type) {
        this.connection = connection;
        this.type = type;
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

    @NotNull
    public SQLTable.Builder tableBuilder(@NotNull String name) {
        return SQLTable.builder(this, name);
    }

    @NotNull
    public UpdateBuilder updateBuilder(@NotNull SQLTable table) {
        return new UpdateBuilder(table);
    }

    public SelectBuilder selectBuilder(@NotNull SQLTable table, @NotNull String... columns) {
        return new SelectBuilder(table, columns);
    }

    public SelectBuilder selectBuilder(@NotNull SQLTable table) {
        return new SelectBuilder(table);
    }

}
