package me.TahaCheji.stashData;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GamePlayerStash extends MySQL {


    public GamePlayerStash() {
        super("localhost", "3306", "mafanation", "root", "");
    }
    SQLGetter sqlGetter = new SQLGetter(this);
    @Override
    public void setSqlGetter(SQLGetter sqlGetter) {
        this.sqlGetter = sqlGetter;
    }

    public void addPlayer(OfflinePlayer player) {
        if(!sqlGetter.exists(player.getUniqueId())) {
            sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
            sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), ""));
            sqlGetter.setUUID(new MysqlValue("UUID", player.getUniqueId(), player.getUniqueId()));
        }
    }

    public void setItems(OfflinePlayer player, List<ItemStack> i) {
        sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
        sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), new StashDataHandler().encodeItems(i)));
    }

    public List<ItemStack> getItems(OfflinePlayer player) {
        try {
            return new StashDataHandler().decodeItems(sqlGetter.getString(player.getUniqueId(), new MysqlValue("ITEMS")));
        } catch (Exception e) {
            return null;
        }
    }

    public String getRawItems(OfflinePlayer player) {
        return sqlGetter.getString(player.getUniqueId(), new MysqlValue("ITEMS"));
    }

    public void addItem(OfflinePlayer player, ItemStack itemStack) {
        List<ItemStack> i = getItems(player);
        i.add(itemStack);
        String s = new StashDataHandler().encodeItems(i);
        sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
        sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), s));
    }

    public void removeItem(OfflinePlayer player, ItemStack itemStack) {
        List<ItemStack> i = getItems(player);
        for(ItemStack item : i) {
            if(item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                i.remove(item);
                break;
            }
        }
        String s = new StashDataHandler().encodeItems(i);
        sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
        sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), s));
    }

    public void pickUpStash(Player player) {
        for(ItemStack itemStack : getItems(player)) {
            if(player.getInventory().isEmpty()) {
                player.getInventory().addItem(itemStack);
                player.sendMessage(ChatColor.GREEN + "+" + itemStack.getItemMeta().getDisplayName());
            } else {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            }
            removeItem(player, itemStack);
        }
    }

    @Override
    public void connect() {
        super.connect();
        if(this.isConnected()) sqlGetter.createTable("players_stash", new MysqlValue("NAME", ""),
                new MysqlValue("ITEMS", ""));
    }
}
