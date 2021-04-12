package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFadeForm implements Listener {
    private final CTW ctw;

    public BlockFadeForm(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onBlockFade(final BlockFadeEvent event) {
        if (event.getBlock().getType() != Material.FIRE && !this.ctw.getConfigHandler().getBoolean("Settings.allowBlockMeltForm")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(final BlockFormEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowBlockMeltForm")) {
            event.setCancelled(true);
        }
    }
}
