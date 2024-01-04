package me.cyrzu.git.supersql;

import lombok.Getter;
import me.cyrzu.git.supersql.column.AbstractColumn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLTable {

    @Getter
    @NotNull
    private final String name;

    @NotNull
    private final Map<String, AbstractColumn> columns;

    @Getter
    @Nullable
    private AbstractColumn key = null;

    @Getter
    @NotNull
    private final String INSERT;

    private SQLTable(@NotNull String name, @NotNull Map<String, AbstractColumn> columns, @NotNull SuperSQL sql) {
        this.name = name;
        this.columns = columns;
        this.INSERT = sql.getType().insertAndUpdate(this);
    }

    @NotNull
    public PreparedStatement getCreateStatement(@NotNull Connection connection) {
        try {
            return connection.prepareStatement(getCreateCommand());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public String getCreateCommand() {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + name + " (");
        final List<AbstractColumn> values = List.copyOf(columns.values());
        for (int i = 0; i < values.size(); i++) {
            final AbstractColumn column = values.get(i);
            builder.append(column.create());

            if(i != values.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append(");");
        return builder.toString();
    }

    @NotNull
    public Map<String, AbstractColumn> getColumns() {
        return Map.copyOf(columns);
    }

    public SQLTable setKey(@Nullable AbstractColumn column) {
        this.key = column;
        return this;
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
            if(this.key == null && column.isPrimaryKey()) {
                this.key = column;
            }

            columns.put(column.getName(), column);
            return this;
        }

        public @NotNull SQLTable build() {
            return new SQLTable(name, columns, sql).setKey(this.key);
        }

    }

}
