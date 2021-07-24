package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    private final CTW ctw;

    public BlockBreak(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (this.ctw.getGameEngine().gameStage != GameEngine.GameStages.RUNNING) {
            event.setCancelled(true);
            return;
        }
        final Location l = event.getBlock().getLocation();
        if (l.equals(this.ctw.getMapConfigHandler().redWool)) {
            if (event.getPlayer() != null) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
            return;
        }
        if (l.equals(this.ctw.getMapConfigHandler().pinkWool)) {
            if (event.getPlayer() != null) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
            return;
        }
        if (l.equals(this.ctw.getMapConfigHandler().blueWool)) {
            if (event.getPlayer() != null) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
            return;
        }
        if (l.equals(this.ctw.getMapConfigHandler().cyanWool)) {
            if (event.getPlayer() != null) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
            return;
        }
        if (this.ctw.getProtectionHandler().isAreaProtected(l)) {
            if (event.getPlayer() != null) {
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    if (event.getBlock().getType() != Material.IRON_BLOCK && event.getBlock().getType() != Material.GOLD_BLOCK) {
                        event.setCancelled(true);
                    }
                }
            } else {
                event.setCancelled(true);
            }
            return;
        }
        this.removeDrops(event);
    }

    private void removeDrops(final BlockBreakEvent event) {
        final Block b = event.getBlock();
        if (b.getType() == Material.WEB) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.REDSTONE_WIRE) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.LEAVES) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.LEAVES_2) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.YELLOW_FLOWER) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.RED_ROSE) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.WOOL) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        } else if (b.getType() == Material.ICE) {
            event.setCancelled(true);
            b.setType(Material.AIR);
        }
    }
}
