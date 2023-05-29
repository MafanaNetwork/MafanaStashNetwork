package me.TahaCheji.stashData;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

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
        ItemStack[] itemStacks = i.toArray(new ItemStack[0]);
        sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
        sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), new StashDataHandler().encodeItems(itemStacks)));
    }

    public List<ItemStack> getItems(OfflinePlayer player) {
        try {
            return Arrays.asList(new StashDataHandler().decodeItems(sqlGetter.getString(player.getUniqueId(), new MysqlValue("ITEMS"))));
        } catch (Exception e) {
            return null;
        }
    }


    public String getRawItems(OfflinePlayer player) {
        return sqlGetter.getString(player.getUniqueId(), new MysqlValue("ITEMS"));
    }

    public void addItem(OfflinePlayer player, ItemStack itemStack) {
        List<ItemStack> i = getItems(player);
        if (i == null) {
            List<ItemStack> itemStacks = new ArrayList<>();
            itemStacks.add(itemStack);
            String s = new StashDataHandler().encodeItems(itemStacks.toArray(new ItemStack[0]));
            sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
            sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), s));
            return;
        }
        List<ItemStack> updatedItems = new ArrayList<>(i); // Create a new ArrayList from i
        updatedItems.add(itemStack);
        String s = new StashDataHandler().encodeItems(updatedItems.toArray(new ItemStack[0]));
        sqlGetter.setString(new MysqlValue("NAME", player.getUniqueId(), player.getName()));
        sqlGetter.setString(new MysqlValue("ITEMS", player.getUniqueId(), s));
    }


    public void removeItem(OfflinePlayer player, ItemStack itemsToRemove) {
        List<ItemStack> items = new ArrayList<>(getItems(player));
        Iterator<ItemStack> iterator = items.iterator();
        while (iterator.hasNext()) {
            ItemStack itemStack = iterator.next();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
                if (itemsToRemove.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                    iterator.remove();
                    break;
            }
        }
        setItems(player, items);
    }

    public void pickUpStash(Player player) {
        List<ItemStack> items = new ArrayList<>(getItems(player));
        if(items.size() == 0) {
            player.sendMessage("No items in your stash");
            return;
        }
        if (items != null) {
            List<ItemStack> itemsToRemove = new ArrayList<>();

            for (ItemStack itemStack : items) {
                if (!player.getInventory().isEmpty()) {
                    player.getInventory().addItem(itemStack);
                    player.sendMessage(ChatColor.GREEN + "+" + itemStack.getItemMeta().getDisplayName() + " x" + itemStack.getAmount());
                    itemsToRemove.add(itemStack);
                } else {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                }
            }

            Iterator<ItemStack> iterator = items.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next();
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }
                for (ItemStack i : itemsToRemove) {
                    if (i == null || i.getType() == Material.AIR) {
                        continue;
                    }
                    if (i.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                        iterator.remove();
                        break;
                    }
                }
            }

            setItems(player, items);
        }
    }


    private void removeItems(OfflinePlayer player, List<ItemStack> itemsToRemove) {
        List<ItemStack> items = new ArrayList<>(getItems(player));
        Iterator<ItemStack> iterator = items.iterator();
        while (iterator.hasNext()) {
            ItemStack itemStack = iterator.next();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            for (ItemStack i : itemsToRemove) {
                if (i == null || i.getType() == Material.AIR) {
                    continue;
                }
                if (i.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                    iterator.remove();
                    break;
                }
            }
        }

        setItems(player, items);
    }


    @Override
    public void connect() {
        super.connect();
        if(this.isConnected()) sqlGetter.createTable("players_stash", new MysqlValue("NAME", ""),
                new MysqlValue("ITEMS", ""));
    }
}
