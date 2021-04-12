package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

public class FireSpread implements Listener {
    private final CTW ctw;

    public FireSpread(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onFireSpread(final BlockIgniteEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowFireSpread") && event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowFireSpread")) {
            event.setCancelled(true);
        }
    }
}
