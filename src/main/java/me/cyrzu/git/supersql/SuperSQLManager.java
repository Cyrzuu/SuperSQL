package me.cyrzu.git.supersql;

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
