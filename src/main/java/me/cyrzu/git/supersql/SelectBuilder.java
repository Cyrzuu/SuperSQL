package me.cyrzu.git.supersql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class SelectBuilder {

    @NotNull
    private final JavaPlugin plugin;

    @NotNull
    private final SQLTable sqlTable;

    @NotNull
    private final Set<String> columns;

    @NotNull
    private final Map<String, Object> where;

    @Nullable
    private Pair<String, Boolean> order;

    private int limit = 0;

    public SelectBuilder(@NotNull SQLTable sqlTable) {
        this.plugin = sqlTable.getSuperSQL().getPlugin();
        this.sqlTable = sqlTable;
        this.columns = new HashSet<>();
        this.where = new LinkedHashMap<>();
    }

    public SelectBuilder(@NotNull SQLTable sqlTable, @NotNull String... columns) {
        this.plugin = sqlTable.getSuperSQL().getPlugin();
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

    public SelectBuilder order(@NotNull String column, boolean descending) {
        order = new Pair<>(column, descending);
        return this;
    }

    public SelectBuilder limit(int limit) {
        this.limit = Math.max(1, limit);
        return this;
    }

    public void executeAsync(@NotNull Consumer<@NotNull SQLResult> result) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> result.accept(execute()));
    }

    @NotNull
    public SQLResult execute() {
        StringBuilder builder = new StringBuilder("SELECT");

        if(columns.isEmpty()) {
            builder.append(" *");
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
            builder.append(" WHERE ");

            Iterator<Map.Entry<String, Object>> iterator = where.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                builder.append(next.getKey()).append(" = ?");

                if(iterator.hasNext()) {
                    builder.append(" AND ");
                }
            }
        }

        if(order != null) {
            builder.append(" ORDER BY ").append(order.getKey());
            if(order.getValue()) {
                builder.append(" DESC");
            }
        }

        if(limit > 0) {
            builder.append("LIMIT ").append(limit);
        }

        try {
            PreparedStatement statement = sqlTable.getSuperSQL().getConnection().prepareStatement(builder.append(";").toString());

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