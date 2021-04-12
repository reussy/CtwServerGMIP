package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class BukkitUsage implements Listener {
    private final CTW ctw;

    public BukkitUsage(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onBukkitEmpty(final PlayerBucketEmptyEvent event) {
        final Location l = event.getBlockClicked().getLocation();
        if (this.ctw.getProtectionHandler().isAreaProtected(l)) {
            event.setCancelled(true);
        }
    }
}
