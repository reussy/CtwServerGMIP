package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class Connecting implements Listener {
    private final CTW ctw;

    public Connecting(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent event) {
        if (!this.ctw.isEnabled) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.ctw.getLanguageHandler().getMessage("ChatMessages.GameStartingUp"));
        } else if (this.ctw.isDisabling) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.ctw.getLanguageHandler().getMessage("ChatMessages.GameStartingUp"));
        } else if (this.ctw.getGameEngine().gameStage == GameEngine.GameStages.LOADING || !this.ctw.isEnabled) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.ctw.getLanguageHandler().getMessage("ChatMessages.GameStartingUp"));
        }
    }
}
