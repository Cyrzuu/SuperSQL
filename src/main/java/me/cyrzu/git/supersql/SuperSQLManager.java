package me.cyrzu.git.supersql;

import me.cyrzu.git.supersql.column.IntegerColumn;
import me.cyrzu.git.supersql.column.StringColumn;
import me.cyrzu.git.supersql.column.VarcharColumn;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuperSQLManager {

    private @Nullable static SuperSQLManager manager = null;

    public static void registerManager(JavaPlugin instance) {
        if(manager != null) {
            throw new RuntimeException("Manager is registered");
        }

        manager = new SuperSQLManager(instance);

        SQLTable sqlTable = SQLTable.builder("xD")
                .add(new VarcharColumn("uuid", 36).primaryKey())
                .add(new StringColumn("username").notNull())
                .add(new IntegerColumn("kills"))
                .build();


    }

    public static @NotNull SuperSQLManager getManager() {
        if(manager == null) {
            throw new RuntimeException("Manager is not registered");
        }

        return manager;
    }

    private final JavaPlugin instance;

    private SuperSQLManager(JavaPlugin instance) {
        this.instance = instance;
    }

    public JavaPlugin getInstance() {
        return instance;
    }

}
