package me.cyrzu.git.supersql;

import lombok.Getter;
import me.cyrzu.git.supersql.column.AbstractColumn;
import me.cyrzu.git.supersql.column.AbstractKeyColumn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SQLTable {

    @Getter
    @NotNull
    private final SuperSQL superSQL;

    @Getter
    @NotNull
    private final String name;

    @NotNull
    private final Map<String, AbstractColumn> columns;

    @Getter
    @Nullable
    private final AbstractColumn key;

    @Getter
    @NotNull
    private final String INSERT;

    private SQLTable(@NotNull String name, @NotNull Map<String, AbstractColumn> columns, @Nullable AbstractColumn key, @NotNull SuperSQL superSQL) {
        this.superSQL = superSQL;
        this.name = name;
        this.columns = columns;
        this.key = key;
        this.INSERT = superSQL.getType().insertAndUpdate(this);
    }

    @NotNull
    public PreparedStatement getCreateStatement(@NotNull Connection connection) {
        try {
            return connection.prepareStatement(superSQL.getType().createTable(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public Map<String, AbstractColumn> getColumns() {
        return Map.copyOf(columns);
    }

    public void createUpdate(@NotNull SQLObject object) {
        try(PreparedStatement statement = object.updateObject(new UpdateBuilder(superSQL, this)).build()) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public static Builder builder(@NotNull SuperSQL sql, @NotNull String name) {
        return new Builder(sql, name);
    }

    public static class Builder {

        @NotNull
        private final SuperSQL sql;

        @NotNull
        private final String name;

        @NotNull
        private final Map<String, AbstractColumn> columns;

        @Nullable
        private AbstractColumn key = null;

        private Builder(@NotNull SuperSQL sql, @NotNull String name) {
            this.sql = sql;
            this.name = name;
            this.columns = new LinkedHashMap<>();
        }

        public Builder add(@NotNull AbstractColumn... columns) {
            Arrays.stream(columns).forEach(this::add);
            return this;
        }

        public Builder add(@NotNull AbstractColumn column) {
            System.out.println(key + " < kej");
            if(this.key == null && column instanceof AbstractKeyColumn keyColumn && keyColumn.isPrimaryKey()) {
                this.key = column;
            }

            columns.put(column.getName(), column);
            return this;
        }

        public @NotNull SQLTable build() {
            return new SQLTable(name, columns, key, sql);
        }

    }

}
