package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import net.craftersland.ctw.server.game.WoolHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
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

    public void woolTakenCheck(Player player, byte wool) {
        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(player);

        switch (team) {
            case RED -> {
                // Sumamos una lana capturaa al equipo rojo
                this.ctw.getTeamWoolsCaptured().addRedWoolsCaptured();
                if (wool == 14) {
                    if (!this.ctw.getWoolHandler().isWoolTaken(WoolHandler.Wool.RED)) {
                        // Iniciamos la tarea de la alerta en la scoreboard
                        this.ctw.getNewScoreboardHandler().startRedWoolTask();
                        // Establecemos la lana roja como tomada
                        this.ctw.getWoolHandler().setWoolTaken(WoolHandler.Wool.RED);
                        //this.ctw.getWoolHandler().setRedTaken(); VIEJO

                        // Lo agregamos a los jugaores que han capturado la lana
                        this.ctw.getWoolHandler().addRedTakenByPlayer(player);

                        // Notificamos sobre la captura de la lana a todos los jugadores
                        this.announcePickup(player, "RED", true);

                        // Modificamos puntaje al jugador
                        this.ctw.getPlayerScoreHandler().addScore(player, 2);
                        this.ctw.getEconomyHandler().addCoins(player, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(player, "+2", 25);
                        break;
                    }

                    if (!this.ctw.getWoolHandler().getRedPlayers().contains(player)) {
                        this.announcePickup(player, "RED", false);
                        this.ctw.getWoolHandler().addRedTakenByPlayer(player);
                    }
                } else if (wool == 6) {
                    if (!this.ctw.getWoolHandler().isWoolTaken(WoolHandler.Wool.PINK)) {
                        this.ctw.getNewScoreboardHandler().startPinkWoolTask();
                        this.ctw.getWoolHandler().setWoolTaken(WoolHandler.Wool.PINK);
                        this.ctw.getWoolHandler().addPinkTakenByPlayer(player);
                        this.announcePickup(player, "PINK", true);
                        this.ctw.getPlayerScoreHandler().addScore(player, 2);
                        this.ctw.getEconomyHandler().addCoins(player, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(player, "+2", 25);
                        break;
                    }

                    if (!this.ctw.getWoolHandler().getPinkPlayers().contains(player)) {
                        this.announcePickup(player, "PINK", false);
                        this.ctw.getWoolHandler().addPinkTakenByPlayer(player);
                    }
                }
            }

            case BLUE -> {
                this.ctw.getTeamWoolsCaptured().addBlueWoolsCapture();
                if (wool == 11) {
                    if (!this.ctw.getWoolHandler().isWoolTaken(WoolHandler.Wool.BLUE)) {
                        this.ctw.getNewScoreboardHandler().startBlueWoolTask();
                        this.ctw.getWoolHandler().setWoolTaken(WoolHandler.Wool.BLUE);
                        this.ctw.getWoolHandler().addBlueTakenByPlayer(player);
                        this.announcePickup(player, "BLUE", true);
                        this.ctw.getPlayerScoreHandler().addScore(player, 2);
                        this.ctw.getEconomyHandler().addCoins(player, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(player, "+2", 25);
                        break;
                    }

                    if (!this.ctw.getWoolHandler().getBluePlayers().contains(player)) {
                        this.announcePickup(player, "BLUE", false);
                        this.ctw.getWoolHandler().addBlueTakenByPlayer(player);
                    }
                } else if (wool == 9){
                    if (!this.ctw.getWoolHandler().isWoolTaken(WoolHandler.Wool.CYAN)) {
                        this.ctw.getNewScoreboardHandler().startCyanWoolTask();
                        this.ctw.getWoolHandler().setWoolTaken(WoolHandler.Wool.CYAN);
                        this.ctw.getWoolHandler().addCyanTakenByPlayer(player);
                        this.announcePickup(player, "CYAN", true);
                       this.ctw.getPlayerScoreHandler().addScore(player, 2);
                        this.ctw.getEconomyHandler().addCoins(player, 25.0);
                        this.ctw.getMessageUtils().sendScoreMessage(player, "+2", 25);
                    }

                    if (!this.ctw.getWoolHandler().getCyanPlayers().contains(player)) {
                        this.announcePickup(player, "CYAN", false);
                        this.ctw.getWoolHandler().addCyanTakenByPlayer(player);
                    }
                }
            }
        }
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
        p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 8));

        CTW.getPlayersAlreadyEquipped().add(p.getUniqueId());
        StartupKit.setUnbreakableArmor(p);
    }

    private void announcePickup(Player player, @NotNull String wool, boolean first){
        String path = first ? "ChatMessages.WoolCaptureFirst" : "ChatMessages.WoolCapture";

        if (first) this.ctw.getSoundHandler().broadcastFireworkLaunchSound();

        this.tagPlayerWithWool(player);
        this.setNewEquipment(player);

        switch (wool){
            case "CYAN":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&9&l" + player.getName())
                        .replace("%Wool%", "&3&lCyan"));
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.title"),
                        this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.subtitle"));
                this.sendSoundToEnemy("blue");
                return;

            case "BLUE":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&9&l" + player.getName())
                        .replace("%Wool%", "&9&lAzul"));
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.title"),
                        this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.subtitle"));
                this.sendSoundToEnemy("blue");
                return;

            case "RED":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&c&l" + player.getName())
                        .replace("%Wool%", "&c&lRoja"));
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.title"),
                        this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.subtitle"));
                this.sendSoundToEnemy("red");
                return;

            case "PINK":
                this.ctw.getMessageUtils().broadcastMessage(this.ctw.getLanguageHandler().getMessage(path)
                        .replace("%PlayerName%", "&c&l" + player.getName())
                        .replace("%Wool%", "&d&lRosa"));
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.title"),
                        this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.subtitle"));
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

    private void tagPlayerWithWool(Player player) {
        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(player);
        ItemStack hat = null;

        switch (team){
            case RED:
                if (ctw.getWoolHandler().hadRedTakenByPlayer(player) && ctw.getWoolHandler().hadPinkTakenByPlayer(player)) {
                    this.changeHatColor(player);
                } else if (ctw.getWoolHandler().hadPinkTakenByPlayer(player)) {
                    hat = new ItemStack(Material.WOOL, 1, (short) 6);
                } else if (ctw.getWoolHandler().hadRedTakenByPlayer(player)) {
                    hat = new ItemStack(Material.WOOL, 1, (short) 14);
                }

                case BLUE:
                if (ctw.getWoolHandler().hadBlueTakenByPlayer(player) && ctw.getWoolHandler().hadCyanTakenByPlayer(player)) {
                    this.changeHatColor(player);
                } else if (ctw.getWoolHandler().hadCyanTakenByPlayer(player)) {
                    hat = new ItemStack(Material.WOOL, 1, (short) 9);
                } else if (ctw.getWoolHandler().hadBlueTakenByPlayer(player)) {
                    hat = new ItemStack(Material.WOOL, 1, (short) 11);
                }
        }

        if (hat != null) {
            player.getInventory().setHelmet(hat);
        }
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
