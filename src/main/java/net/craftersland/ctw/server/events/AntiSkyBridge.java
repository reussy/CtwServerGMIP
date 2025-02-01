package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AntiSkyBridge implements Listener {

    private final CTW ctw;

    public AntiSkyBridge(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {

        Block block = e.getBlock();
        if (block.getLocation().getY() >= this.ctw.getMapConfigHandler().pinkWool.getY() + 25) {

            if (block.getType() == Material.LAVA_BUCKET || block.getType() == Material.WATER_BUCKET) {

                e.setCancelled(true);
                return;
            }

            new BukkitRunnable() {
                public void run() {
                    block.setType(Material.AIR);
                    this.cancel();
                }
            }.runTaskLater(ctw, 80);
        }
    }
}
