package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import net.craftersland.ctw.server.game.PlayerProjectile;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        String map = ctw.map;

        if (map.equals("Puerto")) {

            if (y < -2 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }
        } else if (map.equals("Nether")) {

            if (y <= 30 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }

        } else {

            if (y < this.ctw.getMapConfigHandler().pinkWool.getY() - 40 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(final @NotNull PlayerDeathEvent event) {
        event.setDeathMessage("");

        Player player = event.getEntity();

        if (this.ctw.getWoolHandler().listPlayersred().contains(player) || this.ctw.getWoolHandler().listPlayerspink().contains(player) || this.ctw.getWoolHandler().listPlayersblue().contains(player) || this.ctw.getWoolHandler().listPlayerscyan().contains(player)) {
            Bukkit.getOnlinePlayers().forEach(player1 -> this.ctw.getSoundHandler().sendPlayerWoolDeathSound(player1));
        }

        // Eliminar los jugadores de la lista de wools
        this.ctw.getWoolHandler().listPlayersred().remove(player);
        this.ctw.getWoolHandler().listPlayerspink().remove(player);
        this.ctw.getWoolHandler().listPlayersblue().remove(player);
        this.ctw.getWoolHandler().listPlayerscyan().remove(player);

        this.ctw.getTakenWools().checkForLostWool(event.getEntity(), event.getDrops());
        event.getDrops().clear();

        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {

            Player p = event.getEntity();
            this.ctw.getKillStreakHandler().resetData(p);

            if (p.getLastDamageCause() == null) {

                EntityDamageEvent event1 = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.VOID, p.getHealth());
                p.setLastDamageCause(event1);
            }

            if (!(p.getLastDamageCause().getEntity() instanceof Player)) return;
            if (p.getKiller() == null) return;

            Player killer = p.getKiller();

            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 3.0f, 3.0f);

            if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {

                String rawMsg1 = this.ctw.getLanguageHandler().getMessage("ChatMessages.MeleeDeathBroadcast").replace("%WeaponLogo%", this.getWeaponLogo(killer));

                this.ctw.getPlayerKillsHandler().addMeleeKill(killer);
                this.ctw.getMeleeAchievementHandler().checkForAchievements(killer);
                this.ctw.getOverpoweredAchievementHandler().checkForAchievements(p);
                this.ctw.getKillStreakHandler().addStreakKill(killer);
                this.ctw.getPlayerKillsHandler().addMeleeKillMatch(killer);

                addPoints(p, killer, rawMsg1);
                addRegen(killer);

            } else if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                int distance;

                try {
                    PlayerProjectile pj = ctw.playerProjectile.get(killer.getUniqueId());
                    distance = pj.getProjectile(p.getUniqueId());
                } catch (NullPointerException e) {
                    distance = (int) p.getLocation().distance(killer.getLocation());
                    e.printStackTrace();
                }

                final String bowDeathBroadcast = this.ctw.getLanguageHandler().getMessage("ChatMessages.BowDeathBroadcast")
                        .replace("%WeaponLogo%", this.getWeaponLogo(killer))
                        .replace("%KilledPlayer%", this.ctw.getMessageUtils().getTeamColor(p))
                        .replace("%Killer%", this.ctw.getMessageUtils().getTeamColor(killer))
                        .replace("%distance%", String.valueOf(distance));
                
                this.ctw.getPlayerKillsHandler().addBowKill(killer);
                this.ctw.getPlayerBowDistanceKillHandler().setDistanceKill(killer, Integer.valueOf(distance));
                this.ctw.getShooterAchievementHandler().checkForAchievements(killer);
                this.ctw.getOverpoweredAchievementHandler().checkForAchievements(killer);
                this.ctw.getDistanceAchievementHandler().checkForAchievements(killer);
                this.ctw.getKillStreakHandler().addStreakKill(killer);
                this.ctw.getPlayerKillsHandler().addBowKillMatch(killer);
                
                addPoints2(p, killer, bowDeathBroadcast);
                addRegen(killer);
            } else if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID || p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA || p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player lastDamagerKiller = this.ctw.getLastDamageHandler().getKiller(p);
                if (lastDamagerKiller != null) {
                    addRegen(lastDamagerKiller);
                    String icon = this.ctw.getLanguageHandler().getMessage("Icons.Other");
                    String rawMsg4 = this.ctw.getLanguageHandler().getMessage("ChatMessages.VoidDeathBroadcast").replace("%WeaponLogo%", icon);
                    addPoints(p, lastDamagerKiller, rawMsg4);
                    this.ctw.getKillStreakHandler().addStreakKill(lastDamagerKiller);
                    final String weaponType = this.ctw.getLastDamageHandler().getWeaponType(p);
                    if (weaponType.matches("melee")) {
                        this.ctw.getPlayerKillsHandler().addMeleeKill(lastDamagerKiller);
                        this.ctw.getMeleeAchievementHandler().checkForAchievements(lastDamagerKiller);
                        this.ctw.getOverpoweredAchievementHandler().checkForAchievements(p);

                    } else if (weaponType.matches("bow")) {
                        this.ctw.getPlayerKillsHandler().addBowKill(lastDamagerKiller);
                        this.ctw.getShooterAchievementHandler().checkForAchievements(lastDamagerKiller);
                        this.ctw.getOverpoweredAchievementHandler().checkForAchievements(lastDamagerKiller);
                        this.ctw.getDistanceAchievementHandler().checkForAchievements(lastDamagerKiller);
                    }
                } else if (!this.ctw.getTeamHandler().isSpectator(p)) {

                    if (this.ctw.getGameEngine().gameStage != GameEngine.GameStages.COUNTDOWN) {
                        if ((ctw.getTeamHandler().countBlueTeam() + ctw.getTeamHandler().countRedTeam()) > 3) {

                            this.ctw.getPlayerScoreHandler().takeScore(p, this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                            this.ctw.getMessageUtils().sendScoreMessage(p, "-" + this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);
                        }
                    }
                }
            }
        });

        Bukkit.getScheduler().runTask(this.ctw, () -> player.spigot().respawn());
    }

    private void addPoints(Player p, Player killer, @NotNull String rawMsg4) {
        final String rawMsg5 = rawMsg4.replace("%KilledPlayer%", this.ctw.getMessageUtils().getTeamColor(p));
        final String rawMsg6 = rawMsg5.replace("%Killer%", this.ctw.getMessageUtils().getTeamColor(killer));
        addPoints2(p, killer, rawMsg6);
    }

    private void addPoints2(Player p, Player killer, @NotNull String rawMsg6) {

        this.sendDeathMessage(rawMsg6.replaceAll("&", "§"));
        if (this.ctw.getGameEngine().gameStage == GameEngine.GameStages.RUNNING) {

            if ((ctw.getTeamHandler().countBlueTeam() + ctw.getTeamHandler().countRedTeam()) > 3 || killer.isOp()) {

                // Quitar puntos al jugador que muere
                this.ctw.getPlayerScoreHandler().takeScore(p, this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                this.ctw.getMessageUtils().sendScoreMessage(p, "-" + this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);

                // Añadir puntos al jugador que mata
                this.ctw.getPlayerScoreHandler().addScore(killer, this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"));
                this.ctw.getEconomyHandler().addCoins(killer, Double.valueOf(this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill")));
                this.ctw.getMessageUtils().sendScoreMessage(killer, "+" + this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"), this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
            }
        }
    }

    private void addRegen(Player killer) {

        if (killer == null) {
            return;
        }

        if (this.ctw.getGameEngine().gameStage != GameEngine.GameStages.COUNTDOWN) {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            killer.getInventory().addItem(new ItemStack(Material.ARROW, 2));
        }

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

    private void sendDeathMessage(final String msg) {
        final List<Player> redTeam = this.ctw.getTeamHandler().redTeamCopy();
        final List<Player> blueTeam = this.ctw.getTeamHandler().blueTeamCopy();
        if (!redTeam.isEmpty()) {
            for (final Player p : redTeam) {
                if (p != null && p.isOnline()) {
                    p.sendMessage(msg);
                }
            }
        }
        if (!blueTeam.isEmpty()) {
            for (final Player p : blueTeam) {
                if (p != null && p.isOnline()) {
                    p.sendMessage(msg);
                }
            }
        }
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
        } else if (player.getInventory().getHelmet().getType() == Material.GOLD_HELMET) {
            return false;
        }
        return true;
    }
}
