package me.TahaCheji;

import me.TahaCheji.stashData.GamePlayerStash;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainStash extends JavaPlugin {

    private static MainStash instance;
    private GamePlayerStash gamePlayerStash = new GamePlayerStash();
    @Override
    public void onEnable() {
        instance = this;
        gamePlayerStash.connect();
    }

    @Override
    public void onDisable() {
        gamePlayerStash.disconnect();
    }

    public GamePlayerStash getGamePlayerStash() {
        return gamePlayerStash;
    }

    public static MainStash getInstance() {
        return instance;
    }
}
