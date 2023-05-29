package me.TahaCheji;

import org.bukkit.plugin.java.JavaPlugin;

public final class MafanaStashNetwork extends JavaPlugin {

    private static MafanaStashNetwork instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MafanaStashNetwork getInstance() {
        return instance;
    }
}
