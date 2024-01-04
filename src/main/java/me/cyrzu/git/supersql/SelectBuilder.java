package me.cyrzu.git.supersql;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SelectBuilder {

    @NotNull
    private final SQLTable sqlTable;

    @NotNull
    private final Set<String> columns;

    @NotNull
    private final Map<String, Object> where;

    public SelectBuilder(@NotNull SQLTable sqlTable) {
        this.sqlTable = sqlTable;
        this.columns = new HashSet<>();
        this.where = new LinkedHashMap<>();
    }

    public SelectBuilder(@NotNull SQLTable sqlTable, @NotNull String... columns) {
        this.sqlTable = sqlTable;
        this.columns = Set.of(columns);
        this.where = new LinkedHashMap<>();
    }

    public SelectBuilder where(@NotNull String key, @NotNull String value) {
        where.put(key, value);
        return this;
    }

    public SelectBuilder where(@NotNull String key, @NotNull Integer value) {
        where.put(key, value);
        return this;
    }

    public SelectBuilder where(@NotNull String key, @NotNull Long value) {
        where.put(key, value);
        return this;
    }

    @NotNull
    public SQLResult execute() {
        StringBuilder builder = new StringBuilder("SELECT");

        if(columns.isEmpty()) {
            builder.append(" * ");
        } else {
            Iterator<String> iterator = columns.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                builder.append(next);

                if(iterator.hasNext()) {
                    builder.append(", ");
                }
            }
        }

        builder.append(" FROM ").append(sqlTable.getName());

        if(!where.isEmpty()) {
            Iterator<Map.Entry<String, Object>> iterator = where.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                builder.append(next.getKey()).append(" = ?");

                if(iterator.hasNext()) {
                    builder.append(" AND ");
                }
            }
        }

        try(PreparedStatement statement = sqlTable.getSuperSQL().getConnection().prepareStatement(builder.append(";").toString())) {
            int index = 1;
            for (Object value : where.values()) {
                statement.setObject(index++, value);
            }

            return new SQLResult(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}