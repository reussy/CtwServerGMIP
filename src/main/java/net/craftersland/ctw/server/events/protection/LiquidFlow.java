package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class LiquidFlow implements Listener {
    private final CTW ctw;

    public LiquidFlow(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onLiquidFlow(final BlockFromToEvent event) {
        if (this.ctw.getGameEngine().gameStage == GameEngine.GameStages.RUNNING) {
            final Location l = event.getBlock().getLocation();
            if (this.ctw.getProtectionHandler().isAreaProtected(l)) {
                event.setCancelled(true);
            }
        }
    }
}
