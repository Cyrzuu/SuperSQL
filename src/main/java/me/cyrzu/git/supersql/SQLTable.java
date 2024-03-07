package me.cyrzu.git.supersql;

import lombok.Getter;
import me.cyrzu.git.supersql.column.AbstractColumn;
import me.cyrzu.git.supersql.column.AbstractKeyColumn;
import me.cyrzu.git.supersql.sql.SuperSQL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

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
        this.INSERT = superSQL.getType().insertAndUpdate(this, 1);
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
        return new LinkedHashMap<>(columns);
    }

    @NotNull
    public List<AbstractColumn> getColumnList() {
        return List.copyOf(columns.values());
    }

    @NotNull
    public UpdateBuilder updateBuilder() {
        return new UpdateBuilder(this);
    }

    @NotNull
    public UpdateBuilder updateBuilder(@NotNull PreparedStatement statement) {
        return new UpdateBuilder(this, statement);
    }

    public SelectBuilder selectBuilder(@NotNull String... columns) {
        return new SelectBuilder(this, columns);
    }

    public SelectBuilder selectBuilder() {
        return new SelectBuilder(this);
    }

    public DeleteBuilder deleteBuilder() {
        return new DeleteBuilder(this);
    }

    public long createUpdate(@NotNull SQLObject object) {
        return object.updateObject(new UpdateBuilder(this)).execute();
    }

    public void createAsyncUpdate(@NotNull SQLObject object) {
        createAsyncUpdate(object, null);
    }

    public void createAsyncUpdate(@NotNull SQLObject object, @Nullable Consumer<Long> time) {
        object.updateObject(new UpdateBuilder(this)).executeAsync(time);
    }

    public long createMultiUpdate(@NotNull Collection<? extends SQLObject> objects) {
        long start = System.currentTimeMillis();
        MultiUpdateBuilder<? extends SQLObject> multiUpdateBuilder = new MultiUpdateBuilder<>(this, objects);
        multiUpdateBuilder.execute();
        return System.currentTimeMillis() - start;
    }

    public void createAsyncMultiUpdate(@NotNull Collection<? extends SQLObject> objects) {
        createAsyncMultiUpdate(objects, null);
    }

    public void createAsyncMultiUpdate(@NotNull Collection<? extends SQLObject> objects, @Nullable Consumer<Long> consumer) {
        MultiUpdateBuilder<? extends SQLObject> multiUpdateBuilder = new MultiUpdateBuilder<>(this, objects);
        multiUpdateBuilder.executeAsync(consumer);
    }

    public SQLResult sqlResult(@NotNull String sql) {
        return new SQLResult(sqlQuery(sql));
    }

    public ResultSet sqlQuery(@NotNull String sql) {
        try(PreparedStatement statement = superSQL.getConnection().prepareStatement(sql)) {
            return statement.executeQuery();
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
            if(this.key == null && column instanceof AbstractKeyColumn keyColumn && keyColumn.isPrimaryKey()) {
                this.key = column;
            }

            columns.put(column.getName(), column);
            return this;
        }

        public @NotNull SQLTable build() {
            List<AbstractColumn> values = new ArrayList<>(columns.values());
            values.sort(Comparator.comparing(colum -> !(colum instanceof AbstractKeyColumn key) || !key.isPrimaryKey()));

            LinkedHashMap<String, AbstractColumn> newMap = new LinkedHashMap<>();
            for (AbstractColumn value : values) {
                newMap.put(value.getName(), value);
            }

            return new SQLTable(name, newMap, key, sql);
        }
    }

}
