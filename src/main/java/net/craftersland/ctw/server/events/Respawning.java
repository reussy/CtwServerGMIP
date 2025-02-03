package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Respawning implements Listener {
    private final CTW ctw;

    public Respawning(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(final PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            this.loadChunkCheck(this.ctw.getMapConfigHandler().redSpawn);
            event.setRespawnLocation(this.ctw.getMapConfigHandler().redSpawn);
            this.ctw.getPlayerHandler().respawnRedTeam(p);
        } else if (team == TeamHandler.Teams.BLUE) {
            this.loadChunkCheck(this.ctw.getMapConfigHandler().blueSpawn);
            event.setRespawnLocation(this.ctw.getMapConfigHandler().blueSpawn);
            this.ctw.getPlayerHandler().respawnBlueTeam(p);
        } else {
            event.setRespawnLocation(this.ctw.getMapConfigHandler().spectatorSpawn);
            this.ctw.getPlayerHandler().respawnSpectator(p);
        }

        CTW.getPlayersAlreadyEquipped().remove(p.getUniqueId());
    }

    private void loadChunkCheck(final Location l) {
        final World w = l.getWorld();
        if (!w.getChunkAt(l).isLoaded()) {
            w.loadChunk(w.getChunkAt(l));
        }
    }
}
