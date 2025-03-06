package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jetbrains.annotations.NotNull;

public class NPCInteract implements Listener {

    private final CTW ctw;

    public NPCInteract(CTW ctw) {
        this.ctw = ctw;
    }

    /**
     * Handle the interaction with the NPC
     * that opens the kit menu
     */
    @EventHandler
    public void onNPCInteract(@NotNull PlayerInteractAtEntityEvent event) {

        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (this.ctw.getTeamHandler().isSpectator(player)) return;

        if (event.getRightClicked().getCustomName() != null) {
            if (event.getRightClicked().getCustomName().contains("TIENDA")) {
                this.ctw.getKitHandler().sendKitMenu(player);
            }
        }
    }
}
