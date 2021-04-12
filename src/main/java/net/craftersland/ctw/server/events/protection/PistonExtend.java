package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonExtend implements Listener {
    private final CTW ctw;

    public PistonExtend(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPistonExtend(final BlockPistonExtendEvent event) {
        for (final Block b : event.getBlocks()) {
            if (this.ctw.getProtectionHandler().isAreaProtected(b.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
}
