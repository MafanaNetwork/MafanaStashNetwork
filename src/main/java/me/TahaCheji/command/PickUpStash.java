package me.TahaCheji.command;

import me.TahaCheji.MainStash;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PickUpStash implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("stash")) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("add")) {
                MainStash.getInstance().getGamePlayerStash().addItem(player, player.getItemInHand());
                return true;
            }
            if(args[0].equalsIgnoreCase("clear")) {
                MainStash.getInstance().getGamePlayerStash().setItems(player, null);
                return true;
            }
            if(args[0].equalsIgnoreCase("pickup")) {
                if (MainStash.getInstance().getGamePlayerStash().getItems(player) == null) {
                    player.sendMessage(ChatColor.RED + "There is nothing in your stash");
                    return true;
                }
                MainStash.getInstance().getGamePlayerStash().pickUpStash(player);
            }
        }
        return false;
    }
}
