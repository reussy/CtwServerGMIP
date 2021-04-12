package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class PlayerChat implements Listener {
    private final CTW ctw;

    public PlayerChat(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        if (!event.isAsynchronous()) {
            Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
                @Override
                public void run() {
                    event.setCancelled(true);
                    PlayerChat.this.sendChatMessage(event);
                }
            });
        } else {
            event.setCancelled(true);
            this.sendChatMessage(event);
        }
    }

    private void sendChatMessage(final AsyncPlayerChatEvent event) {
        final Player p = event.getPlayer();
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.SPECTATOR) {
            final List<Player> spec = this.ctw.getTeamHandler().spectatorsCopy();
            for (final Player sp : spec) {
                if (sp.isOnline()) {
                    String msg = this.ctw.getConfigHandler().getStringWithColor("ChatFormat.Spectator").replaceAll("<TeamPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.SpectatorPrefix"));
                    msg = msg.replaceAll("<PlayerName>", p.getName());
                    msg = msg.replaceAll("<Message>", event.getMessage());
                    sp.sendMessage(msg);
                }
            }
        } else if (team == TeamHandler.Teams.BLUE) {
            final List<Player> blue = this.ctw.getTeamHandler().blueTeamCopy();
            for (final Player bp : blue) {
                if (bp.isOnline()) {
                    String msg = this.ctw.getConfigHandler().getStringWithColor("ChatFormat.BlueTeam").replaceAll("<TeamPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.TeamPrefix"));
                    msg = msg.replaceAll("<PlayerName>", p.getName());
                    msg = msg.replaceAll("<Message>", event.getMessage());
                    bp.sendMessage(msg);
                }
            }
        } else if (team == TeamHandler.Teams.RED) {
            final List<Player> red = this.ctw.getTeamHandler().redTeamCopy();
            for (final Player rp : red) {
                if (rp.isOnline()) {
                    String msg = this.ctw.getConfigHandler().getStringWithColor("ChatFormat.RedTeam").replaceAll("<TeamPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.TeamPrefix"));
                    msg = msg.replaceAll("<PlayerName>", p.getName());
                    msg = msg.replaceAll("<Message>", event.getMessage());
                    rp.sendMessage(msg);
                }
            }
        }
    }
}
