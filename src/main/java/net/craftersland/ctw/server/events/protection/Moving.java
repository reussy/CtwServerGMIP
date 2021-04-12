package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class Moving implements Listener {
    private final CTW ctw;

    public Moving(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (!event.isCancelled() && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            final Player p = event.getPlayer();
            final Location l = event.getTo();
            final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
            if (team == TeamHandler.Teams.RED) {
                if (this.ctw.getProtectionHandler().isNoRedAccess(l)) {
                    if (!this.ctw.getProtectedMoveHandler().isPlayerOnList(p)) {
                        this.ctw.getProtectedMoveHandler().addPlayerToList(p);
                        this.removePlayerFromListDelayed(p);
                    }
                    event.setCancelled(true);
                    this.ctw.getProtectedMoveHandler().sendWarningMsg(p);
                    p.teleport(event.getFrom());
                }
            } else if (team == TeamHandler.Teams.BLUE && this.ctw.getProtectionHandler().isNoBlueAccess(l)) {
                if (!this.ctw.getProtectedMoveHandler().isPlayerOnList(p)) {
                    this.ctw.getProtectedMoveHandler().addPlayerToList(p);
                    this.removePlayerFromListDelayed(p);
                }
                event.setCancelled(true);
                this.ctw.getProtectedMoveHandler().sendWarningMsg(p);
                p.teleport(event.getFrom());
            }
        }
    }

    private void removePlayerFromListDelayed(final Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                Moving.this.ctw.getProtectedMoveHandler().removePlayerFromList(p);
            }
        }, 5L);
    }
}
