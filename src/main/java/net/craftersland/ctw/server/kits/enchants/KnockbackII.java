package net.craftersland.ctw.server.kits.enchants;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class KnockbackII {
    private final CTW ctw;
    private final Double price;

    public KnockbackII(final CTW ctw) {
        this.price = 50.0;
        this.ctw = ctw;
    }

    public ItemStack setKitItem(final Player p) {
        final boolean unlocked = this.isKitUnlocked(p);
        final ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        final ItemMeta meta = item.getItemMeta();
        if (unlocked) {
            meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Enchants.KnockbackII.NameUnlocked").replaceAll("&", "§"));
        } else {
            meta.setDisplayName(this.ctw.getLanguageHandler().getMessage("Enchants.KnockbackII.NameLocked").replaceAll("&", "§"));
        }
        final ArrayList<String> lore = new ArrayList<String>();
        for (final String s : this.ctw.getLanguageHandler().getMessageList("Enchants.KnockbackII.BaseLore")) {
            lore.add(s.replaceAll("&", "§"));
        }
        if (unlocked) {
            for (String s : this.ctw.getLanguageHandler().getMessageList("Enchants.KnockbackII.UnlockedLore")) {
                s = s.replaceAll("%price%", new StringBuilder(String.valueOf(this.price.intValue())).toString());
                lore.add(s.replaceAll("&", "§"));
            }
        } else {
            for (final String s : this.ctw.getLanguageHandler().getMessageList("Enchants.KnockbackII.LockedLore")) {
                lore.add(s.replaceAll("&", "§"));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isKitUnlocked(final Player p) {
        return p.hasPermission("CTW.enchant.bypassrequirements") || p.hasPermission("CTW.enchant.KnockbackII");
    }

    public void giveKit(final Player p) {
        if (this.isKitUnlocked(p)) {
            final Double balance = CTW.economy.getBalance(p);
            if (balance >= this.price) {
                final ItemStack item = p.getItemInHand();
                if (item != null) {
                    if (item.getType() == Material.WOOD_SWORD || item.getType() == Material.STONE_SWORD || item.getType() == Material.GOLD_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD) {
                        this.setEnchant(p, item);
                    } else if (item.getType() == Material.WOOD_AXE || item.getType() == Material.STONE_AXE || item.getType() == Material.GOLD_AXE || item.getType() == Material.IRON_AXE || item.getType() == Material.DIAMOND_AXE) {
                        this.setEnchant(p, item);
                    } else {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.PlaceSwordOrAxe").replaceAll("&", "§"));
                    }
                } else {
                    this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.PlaceSwordOrAxe").replaceAll("&", "§"));
                }
            } else {
                this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                String s = this.ctw.getLanguageHandler().getMessage("ChatMessages.NotEnoughCoins").replaceAll("%coinsNeeded%", new StringBuilder(String.valueOf(this.price.intValue())).toString());
                s = s.replaceAll("%balance%", new StringBuilder(String.valueOf(balance.intValue())).toString());
                p.sendMessage(s.replaceAll("&", "§"));
            }
        } else {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.EnchantVIP").replaceAll("&", "§"));
        }
    }

    private void setEnchant(final Player p, final ItemStack item) {
        final Enchantment enchant = Enchantment.KNOCKBACK;
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
