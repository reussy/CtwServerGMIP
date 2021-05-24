package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.map.MapHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Death implements Listener {
    private final CTW ctw;

    public Death(final CTW ctw) {
        this.ctw = ctw;
    }


    @EventHandler
    public void onMoveEvent(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location loc = player.getLocation();
        final int y = loc.getBlockY();
        String map = ctw.map;

        if (map.equals("Puerto")) {

            if (y < -2 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }
        } else if (map.equals("Nether")) {

            if (y < 10 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }

        } else {

            if (y < 0 && player.getGameMode() != GameMode.CREATIVE) {

                if (!player.isDead()) {
                    player.setHealth(0.0);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        event.setDeathMessage("");
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                final Player p = event.getEntity();
                Death.this.ctw.getKillStreakHandler().resetData(p);


                if (p.getLastDamageCause() == null) {

                    EntityDamageEvent event = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.VOID, p.getHealth());
                    p.setLastDamageCause(event);

                }
                if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    final Player killer = p.getKiller();
                    final String rawMsg1 = Death.this.ctw.getLanguageHandler().getMessage("ChatMessages.MeleeDeathBroadcast").replace("%WeaponLogo%", Death.this.getWeaponLogo(killer));
                    final String rawMsg2 = rawMsg1.replace("%KilledPlayer%", Death.this.ctw.getMessageUtils().getTeamColor(p));
                    final String rawMsg3 = rawMsg2.replace("%Killer%", Death.this.ctw.getMessageUtils().getTeamColor(killer));
                    Death.this.sendDeathMessage(rawMsg3.replaceAll("&", "§"));
                    Death.this.ctw.getPlayerScoreHandler().takeScore(p, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                    Death.this.ctw.getMessageUtils().sendScoreMessage(p, "-" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);
                    Death.this.ctw.getPlayerScoreHandler().addScore(killer, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"));
                    Death.this.ctw.getEconomyHandler().addCoins(killer, (double) Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                    Death.this.ctw.getMessageUtils().sendScoreMessage(killer, "+" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"), Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                    Death.this.ctw.getPlayerKillsHandler().addMeleeKill(killer);
                    Death.this.ctw.getMeleeAchievementHandler().checkForAchievements(killer);
                    Death.this.ctw.getOverpoweredAchievementHandler().checkForAchievements(p);
                    Death.this.ctw.getKillStreakHandler().addKill(killer);

                    addRegen(killer);


                } else if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    final Player killer = p.getKiller();
                    final Integer distance = (int) p.getLocation().distance(killer.getLocation());
                    final String rawMsg4 = Death.this.ctw.getLanguageHandler().getMessage("ChatMessages.BowDeathBroadcast").replace("%WeaponLogo%", Death.this.getWeaponLogo(killer));
                    final String rawMsg5 = rawMsg4.replace("%KilledPlayer%", Death.this.ctw.getMessageUtils().getTeamColor(p));
                    final String rawMsg6 = rawMsg5.replace("%Killer%", Death.this.ctw.getMessageUtils().getTeamColor(killer));
                    final String rawMsg7 = rawMsg6.replace("%distance%", new StringBuilder().append(distance).toString());
                    Death.this.sendDeathMessage(rawMsg7.replaceAll("&", "§"));
                    Death.this.ctw.getPlayerScoreHandler().takeScore(p, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                    Death.this.ctw.getMessageUtils().sendScoreMessage(p, "-" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);
                    Death.this.ctw.getPlayerScoreHandler().addScore(killer, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"));
                    Death.this.ctw.getEconomyHandler().addCoins(killer, (double) Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                    Death.this.ctw.getMessageUtils().sendScoreMessage(killer, "+" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"), Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                    Death.this.ctw.getPlayerKillsHandler().addBowKill(killer);
                    Death.this.ctw.getPlayerBowDistanceKillHandler().setDistanceKill(killer, distance);
                    Death.this.ctw.getShooterAchievementHandler().checkForAchievements(killer);
                    Death.this.ctw.getOverpoweredAchievementHandler().checkForAchievements(killer);
                    Death.this.ctw.getDistanceAchievementHandler().checkForAchievements(killer);
                    Death.this.ctw.getKillStreakHandler().addKill(killer);
                } else if (p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID || p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA || p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
                    final Player killer = Death.this.ctw.getLastDamageHandler().getKiller(p);
                    if (killer != null) {
                        final String icon = Death.this.ctw.getLanguageHandler().getMessage("Icons.Other");
                        final String rawMsg4 = Death.this.ctw.getLanguageHandler().getMessage("ChatMessages.VoidDeathBroadcast").replace("%WeaponLogo%", icon);
                        final String rawMsg5 = rawMsg4.replace("%KilledPlayer%", Death.this.ctw.getMessageUtils().getTeamColor(p));
                        final String rawMsg6 = rawMsg5.replace("%Killer%", Death.this.ctw.getMessageUtils().getTeamColor(killer));
                        Death.this.sendDeathMessage(rawMsg6.replaceAll("&", "§"));
                        Death.this.ctw.getPlayerScoreHandler().takeScore(p, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                        Death.this.ctw.getMessageUtils().sendScoreMessage(p, "-" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);
                        Death.this.ctw.getPlayerScoreHandler().addScore(killer, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"));
                        Death.this.ctw.getEconomyHandler().addCoins(killer, (double) Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                        Death.this.ctw.getMessageUtils().sendScoreMessage(killer, "+" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.kill"), Death.this.ctw.getConfigHandler().getInteger("Rewards.Coins.kill"));
                        Death.this.ctw.getKillStreakHandler().addKill(killer);
                        final String weaponType = Death.this.ctw.getLastDamageHandler().getWeaponType(p);
                        if (weaponType.matches("melee")) {
                            Death.this.ctw.getPlayerKillsHandler().addMeleeKill(killer);
                            Death.this.ctw.getMeleeAchievementHandler().checkForAchievements(killer);
                            Death.this.ctw.getOverpoweredAchievementHandler().checkForAchievements(p);

                            addRegen(killer);
                        } else if (weaponType.matches("bow")) {
                            Death.this.ctw.getPlayerKillsHandler().addBowKill(killer);
                            Death.this.ctw.getShooterAchievementHandler().checkForAchievements(killer);
                            Death.this.ctw.getOverpoweredAchievementHandler().checkForAchievements(killer);
                            Death.this.ctw.getDistanceAchievementHandler().checkForAchievements(killer);
                        }
                    } else if (!Death.this.ctw.getTeamHandler().isSpectator(p)) {
                        Death.this.ctw.getPlayerScoreHandler().takeScore(p, Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"));
                        Death.this.ctw.getMessageUtils().sendScoreMessage(p, "-" + Death.this.ctw.getConfigHandler().getInteger("Rewards.Score.death"), null);
                    }
                }


                Death.this.ctw.getTakenWools().checkForLostWool(p, event.getDrops());
            }
        });
    }

    private void addRegen(Player killer) {
        if (killer.getInventory().getHelmet().getType() != Material.IRON_HELMET || killer.getInventory().getBoots().getType() != Material.IRON_BOOTS || killer.getInventory().getLeggings().getType() != Material.IRON_LEGGINGS || killer.getInventory().getChestplate().getType() != Material.IRON_CHESTPLATE ||
                killer.getInventory().getHelmet().getType() != Material.DIAMOND_HELMET || killer.getInventory().getBoots().getType() != Material.DIAMOND_BOOTS || killer.getInventory().getLeggings().getType() != Material.DIAMOND_LEGGINGS || killer.getInventory().getChestplate().getType() != Material.DIAMOND_CHESTPLATE ||
                killer.getInventory().getHelmet().getType() != Material.GOLD_HELMET || killer.getInventory().getBoots().getType() != Material.GOLD_BOOTS || killer.getInventory().getLeggings().getType() != Material.GOLD_LEGGINGS || killer.getInventory().getHelmet().getType() != Material.GOLD_HELMET) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println(11);
                    killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 0));
                }
            }.runTaskLater(ctw, 0);


        }
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
            e.printStackTrace();
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
}
