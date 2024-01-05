package me.cyrzu.git.supersql;

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

    public UpdateBuilder(@NotNull SQLTable table) {
        try {
            this.plugin = table.getSuperSQL().getPlugin();
            this.statement = table.getSuperSQL().getConnection().prepareStatement(table.getINSERT());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(@NotNull String value) {
        try {
            statement.setString(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(int value) {
        try {
            statement.setInt(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(long value) {
        try {
            statement.setLong(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(double value) {
        try {
            statement.setDouble(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateBuilder put(byte[] value) {
        try {
            statement.setBytes(index++, value);
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public PreparedStatement build() {
        return statement;
    }

    public void executeAsync() {
        executeAsync(null);
    }

    public void executeAsync(@Nullable Consumer<Integer> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           int index = execute();
           if(consumer != null) {
               consumer.accept(index);
           }
        });
    }

    public int execute() {
        try {
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
