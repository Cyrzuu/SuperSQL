package me.cyrzu.git.supersql;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class UpdateBuilder {

    @NotNull
    private final JavaPlugin plugin;

    @NotNull
    private final PreparedStatement statement;

    private int index = 1;

    @SneakyThrows
    public UpdateBuilder(@NotNull SQLTable table) {
        this.plugin = table.getSuperSQL().getPlugin();
        this.statement = table.getSuperSQL().getConnection().prepareStatement(table.getINSERT());
    }

    @SneakyThrows
    public UpdateBuilder(@NotNull SQLTable table, @NotNull PreparedStatement statement) {
        this.plugin = table.getSuperSQL().getPlugin();
        this.statement = statement;
    }

    @SneakyThrows
    public UpdateBuilder put(@NotNull String value) {
        statement.setString(index++, value);
        return this;
    }

    @SneakyThrows
    public UpdateBuilder put(int value) {
        statement.setInt(index++, value);
        return this;
    }

    @SneakyThrows
    public UpdateBuilder put(long value) {
        statement.setLong(index++, value);
        return this;
    }

    @SneakyThrows
    public UpdateBuilder put(double value) {
        statement.setDouble(index++, value);
        return this;
    }

    @SneakyThrows
    public UpdateBuilder put(byte[] value) {
        statement.setBytes(index++, value);
        return this;
    }

    @NotNull
    public PreparedStatement build() {
        return statement;
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
        try {
            long start = System.currentTimeMillis();
            statement.executeUpdate();
            return System.currentTimeMillis() - start;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
