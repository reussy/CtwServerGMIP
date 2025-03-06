package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TakenWools {
    private final CTW ctw;

    public TakenWools(final CTW ctw) {
        this.ctw = ctw;
    }

    public void redWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (!this.ctw.getWoolHandler().isRedTaken()) {
                this.ctw.getNewScoreboardHandler().redWoolTaken();
                this.ctw.getWoolHandler().addRedTakenByPlayer(p);
                this.ctw.getWoolHandler().setRedTaken();
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.subtitle"));
                this.announcePickup(p, "RED", true);
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            } else if (!this.ctw.getWoolHandler().hadRedTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addRedTakenByPlayer(p);
               this.ctw.getTeamWoolsCaptured().addRedCaptured();
                this.announcePickup(p, "RED", false);
            }
            setNewEquipment(p);
        }
    }

    public void pinkWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (!this.ctw.getWoolHandler().isPinkTaken()) {
                this.ctw.getNewScoreboardHandler().pinkWoolTaken();
                this.ctw.getWoolHandler().addPinkTakenByPlayer(p);
                this.ctw.getWoolHandler().setPinkTaken();
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.subtitle"));
                this.announcePickup(p, "PINK", true);
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            } else if (!this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addPinkTakenByPlayer(p);
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
                this.announcePickup(p, "PINK", false);
            }
            setNewEquipment(p);
        }
    }

    public void blueWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.BLUE) {
            if (!this.ctw.getWoolHandler().isBlueTaken()) {
                this.ctw.getNewScoreboardHandler().blueWoolTaken();
                this.ctw.getWoolHandler().addBlueTakenByPlayer(p);
                this.ctw.getWoolHandler().setBlueTaken();
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.subtitle"));
                this.announcePickup(p, "BLUE", true);
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            } else if (!this.ctw.getWoolHandler().hadBlueTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addBlueTakenByPlayer(p);
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.BlueWoolCapture").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
                this.announcePickup(p, "BLUE", false);
            }
            setNewEquipment(p);
        }
    }

    public void cyanWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.BLUE) {
            if (!this.ctw.getWoolHandler().isCyanTaken()) {
                this.ctw.getNewScoreboardHandler().cyanWoolTaken();
                this.ctw.getWoolHandler().addCyanTakenByPlayer(p);
                this.ctw.getWoolHandler().setCyanTaken();
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.subtitle"));
                this.announcePickup(p, "CYAN", true);
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            } else if (!this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addCyanTakenByPlayer(p);
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
                this.announcePickup(p, "CYAN", false);
            }
            setNewEquipment(p);
        }
    }

    public void checkForLostWool(final Player p, final List<ItemStack> items) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (this.ctw.getWoolHandler().hadRedTakenByPlayer(p) && this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                final byte data1 = 14;
                final byte data2 = 6;
                if (this.woolLost(items, data1) && this.woolLost(items, data2)) {
                    this.ctw.getWoolHandler().removeRedTakenByPlayer(p);
                    this.ctw.getWoolHandler().removePinkTakenByPlayer(p);
                    this.announceLost(p, "ALL");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadRedTakenByPlayer(p)) {
                final byte data3 = 14;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeRedTakenByPlayer(p);
                    this.announceLost(p, "RED");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                final byte data3 = 6;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removePinkTakenByPlayer(p);
                    this.announceLost(p, "PINK");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            }
        } else if (team == TeamHandler.Teams.BLUE) {
            if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(p) && this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                final byte data1 = 11;
                final byte data2 = 9;
                if (this.woolLost(items, data1) && this.woolLost(items, data2)) {
                    this.ctw.getWoolHandler().removeBlueTakenByPlayer(p);
                    this.ctw.getWoolHandler().removeCyanTakenByPlayer(p);
                    this.announceLost(p, "ALL");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(p)) {
                final byte data3 = 11;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeBlueTakenByPlayer(p);
                    this.announceLost(p, "BLUE");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                final byte data3 = 9;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeCyanTakenByPlayer(p);
                    this.announceLost(p, "CYAN");
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            }
        }
    }

    private boolean woolLost(final @NotNull List<ItemStack> items, final byte data) {
        for (final ItemStack i : items) {
            if (i != null && i.getType() == Material.WOOL && i.getData().getData() == data) {
                return true;
            }
        }
        return false;
    }

    private void setNewEquipment(@NotNull Player p) {

        if (CTW.getPlayersAlreadyEquipped().contains(p.getUniqueId())) return;

        this.ctw.getSoundHandler().sendNewEquipmentSound(p.getLocation(), p);

        ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ironChestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemStack ironBoot = new ItemStack(Material.IRON_BOOTS);
        ironBoot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

        ItemMeta chestplateItemMeta = ironChestplate.getItemMeta();
        chestplateItemMeta.spigot().setUnbreakable(true);
        ironChestplate.setItemMeta(chestplateItemMeta);

        ItemMeta bootsMeta = ironBoot.getItemMeta();
        bootsMeta.spigot().setUnbreakable(true);
        ironBoot.setItemMeta(bootsMeta);

        p.getInventory().setChestplate(ironChestplate);
        p.getInventory().setBoots(ironBoot);
        p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 12));

        CTW.getPlayersAlreadyEquipped().add(p.getUniqueId());
        StartupKit.setUnbreakableArmor(p);
    }

    private void announcePickup(Player player, @NotNull String wool, boolean first){
        String path = first ? "ChatMessages.WoolCaptureFirst" : "ChatMessages.WoolCapture";
        switch (wool){
            case "CYAN":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&3&l" + player.getName())
                        .replace("%Wool%", "&3&lCyan"));
                this.sendSoundToEnemy("blue");

            case "BLUE":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&9&l" + player.getName())
                        .replace("%Wool%", "&9&lAzul"));
                this.sendSoundToEnemy("blue");

            case "RED":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&c&l" + player.getName())
                        .replace("%Wool%", "&c&lRoja"));
                this.sendSoundToEnemy("red");

            case "PINK":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&d&l" + player.getName())
                        .replace("%Wool%", "&d&lRosa"));
                this.sendSoundToEnemy("red");
        }
    }

    private void announceLost(Player player, @NotNull String wool){
        String message = this.ctw.getLanguageHandler().getMessage("ChatMessages.WoolLost");
        switch (wool){
            case "ALL":
                message = this.ctw.getLanguageHandler().getMessage("ChatMessages.AllWoolsLost")
                        .replace("%PlayerName%", this.ctw.getTeamHandler().isBlueTeam(player) ? "&9&l" : "&c&l" + player.getName());
                this.ctw.getMessageUtils().broadcastMessage(message);
                this.sendSoundToEnemy("blue");
            case "CYAN":
                message = message.replace("%PlayerName%", "&3&l" + player.getName()).replace("%Wool%", "&3&lCyan");
                this.ctw.getMessageUtils().broadcastMessage(message);
                this.sendSoundToEnemy("blue");

            case "BLUE":
                message = message.replace("%PlayerName%", "&9&l" + player.getName()).replace("%Wool%", "&9&lAzul");
                this.ctw.getMessageUtils().broadcastMessage(message);
                this.sendSoundToEnemy("blue");

            case "RED":
                message = message.replace("%PlayerName%", "&c&l" + player.getName()).replace("%Wool%", "&c&lRoja");
                this.ctw.getMessageUtils().broadcastMessage(message);
                this.sendSoundToEnemy("red");

            case "PINK":
                message = message.replace("%PlayerName%", "&d&l" + player.getName()).replace("%Wool%", "&d&lRosa");
                this.ctw.getMessageUtils().broadcastMessage(message);
                this.sendSoundToEnemy("red");
        }
    }

    private void sendSoundToEnemy(@NotNull String team) {
        if (team.equalsIgnoreCase("red")) {
            this.ctw.getTeamHandler().blueTeamCopy().forEach(player -> this.ctw.getSoundHandler().sendPickupWoolSound(player));
        } else {
            this.ctw.getTeamHandler().redTeamCopy().forEach(player -> this.ctw.getSoundHandler().sendPickupWoolSound(player));
        }
    }

    private void tagPlayerWithWool(Player player, @NotNull String wool) {
        ItemStack hat = switch (wool) {
            case "CYAN" -> new ItemStack(Material.WOOL, 1, (short) 9);
            case "BLUE" -> new ItemStack(Material.WOOL, 1, (short) 11);
            case "RED" -> new ItemStack(Material.WOOL, 1, (short) 14);
            case "PINK" -> new ItemStack(Material.WOOL, 1, (short) 6);
            default -> new ItemStack(Material.WOOL, 1, (short) 0);
        };

        ItemMeta meta = hat.getItemMeta();
        hat.setItemMeta(meta);
        player.getInventory().setHelmet(hat);


    }
}
