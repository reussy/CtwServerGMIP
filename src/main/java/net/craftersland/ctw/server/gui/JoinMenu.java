package net.craftersland.ctw.server.gui;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JoinMenu {
    public static Inventory inventory;
    private final String title;
    private final int size;
    private final int redTeamSlot;
    private final int blueTeamSlot;
    private final int autoJoinSlot;

    static {
        inventory = null;
    }

    private final CTW ctw;

    public JoinMenu(final @NotNull CTW ctw) {
        this.ctw = ctw;
        this.title = ctw.getConfigHandler().getString("Menus.Join.Title");
        this.size = ctw.getConfigHandler().getInteger("Menus.Join.Size");
        this.redTeamSlot = ctw.getConfigHandler().getInteger("Menus.Join.Items.RedTeam.Slot");
        this.blueTeamSlot = ctw.getConfigHandler().getInteger("Menus.Join.Items.BlueTeam.Slot");
        this.autoJoinSlot = ctw.getConfigHandler().getInteger("Menus.Join.Items.AutoJoin.Slot");
    }

    public Inventory JoinMenuGUI(final Player player) {
        if (inventory == null) {
            try {
                inventory = Bukkit.createInventory(player, this.size, this.title);
                inventory.setItem(autoJoinSlot, this.autoJoinItem());
                inventory.setItem(redTeamSlot, this.joinRedTeam());
                inventory.setItem(blueTeamSlot, this.joinBlueTeam());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inventory;
    }

    public ItemStack autoJoinItem() {
        final ItemStack item = new ItemStack(Material.valueOf(ctw.getConfigHandler().getString("Menus.Join.Items.AutoJoin.Material")), 1, Short.parseShort(ctw.getConfigHandler().getInteger("Menus.Join.Items.AutoJoin.Data").toString()));
        final List<String> lore = new ArrayList<>(replacePlaceholders("Menus.Join.Items.AutoJoin.Lore"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Menus.Join.Items.AutoJoin.Name"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack joinRedTeam() {
        final ItemStack item = new ItemStack(Material.valueOf(ctw.getConfigHandler().getString("Menus.Join.Items.RedTeam.Material")), 1, Short.parseShort(ctw.getConfigHandler().getInteger("Menus.Join.Items.RedTeam.Data").toString()));
        final List<String> lore = new ArrayList<>(replacePlaceholders("Menus.Join.Items.RedTeam.Lore"));
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Menus.Join.Items.RedTeam.Name"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack joinBlueTeam() {
        final ItemStack item = new ItemStack(Material.valueOf(ctw.getConfigHandler().getString("Menus.Join.Items.BlueTeam.Material")), 1, Short.parseShort(ctw.getConfigHandler().getInteger("Menus.Join.Items.BlueTeam.Data").toString()));
        final List<String> lore = new ArrayList<>(replacePlaceholders("Menus.Join.Items.BlueTeam.Lore"));
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.ctw.getConfigHandler().getString("Menus.Join.Items.BlueTeam.Name"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public String joinMenuTitle() {
        return this.ctw.getConfigHandler().getString("Menus.Join.Title");
    }

    public void menuUpdateTask() {
        if (inventory != null) {
            try {
                if (!inventory.getViewers().isEmpty()) {
                    inventory.setItem(autoJoinSlot, this.autoJoinItem());
                    inventory.setItem(redTeamSlot, this.joinRedTeam());
                    inventory.setItem(blueTeamSlot, this.joinBlueTeam());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private @NotNull List<String> replacePlaceholders(String path) {
        List<String> lore = new ArrayList<>();
        for (String line : this.ctw.getConfigHandler().getStringList(path)) {
            line = line.replace("%RedTeamCount%", this.ctw.getTeamHandler().countRedTeam().toString())
                    .replace("%BlueTeamCount%", this.ctw.getTeamHandler().countBlueTeam().toString())
                    .replace("%SpectatorCount%", this.ctw.getTeamHandler().countSpectators().toString())
                    .replace("%TeamMax%", String.valueOf(this.ctw.getMapConfigHandler().maxPlayers));
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return lore;
    }
}
