package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.util.List;

public class SpectatorTask {
    private final CTW ctw;

    public SpectatorTask(final CTW ctw) {
        this.ctw = ctw;
    }

    public void sendSpectatorMessage() {
        final List<Player> spectators = this.ctw.getTeamHandler().spectatorsCopy();
        if (!spectators.isEmpty()) {
            for (final Player p : spectators) {
                if (p != null && p.isOnline()) {
                    this.ctw.getSoundHandler().sendLavaPopSound(p.getLocation(), p);
                    this.ctw.getMessageUtils().sendActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.SpectatorJoinMessage"), p);
                }
            }
        }
    }
}
