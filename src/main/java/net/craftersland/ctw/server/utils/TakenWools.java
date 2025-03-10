package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TakenWools {
    private final CTW ctw;
    private final Map<Player, BukkitTask> hatColorChangeTask;

    public TakenWools(final CTW ctw) {
        this.ctw = ctw;
        this.hatColorChangeTask = new ConcurrentHashMap<>();
    }

    public void woolTakenCheck(Player p, byte wool) {
        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(p);

        switch (team) {
            case RED -> {
                this.sendSoundToEnemy("blue");
                if (wool == 14) {
                    tagPlayerWithWool(p, "RED");
                    this.ctw.getWoolHandler().addRedTakenByPlayer(p);
                    this.ctw.getTeamWoolsCaptured().addRedCaptured();
                    if (!this.ctw.getWoolHandler().isRedTaken()) {
                        this.ctw.getNewScoreboardHandler().redWoolTaken();
                        this.ctw.getWoolHandler().setRedTaken();
                        this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                        this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.subtitle"));
                        this.announcePickup(p, "RED", true);
                        this.ctw.getPlayerScoreHandler().addScore(p, 2);
                        this.ctw.getEconomyHandler().addCoins(p, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                    } else if (!this.ctw.getWoolHandler().hadRedTakenByPlayer(p)) {
                        this.announcePickup(p, "RED", false);
                    }
                } else if (wool == 6) {
                    tagPlayerWithWool(p, "PINK");
                    this.ctw.getWoolHandler().addPinkTakenByPlayer(p);
                    this.ctw.getTeamWoolsCaptured().addRedCaptured();
                    if (!this.ctw.getWoolHandler().isPinkTaken()) {
                        this.ctw.getNewScoreboardHandler().pinkWoolTaken();
                        this.ctw.getWoolHandler().setPinkTaken();
                        this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                        this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.subtitle"));
                        this.announcePickup(p, "PINK", true);
                        this.ctw.getPlayerScoreHandler().addScore(p, 2);
                        this.ctw.getEconomyHandler().addCoins(p, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                    } else if (!this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                        this.announcePickup(p, "PINK", false);
                    }
                }
            }

            case BLUE -> {
                this.sendSoundToEnemy("red");
                if (wool == 11) {
                    tagPlayerWithWool(p, "BLUE");
                    this.ctw.getWoolHandler().addBlueTakenByPlayer(p);
                    this.ctw.getTeamWoolsCaptured().addBlueCaptured();
                    if (!this.ctw.getWoolHandler().isBlueTaken()) {
                        this.ctw.getNewScoreboardHandler().blueWoolTaken();
                        this.ctw.getWoolHandler().setBlueTaken();
                        this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                        this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.subtitle"));
                        this.announcePickup(p, "BLUE", true);
                        this.ctw.getPlayerScoreHandler().addScore(p, 2);
                        this.ctw.getEconomyHandler().addCoins(p, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                    } else if (!this.ctw.getWoolHandler().hadBlueTakenByPlayer(p)) {
                        this.announcePickup(p, "BLUE", false);
                    }
                } else if (wool == 9){
                    tagPlayerWithWool(p, "CYAN");
                    this.ctw.getWoolHandler().addCyanTakenByPlayer(p);
                    this.ctw.getTeamWoolsCaptured().addBlueCaptured();
                    if (!this.ctw.getWoolHandler().isCyanTaken()) {
                        this.ctw.getNewScoreboardHandler().blueWoolTaken();
                        this.ctw.getWoolHandler().setCyanTaken();
                        this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                        this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.subtitle"));
                        this.announcePickup(p, "CYAN", true);
                        this.ctw.getPlayerScoreHandler().addScore(p, 2);
                        this.ctw.getEconomyHandler().addCoins(p, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                    } else if (!this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                        this.announcePickup(p, "CYAN", false);
                    }
                }
            }
        }

        setNewEquipment(p);
    }

    public void checkForLostWool(Player player, List<ItemStack> items) {
        if (this.hatColorChangeTask.get(player) != null){
            this.hatColorChangeTask.get(player).cancel();
        }

        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(player);
        byte red = 14, pink = 6, blue = 11, cyan = 9;
        boolean hasBothRed = this.ctw.getWoolHandler().hadRedTakenByPlayer(player) && this.ctw.getWoolHandler().hadPinkTakenByPlayer(player);
        boolean hasBothBlue = this.ctw.getWoolHandler().hadBlueTakenByPlayer(player) && this.ctw.getWoolHandler().hadCyanTakenByPlayer(player);

        switch (team) {
            case RED -> {
                if (hasBothRed){
                    if (this.woolLost(items, red) && this.woolLost(items, pink)) {
                        this.ctw.getWoolHandler().removeRedTakenByPlayer(player);
                        this.ctw.getWoolHandler().removePinkTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
                } else if (this.ctw.getWoolHandler().hadRedTakenByPlayer(player)){
                    if (this.woolLost(items, red)) {
                        this.ctw.getWoolHandler().removeRedTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
                } else if (this.ctw.getWoolHandler().hadPinkTakenByPlayer(player)){
                    if (this.woolLost(items, pink)) {
                        this.ctw.getWoolHandler().removePinkTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
                }
            }

            case BLUE -> {
                if (hasBothBlue){
                    if (this.woolLost(items, blue) && this.woolLost(items, cyan)) {
                        this.ctw.getWoolHandler().removeBlueTakenByPlayer(player);
                        this.ctw.getWoolHandler().removeCyanTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
                } else if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(player)){
                    if (this.woolLost(items, blue)) {
                        this.ctw.getWoolHandler().removeBlueTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
                } else if (this.ctw.getWoolHandler().hadCyanTakenByPlayer(player)){
                    if (this.woolLost(items, cyan)) {
                        this.ctw.getWoolHandler().removeCyanTakenByPlayer(player);
                        this.ctw.getEffectUtils().sendWoolLostEffect(player);
                    }
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
                        .replace("%PlayerName%", "&9&l" + player.getName())
                        .replace("%Wool%", "&3&lCyan"));
                this.sendSoundToEnemy("blue");
                return;

            case "BLUE":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&9&l" + player.getName())
                        .replace("%Wool%", "&9&lAzul"));
                this.sendSoundToEnemy("blue");
                return;

            case "RED":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&c&l" + player.getName())
                        .replace("%Wool%", "&c&lRoja"));
                this.sendSoundToEnemy("red");
                return;

            case "PINK":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&c&l" + player.getName())
                        .replace("%Wool%", "&d&lRosa"));
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
        ItemStack hat;
        switch (wool) {
            case "CYAN":
                hat = new ItemStack(Material.WOOL, 1, (short) 9);
                if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(player)) {
                    this.changeHatColor(player);
                }
                break;

            case "BLUE":
                hat = new ItemStack(Material.WOOL, 1, (short) 11);
                if (this.ctw.getWoolHandler().hadCyanTakenByPlayer(player)) {
                    this.changeHatColor(player);
                }
                break;

            case "RED":
                hat = new ItemStack(Material.WOOL, 1, (short) 14);
                if (this.ctw.getWoolHandler().hadPinkTakenByPlayer(player)) {
                    this.changeHatColor(player);
                }
                break;

            case "PINK":
                hat = new ItemStack(Material.WOOL, 1, (short) 6);
                if (this.ctw.getWoolHandler().hadRedTakenByPlayer(player)) {
                    this.changeHatColor(player);
                }
                break;

            default:
                return;
        }

        ItemMeta meta = hat.getItemMeta();
        hat.setItemMeta(meta);
        player.getInventory().setHelmet(hat);
    }

    private void changeHatColor(Player player){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(this.ctw, () -> {
            ItemStack hat = player.getInventory().getHelmet();
            if (hat != null && hat.getType() == Material.WOOL) {
                if (hat.getData().getData() == 14) {
                    hat.setDurability((short) 6);
                } else if (hat.getData().getData() == 6) {
                    hat.setDurability((short) 14);
                } else if (hat.getData().getData() == 11) {
                    hat.setDurability((short) 9);
                } else if (hat.getData().getData() == 9) {
                    hat.setDurability((short) 11);
                }
                player.getInventory().setHelmet(hat);
            }
        }, 0L, 20L);
        this.hatColorChangeTask.put(player, task);
    }
}
