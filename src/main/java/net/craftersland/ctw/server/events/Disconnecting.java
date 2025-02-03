package net.craftersland.ctw.server.events;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class Disconnecting implements Listener {
    private final CTW ctw;

    public Disconnecting(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onDisconnect(final PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
            final Player p = event.getPlayer();

            ctw.playerProjectile.remove(p.getUniqueId());

            Disconnecting.this.ctw.getTeamHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getNewScoreboardHandler().removePlayer(p);
            //Disconnecting.this.ctw.getNewScoreboardHandler().removeFromTeams(p);
            Disconnecting.this.ctw.getWoolHandler().removeBlueTakenByPlayer(p);
            Disconnecting.this.ctw.getWoolHandler().removeCyanTakenByPlayer(p);
            Disconnecting.this.ctw.getWoolHandler().removePinkTakenByPlayer(p);
            Disconnecting.this.ctw.getWoolHandler().removeRedTakenByPlayer(p);
            Disconnecting.this.ctw.getPlayerScoreHandler().saveScoreToDatabase(p);
            Disconnecting.this.ctw.getPlayerScoreHandler().removeEffect(p);
            Disconnecting.this.ctw.getPlayerKillsHandler().saveKillsToDatabasse(p);
            Disconnecting.this.ctw.getPlayerWoolsPlacedHandler().saveDataToDatabasse(p);
            Disconnecting.this.ctw.getPlayerBowDistanceKillHandler().saveDataToDatabasse(p);
            Disconnecting.this.ctw.getWoolAchievementHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getShooterAchievementHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getMeleeAchievementHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getOverpoweredAchievementHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getDistanceAchievementHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getKillStreakHandler().removeOnDisconnect(p);
            Disconnecting.this.ctw.getKitHandler().removeInventory(p);
            Disconnecting.this.ctw.getLastDamageHandler().removeData(p);
            Disconnecting.this.ctw.getEffectUtils().removeEffectsOnDisconnect(p);

        });
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
                    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF(this.ctw.getLobbyServersHandler().getLobbyToTeleport());
                    player.sendPluginMessage(this.ctw, "BungeeCord", out.toByteArray());
                });
            });
        }
    }
}
