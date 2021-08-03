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

                    if (item.getData().getData() == 14) {
                        PickupItem.this.ctw.getTakenWools().redWoolTakenCheck(p);
                    } else if (item.getData().getData() == 6) {
                        PickupItem.this.ctw.getTakenWools().pinkWoolTakenCheck(p);
                    } else if (item.getData().getData() == 11) {
                        PickupItem.this.ctw.getTakenWools().blueWoolTakenCheck(p);
                    } else if (item.getData().getData() == 9) {
                        PickupItem.this.ctw.getTakenWools().cyanWoolTakenCheck(p);
                    }
                }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItem(InventoryPickupItemEvent e){

        if (PickupItem.this.ctw.getGameEngine().gameStage == GameEngine.GameStages.COUNTDOWN) {
            e.setCancelled(true);
        }


    }
}
