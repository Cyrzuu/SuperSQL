package me.cyrzu.git.supersql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DeleteBuilder {

    @NotNull
    private final SQLTable sqlTable;

    @NotNull
    private final Map<String, Object> where;

    public DeleteBuilder(@NotNull SQLTable sqlTable) {
        this.sqlTable = sqlTable;
        this.where = new LinkedHashMap<>();
    }

    public DeleteBuilder where(@NotNull String key, @NotNull String value) {
        where.put(key, value);
        return this;
    }

    public DeleteBuilder where(@NotNull String key, @NotNull Integer value) {
        where.put(key, value);
        return this;
    }

    public DeleteBuilder where(@NotNull String key, @NotNull Long value) {
        where.put(key, value);
        return this;
    }

    public void executeAsync() {
        executeAsync((ms) -> {});
    }

    public void executeAsync(@NotNull Consumer<Long> consumer) {
        JavaPlugin plugin = sqlTable.getSuperSQL().getPlugin();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> consumer.accept(execute()));
    }

    public long execute() {
        long start = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder("DELETE FROM ").append(sqlTable.getName());

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

        try {
            PreparedStatement statement = sqlTable.getSuperSQL().getConnection().prepareStatement(builder.append(";").toString());

            int index = 1;
            for (Object value : where.values()) {
                statement.setObject(index++, value);
            }

            statement.executeUpdate();
            return System.currentTimeMillis() - start;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}