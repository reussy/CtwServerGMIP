package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class InventoryClose implements Listener {
    private final CTW ctw;

    public InventoryClose(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                final Player p = (Player) event.getPlayer();
                if (event.getInventory().equals(InventoryClose.this.ctw.getKitHandler().getKitsInventory(p))) {
                    InventoryClose.this.ctw.getKitHandler().removeInventory(p);
                    InventoryClose.this.ctw.getKitHandler().sendMainMenu(p);
                } else if (event.getInventory().getTitle().matches(InventoryClose.this.ctw.getKitHandler().getTrashMenuTitle())) {
                    InventoryClose.this.ctw.getKitHandler().sendMainMenu(p);
                } else if (event.getInventory().equals(InventoryClose.this.ctw.getKitHandler().getEnchantsInventory(p))) {
                    InventoryClose.this.ctw.getKitHandler().removeInventory(p);
                    InventoryClose.this.ctw.getKitHandler().sendMainMenu(p);
                }
            }
        });
    }
}
