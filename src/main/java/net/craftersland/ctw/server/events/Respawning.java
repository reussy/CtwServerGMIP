package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class Respawning implements Listener {
    private final CTW ctw;

    public Respawning(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onSpawn(@NotNull CreatureSpawnEvent event){
        if (event.getEntity() instanceof Player) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(final @NotNull PlayerRespawnEvent event) {
        final Player p = event.getPlayer();
        final TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Team.RED) {
            this.loadChunkCheck(this.ctw.getMapConfigHandler().redSpawn);
            event.setRespawnLocation(this.ctw.getMapConfigHandler().redSpawn);
            this.ctw.getPlayerHandler().respawnRedTeam(p);
        } else if (team == TeamHandler.Team.BLUE) {
            this.loadChunkCheck(this.ctw.getMapConfigHandler().blueSpawn);
            event.setRespawnLocation(this.ctw.getMapConfigHandler().blueSpawn);
            this.ctw.getPlayerHandler().respawnBlueTeam(p);
        } else {
            event.setRespawnLocation(this.ctw.getMapConfigHandler().spectatorSpawn);
            this.ctw.getPlayerHandler().respawnSpectator(p);
        }

        CTW.getPlayersAlreadyEquipped().remove(p.getUniqueId());
    }

    private void loadChunkCheck(final @NotNull Location l) {
        final World w = l.getWorld();
        if (!w.getChunkAt(l).isLoaded()) {
            w.loadChunk(w.getChunkAt(l));
        }
    }
}
