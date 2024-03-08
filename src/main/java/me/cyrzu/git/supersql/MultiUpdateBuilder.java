package me.cyrzu.git.supersql;

import lombok.SneakyThrows;
import me.cyrzu.git.supersql.sql.SuperSQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.function.Consumer;

public class MultiUpdateBuilder<T extends SQLObject> {

    @NotNull
    private final JavaPlugin plugin;

    @NotNull
    private final SQLTable sqlTable;

    @NotNull
    private final Collection<T> values;

    @NotNull
    private final PreparedStatement statement;

    private boolean builded = false;

    @SneakyThrows
    public MultiUpdateBuilder(@NotNull SQLTable sqlTable, @NotNull Collection<T> values) {
        if(values.isEmpty()) {
            throw new RuntimeException("The `MultiUpdateBuilder` could not be created due to an empty collection");
        }

        SuperSQL superSQL = sqlTable.getSuperSQL();

        this.plugin = superSQL.getPlugin();
        this.sqlTable = sqlTable;
        this.values = values;

        String statment = superSQL.getType().insertAndUpdate(sqlTable, values.size());
        this.statement = superSQL.getConnection().prepareStatement(statment);
    }

    public PreparedStatement build() {
        if(builded) {
            return statement;
        }

        UpdateBuilder builder = sqlTable.updateBuilder(statement);
        for (T value : values) {
            value.updateObject(builder);
        }

        this.builded = true;
        return this.statement;
    }


    public void executeAsync() {
        executeAsync(null);
    }

    public void executeAsync(@Nullable Consumer<Long> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            long index = execute();
            if(consumer != null) {
                consumer.accept(index);
            }
        });
    }

    public long execute() {
        if(values.isEmpty()) {
            return 0;
        }

        PreparedStatement statement = this.build();
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
