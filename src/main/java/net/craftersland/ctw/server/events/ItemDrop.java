package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class ItemDrop implements Listener {

    private final CTW ctw;

    public ItemDrop(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onDropArmor(@NotNull PlayerDropItemEvent event){
        if (event.getItemDrop().getItemStack().getType().name().contains("HELMET") || event.getItemDrop().getItemStack().getType().name().contains("CHESTPLATE") || event.getItemDrop().getItemStack().getType().name().contains("LEGGINGS") || event.getItemDrop().getItemStack().getType().name().contains("BOOTS")) {
            event.setCancelled(true);
        }
    }
}
