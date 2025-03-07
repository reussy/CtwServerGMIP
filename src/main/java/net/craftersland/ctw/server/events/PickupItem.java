package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupItem implements Listener {
    public CTW ctw;

    public PickupItem(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        if (PickupItem.this.ctw.getGameEngine().gameStage == GameEngine.GameStages.COUNTDOWN) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {

            if (event.getItem().getItemStack().getType() == Material.WOOL) {
                final ItemStack item = event.getItem().getItemStack();
                final Player p = event.getPlayer();

                ctw.getTakenWools().woolTakenCheck(p, item.getData().getData());
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItem(InventoryPickupItemEvent e) {
        if (PickupItem.this.ctw.getGameEngine().gameStage == GameEngine.GameStages.COUNTDOWN) {
            e.setCancelled(true);
        }
    }
}
