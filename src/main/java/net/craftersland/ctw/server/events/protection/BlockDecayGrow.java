package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class BlockDecayGrow implements Listener {
    private final CTW ctw;

    public BlockDecayGrow(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onBlockDecay(final LeavesDecayEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowDecayGrowth")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(final BlockSpreadEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowDecayGrowth")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(final BlockGrowEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowDecayGrowth")) {
            event.setCancelled(true);
        }
    }
}
