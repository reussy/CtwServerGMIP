package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class StartupKit {
    private final CTW ctw;

    public StartupKit(final CTW ctw) {
        this.ctw = ctw;
    }

    public void saveStartupKit(final Player p) {
        for (int i = 0; i < p.getInventory().getSize(); ++i) {
            final ItemStack item = p.getInventory().getItem(i);
            this.ctw.getMapConfigHandler().saveStartupKit(item, i);
        }
    }

    public void giveStartupKit(final Player p) {
        p.setHealth(p.getMaxHealth());
        p.getInventory().setContents(this.ctw.getMapConfigHandler().startupKit);
    }

    public void setRedSuit(final Player p) {
        final ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta lamHelmet = (LeatherArmorMeta) helmet.getItemMeta();
        lamHelmet.setColor(Color.fromRGB(255, 85, 85));
        helmet.setItemMeta(lamHelmet);
        final ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        final LeatherArmorMeta lamChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
        lamChestplate.setColor(Color.fromRGB(255, 85, 85));
        chestplate.setItemMeta(lamChestplate);
        final ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta lamLeggings = (LeatherArmorMeta) leggings.getItemMeta();
        lamLeggings.setColor(Color.fromRGB(255, 85, 85));
        leggings.setItemMeta(lamLeggings);
        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta lamBoots = (LeatherArmorMeta) boots.getItemMeta();
        lamBoots.setColor(Color.fromRGB(255, 85, 85));
        boots.setItemMeta(lamBoots);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
    }

    public void setBlueSuit(final Player p) {
        final ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta lamHelmet = (LeatherArmorMeta) helmet.getItemMeta();
        lamHelmet.setColor(Color.fromRGB(85, 85, 255));
        helmet.setItemMeta(lamHelmet);
        final ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        final LeatherArmorMeta lamChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
        lamChestplate.setColor(Color.fromRGB(85, 85, 255));
        chestplate.setItemMeta(lamChestplate);
        final ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta lamLeggings = (LeatherArmorMeta) leggings.getItemMeta();
        lamLeggings.setColor(Color.fromRGB(85, 85, 255));
        leggings.setItemMeta(lamLeggings);
        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta lamBoots = (LeatherArmorMeta) boots.getItemMeta();
        lamBoots.setColor(Color.fromRGB(85, 85, 255));
        boots.setItemMeta(lamBoots);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
    }
}
