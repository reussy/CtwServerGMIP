package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import net.craftersland.ctw.server.game.PlayerProjectile;
import net.craftersland.ctw.server.game.TeamHandler;
import net.craftersland.ctw.server.utils.PluginUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Death implements Listener {
    private final CTW ctw;

    public Death(final CTW ctw) {
        this.ctw = ctw;
    }


    @EventHandler
    public void onSplash(@NotNull PotionSplashEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player player = ((Player) e.getEntity().getShooter()).getPlayer();

            e.getAffectedEntities().forEach(livingEntity -> {

                if (livingEntity instanceof Player) {
                    if (this.ctw.getTeamHandler().getTeam(player) == this.ctw.getTeamHandler().getTeam(((Player) livingEntity).getPlayer())) {

                        e.getPotion().getEffects().forEach(potionEffect -> {

                            if (potionEffect.getType().equals(PotionEffectType.POISON) || potionEffect.getType().equals(PotionEffectType.BLINDNESS) || potionEffect.getType().equals(PotionEffectType.SLOW) ||
                                    potionEffect.getType().equals(PotionEffectType.WEAKNESS) || potionEffect.getType().equals(PotionEffectType.CONFUSION) || potionEffect.getType().equals(PotionEffectType.WITHER) ||
                                    potionEffect.getType().equals(PotionEffectType.HARM)) {
                                e.setIntensity(livingEntity, 0);
                            }
                        });
                    }
                }
            });
        }
    }

    @EventHandler
    public void onMoveEvent(final @NotNull PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location loc = player.getLocation();
        final int y = (int) loc.getY();

        if (y < this.ctw.getMapConfigHandler().pinkWool.getY() - 40 && player.getGameMode() != GameMode.CREATIVE) {
                if (!player.isDead()) {
                    player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, player.getHealth()));
                    player.damage(player.getHealth(), null);
                    Bukkit.getScheduler().runTask(this.ctw, () -> player.spigot().respawn());
            }
            }
    }

    @EventHandler
    public void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        event.setDeathMessage("");

        if (this.ctw.getGameEngine().gameStage == GameEngine.GameStages.COUNTDOWN) return;

        Player victim = event.getEntity();

        if (this.ctw.getWoolHandler().getRedPlayers().contains(victim) || this.ctw.getWoolHandler().getPinkPlayers().contains(victim) || this.ctw.getWoolHandler().getBluePlayers().contains(victim) || this.ctw.getWoolHandler().getPinkPlayer().contains(victim)) {
            Bukkit.getOnlinePlayers().forEach(player1 -> this.ctw.getSoundHandler().sendPlayerWoolDeathSound(player1));
        }

        this.ctw.getTakenWools().checkForLostWool(event.getEntity(), event.getDrops());
        event.getDrops().clear();

        // Resetear los datos del jugador
        this.ctw.getKillStreakHandler().resetData(victim);

        // Si el asesino es null cambiamos la causa de la muerte a void
        if (victim.getLastDamageCause() == null) {
            EntityDamageEvent event1 = new EntityDamageEvent(victim, EntityDamageEvent.DamageCause.VOID, victim.getHealth());
            victim.setLastDamageCause(event1);
        }

        // Verificamos que el jugador haya sido asesinado por otro jugador
        if (!(victim.getLastDamageCause().getEntity() instanceof Player)){
            return;
        }

        // Empezamos a verificar las causas de la muerte de la victima
        // Si la causa de la muerte es un ataque de un jugador:
        if (victim.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            // Verificamos que el asesino no sea null
            if (victim.getKiller() == null){
                return;
            }

            Player killer = victim.getKiller();

            // Mandamos sonido de kill al asesino
            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 3.0f, 3.0f);

            String meleeDeathBroadcast = this.ctw.getLanguageHandler().getMessage("ChatMessages.MeleeDeathBroadcast")
                    .replace("%WeaponLogo%", this.getWeaponLogo(killer));

            this.ctw.getPlayerKillsHandler().addMeleeKill(killer);
            this.ctw.getMeleeAchievementHandler().checkForAchievements(killer);
            this.ctw.getOverpoweredAchievementHandler().checkForAchievements(victim);
            this.ctw.getKillStreakHandler().addStreakKill(killer);
            this.ctw.getPlayerKillsHandler().addMeleeKillMatch(killer);

            sendDeathMessage(victim, killer, meleeDeathBroadcast);
            modifyScores(victim, killer);
            buffKiller(killer);

            // Si la causa de la muerte es un proyectil:
        } else if (victim.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            // Verificamos que el asesino no sea null
            if (victim.getKiller() == null){
                return;
            }

            Player killer = victim.getKiller();

            // Mandamos sonido de kill al asesino
            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 3.0f, 3.0f);

            int distance;

            try {
                PlayerProjectile pj = ctw.playerProjectile.get(killer.getUniqueId());
                distance = pj.getProjectile(victim.getUniqueId());
            } catch (NullPointerException e) {
                distance = (int) victim.getLocation().distance(killer.getLocation());
                e.printStackTrace();
            }

            final String bowDeathBroadcast = this.ctw.getLanguageHandler().getMessage("ChatMessages.BowDeathBroadcast")
                    .replace("%WeaponLogo%", this.getWeaponLogo(killer))
                    .replace("%distance%", String.valueOf(distance));

            this.ctw.getPlayerKillsHandler().addBowKill(killer);
            this.ctw.getPlayerBowDistanceKillHandler().setDistanceKill(killer, distance);
            this.ctw.getShooterAchievementHandler().checkForAchievements(killer);
            this.ctw.getOverpoweredAchievementHandler().checkForAchievements(killer);
            this.ctw.getDistanceAchievementHandler().checkForAchievements(killer);
            this.ctw.getKillStreakHandler().addStreakKill(killer);
            this.ctw.getPlayerKillsHandler().addBowKillMatch(killer);

            sendDeathMessage(victim, killer, bowDeathBroadcast);
            modifyScores(victim, killer);
            buffKiller(killer);

            // Si la causa de la muerte es tirado al vacio, lava o caida (altura):
        } else if (victim.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID
                || victim.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA
                || victim.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {

            // Obtenemos el ultimo jugador que daño al jugador que murio
            Player lastDamagerKiller = this.ctw.getLastDamageHandler().getKiller(victim);

            // Verificamos que el ultimo jugador que daño al jugador que murio no sea null
            if (lastDamagerKiller != null) {
                String voidDeathMessage = this.ctw.getLanguageHandler().getMessage("ChatMessages.VoidDeathBroadcast")
                        .replace("%WeaponLogo%", this.ctw.getLanguageHandler().getMessage("Icons.Other"))
                        .replace("%KilledPlayer%", this.ctw.getMessageUtils().getPlayerWithTeam(victim))
                        .replace("%Killer%", this.ctw.getMessageUtils().getPlayerWithTeam(lastDamagerKiller));

                this.ctw.getKillStreakHandler().addStreakKill(lastDamagerKiller);
                String weaponType = this.ctw.getLastDamageHandler().getWeaponType(lastDamagerKiller);

                if (weaponType == null) {
                    this.ctw.getLastDamageHandler().setData(victim, lastDamagerKiller, "melee");
                    weaponType = "melee";
                }

                if (weaponType.matches("melee")) {
                    this.ctw.getPlayerKillsHandler().addMeleeKill(lastDamagerKiller);
                    this.ctw.getMeleeAchievementHandler().checkForAchievements(lastDamagerKiller);
                    this.ctw.getOverpoweredAchievementHandler().checkForAchievements(lastDamagerKiller);

                } else if (weaponType.matches("bow")) {
                    this.ctw.getPlayerKillsHandler().addBowKill(lastDamagerKiller);
                    this.ctw.getShooterAchievementHandler().checkForAchievements(lastDamagerKiller);
                    this.ctw.getOverpoweredAchievementHandler().checkForAchievements(lastDamagerKiller);
                    this.ctw.getDistanceAchievementHandler().checkForAchievements(lastDamagerKiller);
                }

                buffKiller(lastDamagerKiller);
                modifyScores(victim, lastDamagerKiller);
            } else {
                String voidSelfDeathBroadcast = this.ctw.getLanguageHandler().getMessage("ChatMessages.VoidSelfDeathBroadcast")
                        .replace("%WeaponLogo%", this.ctw.getLanguageHandler().getMessage("Icons.Other"))
                        .replace("%KilledPlayer%", this.ctw.getMessageUtils().getPlayerWithTeam(victim));

                sendDeathMessage(victim, null, voidSelfDeathBroadcast);
                modifyScores(victim, null);
            }
        }

        Bukkit.getScheduler().runTask(this.ctw, () -> victim.spigot().respawn());
    }

    private void modifyScores(Player victim, Player killer) {

        //this.sendDeathMessage(ChatColor.translateAlternateColorCodes('&', rawMsg6));

        // Quitar puntos al jugador que muere
        this.ctw.getPlayerScoreHandler().takeScore(victim, this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
        this.ctw.getMessageUtils().sendScoreMessage(victim, "-" + this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);

        // Añadir puntos al jugador que mata
        if (killer != null) {
            this.ctw.getPlayerScoreHandler().addScore(killer, this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"));
            this.ctw.getEconomyHandler().addCoins(killer, Double.valueOf(this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill")));
            this.ctw.getMessageUtils().sendScoreMessage(killer, "+" + this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"), this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
        }
    }

    private void buffKiller(@NotNull Player killer) {

        killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
        killer.getInventory().addItem(new ItemStack(Material.ARROW, 2));

        if (!checkArmor(killer)) return;

        Bukkit.getScheduler().runTaskLater(this.ctw, () -> {
            if (this.ctw.getKillStreakHandler().getStreak(killer) <= 3) {
                killer.removePotionEffect(PotionEffectType.REGENERATION);
                killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 1));
            } else {
                killer.removePotionEffect(PotionEffectType.ABSORPTION);
                killer.removePotionEffect(PotionEffectType.REGENERATION);

                killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 1));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 0));
            }
        }, 3L);
    }

    private String getWeaponLogo(final Player p) {
        String icon = "";
        try {
            if (p.getItemInHand().getType() == Material.IRON_SWORD) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.IronSword");
            } else if (p.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.DiamondSword");
            } else if (p.getItemInHand().getType() == Material.BOW) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.Bow");
            } else if (p.getItemInHand().getType() == Material.GOLD_SWORD) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.GoldSword");
            } else if (p.getItemInHand().getType() == Material.STONE_SWORD) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.StoneSword");
            } else if (p.getItemInHand().getType() == Material.WOOD_SWORD) {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.IronSword");
            } else {
                icon = this.ctw.getLanguageHandler().getMessage("Icons.Other");
            }
        } catch (Exception e) {
            CTW.log.warning("Error getting weapon logo for the weapon used! Error: " + e.getMessage());
        }
        return icon;
    }

    private void sendDeathMessage(@NotNull Player victim, @Nullable Player killer, String msg) {
        if (killer != null){
            Bukkit.getOnlinePlayers().forEach(player -> PluginUtil.send(player, msg
                    .replace("%KilledPlayer%", this.ctw.getMessageUtils().getPlayerWithTeam(victim))
                    .replace("%Killer%", this.ctw.getMessageUtils().getPlayerWithTeam(killer)).replace("%WoolPlaceholder%", getWoolLostPlaceholder(victim, getWoolLost(victim)))));
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> PluginUtil.send(player, msg
                    .replace("%KilledPlayer%", this.ctw.getMessageUtils().getPlayerWithTeam(victim))
                    .replace("%WoolPlaceholder%", getWoolLostPlaceholder(victim, getWoolLost(victim)))));
        }
    }

    private @NotNull String getWoolLost(Player victim) {
        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(victim);

        switch (team){
            case RED:
                if (this.ctw.getWoolHandler().hadRedTakenByPlayer(victim) && this.ctw.getWoolHandler().hadPinkTakenByPlayer(victim)){
                    return "ALL";
                } else if (this.ctw.getWoolHandler().hadRedTakenByPlayer(victim)){
                    return "RED";
                } else if (this.ctw.getWoolHandler().hadPinkTakenByPlayer(victim)){
                    return "PINK";
                }

            case BLUE:
                if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(victim) && this.ctw.getWoolHandler().hadCyanTakenByPlayer(victim)){
                    return "ALL";
                } else if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(victim)){
                    return "BLUE";
                } else if (this.ctw.getWoolHandler().hadCyanTakenByPlayer(victim)){
                    return "CYAN";
                }
            }
        return "";
        }

    private String getWoolLostPlaceholder(@NotNull Player player, @NotNull String wool){
        String placeholder = this.ctw.getLanguageHandler().getMessage("ChatMessages.WoolLost").replace("%PlayerName%", player.getName());
        placeholder = switch (wool) {
            case "ALL" -> this.ctw.getLanguageHandler().getMessage("ChatMessages.AllWoolsLost")
                    .replace("%PlayerName%", this.ctw.getTeamHandler().isBlueTeam(player) ? "&9&l" : "&c&l" + player.getName());
            case "CYAN" ->
                    placeholder.replace("%Wool%", "&3&l" + this.ctw.getLanguageHandler().getMessage("Words.Cyan"));
            case "BLUE" ->
                    placeholder.replace("%Wool%", "&9&l" + this.ctw.getLanguageHandler().getMessage("Words.Blue"));
            case "RED" -> placeholder.replace("%Wool%", "&c&l" + this.ctw.getLanguageHandler().getMessage("Words.Red"));
            case "PINK" ->
                    placeholder.replace("%Wool%", "&d&l" + this.ctw.getLanguageHandler().getMessage("Words.Pink"));
            default -> placeholder;
        };
        return placeholder;
    }

    private boolean checkArmor(@NotNull Player player) {

        if (player.getInventory().getHelmet() == null
                || player.getInventory().getChestplate() == null
                || player.getInventory().getLeggings() == null
                || player.getInventory().getBoots() == null) {
            return false;
        }

        if (player.getInventory().getHelmet().getType() == Material.IRON_HELMET) {
            return false;
        } else if (player.getInventory().getBoots().getType() == Material.IRON_BOOTS) {
            return false;
        } else if (player.getInventory().getLeggings().getType() == Material.IRON_LEGGINGS) {
            return false;
        } else if (player.getInventory().getChestplate().getType() == Material.IRON_CHESTPLATE) {
            return false;
        } else if (player.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) {
            return false;
        } else if (player.getInventory().getBoots().getType() == Material.DIAMOND_BOOTS) {
            return false;
        } else if (player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
            return false;
        } else if (player.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE) {
            return false;
        } else if (player.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
            return false;
        } else if (player.getInventory().getBoots().getType() == Material.GOLD_BOOTS) {
            return false;
        } else if (player.getInventory().getLeggings().getType() == Material.GOLD_LEGGINGS) {
            return false;
        } else return player.getInventory().getHelmet().getType() != Material.GOLD_HELMET;
    }
}
