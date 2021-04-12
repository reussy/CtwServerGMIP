package net.craftersland.ctw.server.kits.enchants;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.achievements.WoolAchievementHandler;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ProtectionII {
    private final CTW ctw;
    private final Double price;
    private final WoolAchievementHandler.WoolAchievements requiredAchievement;

    public ProtectionII(final CTW ctw) {
        this.price = 60.0;
        this.requiredAchievement = WoolAchievementHandler.WoolAchievements.WOOLMASTER4;
        this.ctw = ctw;
    }

    public ItemStack setKitItem(final Player p) {
        final boolean unlocked = this.isKitUnlocked(p);
        final ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        final ItemMeta meta = item.getItemMeta();
        if (unlocked) {
            meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Enchants.ProtectionII.NameUnlocked").replaceAll("&", "§"));
        } else {
            meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Enchants.ProtectionII.NameLocked").replaceAll("&", "§"));
        }
        final ArrayList<String> lore = new ArrayList<String>();
        for (final String s : this.ctw.getLanguageHandler().getMessageList("Enchants.ProtectionII.BaseLore")) {
            lore.add(s.replaceAll("&", "§"));
        }
        if (unlocked) {
            for (String s : this.ctw.getLanguageHandler().getMessageList("Enchants.ProtectionII.UnlockedLore")) {
                s = s.replaceAll("%price%", new StringBuilder(String.valueOf(this.price.intValue())).toString());
                lore.add(s.replaceAll("&", "§"));
            }
        } else {
            for (String s : this.ctw.getLanguageHandler().getMessageList("Enchants.ProtectionII.LockedLore")) {
                s = s.replaceAll("%achievement%", this.ctw.getWoolAchievementHandler().getAchievementName(this.requiredAchievement));
                lore.add(s.replaceAll("&", "§"));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isKitUnlocked(final Player p) {
        return p.hasPermission("CTW.kit.bypassrequirements") || this.ctw.getWoolAchievementHandler().hasAchievement(p, this.requiredAchievement);
    }

    public void giveKit(final Player p) {
        if (this.isKitUnlocked(p)) {
            final Double balance = CTW.economy.getBalance(p);
            if (balance >= this.price) {
                final ItemStack item = p.getItemInHand();
                if (item != null) {
                    if (item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS) {
                        this.setEnchant(p, item);
                    }
                    if (item.getType() == Material.CHAINMAIL_HELMET || item.getType() == Material.CHAINMAIL_CHESTPLATE || item.getType() == Material.CHAINMAIL_LEGGINGS || item.getType() == Material.CHAINMAIL_BOOTS) {
                        this.setEnchant(p, item);
                    }
                    if (item.getType() == Material.GOLD_HELMET || item.getType() == Material.GOLD_CHESTPLATE || item.getType() == Material.GOLD_LEGGINGS || item.getType() == Material.GOLD_BOOTS) {
                        this.setEnchant(p, item);
                    }
                    if (item.getType() == Material.IRON_HELMET || item.getType() == Material.IRON_CHESTPLATE || item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.IRON_BOOTS) {
                        this.setEnchant(p, item);
                    }
                    if (item.getType() == Material.DIAMOND_HELMET || item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.DIAMOND_BOOTS) {
                        this.setEnchant(p, item);
                    } else {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.PlaceArmorInHand").replaceAll("&", "§"));
                    }
                } else {
                    this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.PlaceArmorInHand").replaceAll("&", "§"));
                }
            } else {
                this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                String s = this.ctw.getLanguageHandler().getMessage("ChatMessages.NotEnoughCoins").replaceAll("%coinsNeeded%", new StringBuilder(String.valueOf(this.price.intValue())).toString());
                s = s.replaceAll("%balance%", new StringBuilder(String.valueOf(balance.intValue())).toString());
                p.sendMessage(s.replaceAll("&", "§"));
            }
        } else {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            String s2 = this.ctw.getLanguageHandler().getMessage("ChatMessages.EnchantLockedWools").replaceAll("%achievement%", this.ctw.getWoolAchievementHandler().getAchievementName(this.requiredAchievement));
            s2 = s2.replaceAll("%amount%", new StringBuilder().append(this.ctw.getWoolAchievementHandler().getRequiredKills(this.requiredAchievement)).toString());
            p.sendMessage(s2.replaceAll("&", "§"));
        }
    }

    private void setEnchant(final Player p, final ItemStack item) {
        final Enchantment enchant = Enchantment.PROTECTION_ENVIRONMENTAL;
        if (!this.hasEnchant(item, enchant)) {
            CTW.economy.withdrawPlayer(p, this.price);
            item.addEnchantment(enchant, 2);
            this.ctw.getSoundHandler().sendAnvilLandSound(p.getLocation(), p);
            final Double bb = CTW.economy.getBalance(p);
            final String s = this.ctw.getLanguageHandler().getMessage("ChatMessages.EnchantSet").replaceAll("%balance%", new StringBuilder(String.valueOf(bb.intValue())).toString());
            p.sendMessage(s.replaceAll("&", "§"));
        } else {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.EnchantConflict").replaceAll("&", "§"));
        }
    }

    private boolean hasEnchant(final ItemStack item, final Enchantment enchant) {
        if (!item.getEnchantments().isEmpty()) {
            for (final Enchantment e : item.getEnchantments().keySet()) {
                if (e == enchant || e.conflictsWith(enchant)) {
                    return true;
                }
            }
        }
        return false;
    }
}
