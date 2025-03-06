package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockBreak implements Listener {
    private final CTW ctw;
    private final List<Location> woolLocations;

    public BlockBreak(final CTW ctw) {
        this.ctw = ctw;
        this.woolLocations = new ArrayList<>();
        addWoolLocation();
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (this.ctw.getGameEngine().gameStage != GameEngine.GameStages.RUNNING) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();

        if (player == null) return;

        if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
            return;
        }

        final Location location = event.getBlock().getLocation();

        // Chequeamos si la ubicación del bloque roto es una lana
        woolLocations.forEach(woolLocation -> {
            if (location.equals(woolLocation)) {
                event.setCancelled(true);
            }
        });

       // Protegemos las áreas si están protegidas
        if (this.ctw.getProtectionHandler().isAreaProtected(location)) {
            if (event.getBlock().getType() != Material.IRON_BLOCK
                    || event.getBlock().getType() != Material.GOLD_BLOCK
                    || event.getBlock().getType() != Material.WEB) {
                event.setCancelled(true);
            }
            return;
        }

        this.removeDrops(event);
    }

    private void removeDrops(final @NotNull BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (block.getType() == Material.WEB ||
                block.getType() == Material.REDSTONE_WIRE ||
                block.getType() == Material.LEAVES ||
                block.getType() == Material.LEAVES_2 ||
                block.getType() == Material.YELLOW_FLOWER ||
                block.getType() == Material.RED_ROSE ||
                block.getType() == Material.WOOL ||
                block.getType() == Material.ICE) {
            event.setCancelled(true);
            block.setType(Material.AIR);
        }
    }

    private void addWoolLocation() {
        this.woolLocations.add(this.ctw.getMapConfigHandler().blueWool);
        this.woolLocations.add(this.ctw.getMapConfigHandler().cyanWool);
        this.woolLocations.add(this.ctw.getMapConfigHandler().pinkWool);
        this.woolLocations.add(this.ctw.getMapConfigHandler().redWool);
    }
}
