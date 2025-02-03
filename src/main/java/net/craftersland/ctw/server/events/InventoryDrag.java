package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDrag implements Listener {
    private final CTW ctw;

    public InventoryDrag(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        final Player p = (Player) event.getWhoClicked();
        if (event.getInventory() != null) {
            if (event.getInventory().getTitle().matches(this.ctw.getKitHandler().getMainMenuTitle())) {
                event.setCancelled(true);
            } else if (event.getInventory().equals(this.ctw.getKitHandler().getKitsInventory(p))) {
                event.setCancelled(true);
            } else if (event.getInventory().equals(this.ctw.getKitHandler().getEnchantsInventory(p))) {
                event.setCancelled(true);
            }
        }
        // Cancel move armor
        if (event.getInventorySlots().contains(5) || event.getInventorySlots().contains(6) || event.getInventorySlots().contains(7) || event.getInventorySlots().contains(8)) {
            event.setCancelled(true);
        }
    }
}
