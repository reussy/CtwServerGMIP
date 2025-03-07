package net.craftersland.ctw.server.events.protection;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import net.craftersland.ctw.server.game.TeamHandler;
import net.craftersland.ctw.server.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Moving implements Listener {
    private final CTW ctw;
    private final CooldownManager cooldownManager = new CooldownManager();

    public Moving(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        /*
        if (!event.isCancelled() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            final Player p = event.getPlayer();
            final Location l = event.getTo();
            final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
            if (team == TeamHandler.Teams.RED) {
                if (this.ctw.getProtectionHandler().isNoRedAccess(l)) {
                    if (!this.ctw.getProtectedMoveHandler().isPlayerOnList(p)) {
                        this.ctw.getProtectedMoveHandler().addPlayerToList(p);
                        this.removePlayerFromListDelayed(p);
                    }
                    event.setCancelled(true);
                    this.ctw.getProtectedMoveHandler().sendWarningMsg(p);
                    p.teleport(event.getFrom());
                }
            } else if (team == TeamHandler.Teams.BLUE && this.ctw.getProtectionHandler().isNoBlueAccess(l)) {
                if (!this.ctw.getProtectedMoveHandler().isPlayerOnList(p)) {
                    this.ctw.getProtectedMoveHandler().addPlayerToList(p);
                    this.removePlayerFromListDelayed(p);
                }
                event.setCancelled(true);
                this.ctw.getProtectedMoveHandler().sendWarningMsg(p);
                p.teleport(event.getFrom());
            }
        }

         */
    }


    @EventHandler
    public void onPlayerMoveWool(final @NotNull PlayerMoveEvent event) {
        if (!event.isCancelled() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {

            if (ctw.getGameEngine().gameStage == GameEngine.GameStages.RUNNING) {
                final Player p = event.getPlayer();
                final TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(p);

                if (team == TeamHandler.Team.RED) {

                    RegionManager regionManager = WGBukkit.getRegionManager(p.getWorld());
                    ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());

                    if (set.size() == 0) return;

                    for (ProtectedRegion region : set) {
                        if (region.getId().equals("pink")) {
                            giveItems(p, 6, "pink");
                        }

                        if (region.getId().equals("red")) {
                            giveItems(p, 14, "red");
                        }
                    }

                } else if (team == TeamHandler.Team.BLUE) {

                    RegionManager regionManager = WGBukkit.getRegionManager(p.getWorld());
                    ApplicableRegionSet set = regionManager.getApplicableRegions(p.getLocation());

                    if (set.size() == 0) {
                        return;
                    }

                    for (ProtectedRegion region : set) {

                        if (region.getId().equals("cyan")) {
                            giveItems(p, 9, "cyan");
                        }

                        if (region.getId().equals("blue")) {
                            giveItems(p, 11, "blue");
                        }
                    }
                }
            }
        }
    }

    public boolean invFull(Player p) {
        return p.getInventory().firstEmpty() == -1;
    }


    public void giveItems(Player p, int id, String color) {

        int timeLeft = cooldownManager.getCooldown(p.getUniqueId(), color);

        if (timeLeft == 0) {

            ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
            chestplate.addEnchantment(Enchantment.DURABILITY, 2);
            ItemStack boots = new ItemStack(Material.IRON_BOOTS);
            boots.addEnchantment(Enchantment.DURABILITY, 2);


            ItemStack swords1 = new ItemStack(Material.IRON_SWORD);
            swords1.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            ItemStack swords2 = new ItemStack(Material.IRON_SWORD);
            swords2.addEnchantment(Enchantment.DAMAGE_ALL, 2);

            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.DURABILITY, 1);

            p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), new ItemStack(Material.WOOL, 1, (short) id));
            p.getInventory().setBoots(boots);
            p.getInventory().setChestplate(chestplate);

            if (invFull(p)) {
                p.getWorld().dropItemNaturally(p.getLocation().add(0, 1, 0), new ItemStack(Material.GOLDEN_APPLE, 2));
            } else {

                p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));

            }
            if (p.getInventory().contains(new ItemStack(Material.IRON_SWORD)) || p.getInventory().contains(new ItemStack(Material.GOLD_SWORD)) || p.getInventory().contains(new ItemStack(Material.DIAMOND_SWORD))) {
                if (!p.getInventory().contains(swords1) && !p.getInventory().contains(swords2)) {

                    for (int i = 0; i <= p.getInventory().getSize(); i++) {

                        if (p.getInventory().getItem(i) != null) {
                            if (p.getInventory().getItem(i).getType() == Material.GOLD_SWORD || p.getInventory().getItem(i).getType() == Material.IRON_SWORD) {

                                p.getInventory().setItem(i, sword);

                                break;
                            }
                        }
                    }
                }
            } else {
                p.getInventory().setItem(0, sword);
            }

            cooldownManager.setCooldown(p.getUniqueId(), CooldownManager.DEFAULT_COOLDOWN, color);
            new BukkitRunnable() {
                @Override
                public void run() {
                    int timeLeft = cooldownManager.getCooldown(p.getUniqueId(), color);
                    cooldownManager.setCooldown(p.getUniqueId(), --timeLeft, color);
                    if (timeLeft == 0) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(this.ctw, 0, 20);
        }
    }

    private void removePlayerFromListDelayed(final Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                Moving.this.ctw.getProtectedMoveHandler().removePlayerFromList(p);
            }
        }, 5L);
    }
}
