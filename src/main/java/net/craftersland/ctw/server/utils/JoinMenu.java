package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class JoinMenu {
    private final CTW ctw;
    public static Inventory inv;

    static {
        JoinMenu.inv = null;
    }

    public JoinMenu(final CTW ctw) {
        this.ctw = ctw;
    }

    public Inventory JoinMenuGUI(final Player p) {
        if (JoinMenu.inv == null) {
            try {
                (JoinMenu.inv = Bukkit.createInventory(p, 9, this.joinMenuTitle())).setItem(4, this.autoJoinItem());
                JoinMenu.inv.setItem(2, this.joinRedTeam());
                JoinMenu.inv.setItem(6, this.joinBlueTeam());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JoinMenu.inv;
    }

    public ItemStack autoJoinItem() {
        final ItemStack item = new ItemStack(Material.NETHER_STAR);
        final List<String> lore = new ArrayList<String>();
        for (String msg : this.ctw.getLanguageHandler().getMessageList("JoinGUI.AutoJoinItem.Lore")) {
            msg = msg.replaceAll("&", "§");
            msg = msg.replaceAll("%RedTeamCount%", this.ctw.getTeamHandler().countRedTeam().toString());
            msg = msg.replaceAll("%BlueTeamCount%", this.ctw.getTeamHandler().countBlueTeam().toString());
            msg = msg.replaceAll("%SpectatorCount%", this.ctw.getTeamHandler().countSpectators().toString());
            msg = msg.replaceAll("%TeamMax%", new StringBuilder(String.valueOf(this.ctw.getMapConfigHandler().maxPlayers)).toString());
            lore.add(msg);
        }
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("JoinGUI.AutoJoinItem.Title"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack joinRedTeam() {
        final ItemStack item = new ItemStack(Material.BANNER, 1, (short) 1);
        final List<String> lore = new ArrayList<String>();
        for (String msg : this.ctw.getLanguageHandler().getMessageList("JoinGUI.JoinRedTeamItem.Lore")) {
            msg = msg.replaceAll("&", "§");
            msg = msg.replaceAll("%RedTeamCount%", this.ctw.getTeamHandler().countRedTeam().toString());
            msg = msg.replaceAll("%BlueTeamCount%", this.ctw.getTeamHandler().countBlueTeam().toString());
            msg = msg.replaceAll("%SpectatorCount%", this.ctw.getTeamHandler().countSpectators().toString());
            msg = msg.replaceAll("%TeamMax%", new StringBuilder(String.valueOf(this.ctw.getMapConfigHandler().maxPlayers)).toString());
            lore.add(msg);
        }
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("JoinGUI.JoinRedTeamItem.Title"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack joinBlueTeam() {
        final ItemStack item = new ItemStack(Material.BANNER, 1, (short) 4);
        final List<String> lore = new ArrayList<String>();
        for (String msg : this.ctw.getLanguageHandler().getMessageList("JoinGUI.JoinBlueTeamItem.Lore")) {
            msg = msg.replaceAll("&", "§");
            msg = msg.replaceAll("%RedTeamCount%", this.ctw.getTeamHandler().countRedTeam().toString());
            msg = msg.replaceAll("%BlueTeamCount%", this.ctw.getTeamHandler().countBlueTeam().toString());
            msg = msg.replaceAll("%SpectatorCount%", this.ctw.getTeamHandler().countSpectators().toString());
            msg = msg.replaceAll("%TeamMax%", new StringBuilder(String.valueOf(this.ctw.getMapConfigHandler().maxPlayers)).toString());
            lore.add(msg);
        }
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("JoinGUI.JoinBlueTeamItem.Title"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public String joinMenuTitle() {
        return this.ctw.getLanguageHandler().getMessage("JoinGUI.Title");
    }

    public void menuUpdateTask() {
        if (JoinMenu.inv != null) {
            try {
                if (!JoinMenu.inv.getViewers().isEmpty()) {
                    JoinMenu.inv.setItem(4, this.autoJoinItem());
                    JoinMenu.inv.setItem(2, this.joinRedTeam());
                    JoinMenu.inv.setItem(6, this.joinBlueTeam());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
