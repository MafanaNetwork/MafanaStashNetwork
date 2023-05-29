package me.TahaCheji.event;

import me.TahaCheji.MainStash;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GamePlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        MainStash.getInstance().getGamePlayerStash().addPlayer(event.getPlayer());
    }

}
