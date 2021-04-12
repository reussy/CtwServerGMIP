package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteract implements Listener {
    private final CTW ctw;

    public EntityInteract(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            final Location l = event.getRightClicked().getLocation();
            if (this.ctw.getProtectionHandler().isAreaProtected(l)) {
                event.setCancelled(true);
            }
        }
    }
}
