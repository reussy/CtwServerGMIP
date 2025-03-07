package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class TeamHandler {
    private final CTW ctw;
    private final List<Player> spectator;
    private final List<Player> redTeam;
    private final List<Player> blueTeam;

    public TeamHandler(final CTW ctw) {
        this.spectator = new ArrayList<Player>();
        this.redTeam = new ArrayList<Player>();
        this.blueTeam = new ArrayList<Player>();
        this.ctw = ctw;
    }

    public void addSpectator(final Player p) {
        this.redTeam.remove(p);
        this.blueTeam.remove(p);
        this.spectator.add(p);
    }

    public boolean isSpectator(final Player p) {
        return this.spectator.contains(p);
    }

    public boolean isRedTeam(final Player p) {
        return this.redTeam.contains(p);
    }

    public boolean isBlueTeam(final Player p) {
        return this.blueTeam.contains(p);
    }

    public void addRedTeam(final Player p) {
        this.spectator.remove(p);
        this.blueTeam.remove(p);
        this.redTeam.add(p);
        this.setRedSuit(p);
    }

    public void addBlueTeam(final Player p) {
        this.spectator.remove(p);
        this.redTeam.remove(p);
        this.blueTeam.add(p);
        this.setBlueSuit(p);
    }

    public Integer countSpectators() {
        final int amount = this.spectator.size();
        return amount;
    }

    public Integer countRedTeam() {
        final int amount = this.redTeam.size();
        return amount;
    }

    public Integer countBlueTeam() {
        final int amount = this.blueTeam.size();
        return amount;
    }

    public Team getTeam(final Player p) {
        Team team = null;
        if (this.spectator.contains(p)) {
            team = Team.SPECTATOR;
        } else if (this.redTeam.contains(p)) {
            team = Team.RED;
        } else if (this.blueTeam.contains(p)) {
            team = Team.BLUE;
        }
        return team;
    }

    public List<Player> spectatorsCopy() {
        final List<Player> members = new ArrayList<Player>(this.spectator);
        return members;
    }

    public List<Player> redTeamCopy() {
        final List<Player> members = new ArrayList<Player>(this.redTeam);
        return members;
    }

    public List<Player> blueTeamCopy() {
        final List<Player> members = new ArrayList<Player>(this.blueTeam);
        return members;
    }

    public void removeOnDisconnect(final Player p) {
        this.redTeam.remove(p);
        this.blueTeam.remove(p);
        this.spectator.remove(p);
    }

    public void setRedSuit(final Player p) {
        final ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta lamHelmet = (LeatherArmorMeta) helmet.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamHelmet.setColor(Color.fromRGB(253, 127, 53));
        } else {
            lamHelmet.setColor(Color.fromRGB(255, 85, 85));
        }
        helmet.setItemMeta(lamHelmet);
        final ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        final LeatherArmorMeta lamChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamChestplate.setColor(Color.fromRGB(253, 127, 53));
        } else {
            lamChestplate.setColor(Color.fromRGB(255, 85, 85));
        }
        chestplate.setItemMeta(lamChestplate);
        final ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta lamLeggings = (LeatherArmorMeta) leggings.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamLeggings.setColor(Color.fromRGB(253, 127, 53));
        } else {
            lamLeggings.setColor(Color.fromRGB(255, 85, 85));
        }
        leggings.setItemMeta(lamLeggings);
        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta lamBoots = (LeatherArmorMeta) boots.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamBoots.setColor(Color.fromRGB(253, 127, 53));
        } else {
            lamBoots.setColor(Color.fromRGB(255, 85, 85));
        }
        boots.setItemMeta(lamBoots);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
    }

    public void setBlueSuit(final Player p) {
        final ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        final LeatherArmorMeta lamHelmet = (LeatherArmorMeta) helmet.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamHelmet.setColor(Color.fromRGB(154, 56, 252));
        } else {
            lamHelmet.setColor(Color.fromRGB(85, 85, 255));
        }
        helmet.setItemMeta(lamHelmet);
        final ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        final LeatherArmorMeta lamChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamChestplate.setColor(Color.fromRGB(154, 56, 252));
        } else {
            lamChestplate.setColor(Color.fromRGB(85, 85, 255));
        }
        chestplate.setItemMeta(lamChestplate);
        final ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        final LeatherArmorMeta lamLeggings = (LeatherArmorMeta) leggings.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamLeggings.setColor(Color.fromRGB(154, 56, 252));
        } else {
            lamLeggings.setColor(Color.fromRGB(85, 85, 255));
        }
        leggings.setItemMeta(lamLeggings);
        final ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        final LeatherArmorMeta lamBoots = (LeatherArmorMeta) boots.getItemMeta();
        if (p.hasPermission("CTW.fancySuit")) {
            lamBoots.setColor(Color.fromRGB(154, 56, 252));
        } else {
            lamBoots.setColor(Color.fromRGB(85, 85, 255));
        }
        boots.setItemMeta(lamBoots);
        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
    }

    public enum Team {
        SPECTATOR("SPECTATOR", 0),
        RED("RED", 1),
        BLUE("BLUE", 2);

        Team(final String s, final int n) {
        }
    }
}
