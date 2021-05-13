package net.craftersland.ctw.server.events;

import me.konsolas.aac.api.PlayerViolationEvent;
import net.craftersland.ctw.server.CTW;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// TODO Problema con AAC, realmente se puede acceder a la API?

public class AacIntegration implements Listener {
    private final CTW ctw;

    public AacIntegration(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onViolation(final PlayerViolationEvent event) {
        if (this.ctw.getProtectedMoveHandler().isPlayerOnList(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
