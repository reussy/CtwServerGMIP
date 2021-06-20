package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupItem implements Listener {
    public CTW ctw;

    public PickupItem(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onItemPickup(final PlayerPickupItemEvent event) {
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
}
