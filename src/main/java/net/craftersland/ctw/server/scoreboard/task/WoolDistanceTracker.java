package net.craftersland.ctw.server.scoreboard.task;

import com.cryptomorin.xseries.XMaterial;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import net.craftersland.ctw.server.utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class WoolDistanceTracker {
    private final CTW plugin;

    private final Map<Player, TeamHandler.Team> playerTeams = new HashMap<>(); // Almacena el equipo de cada jugador

    public WoolDistanceTracker(CTW plugin) {
        this.plugin = plugin;
        // Iniciar el rastreador de distancia
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkNearestWoolCarrier(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 100L); // Cada segundo
    }

    public @NotNull String checkNearestWoolCarrier(Player player) {
        TeamHandler.Team team = plugin.getTeamHandler().getTeam(player);
        if (team == null) return "";

        Player nearestWoolCarrier = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            if (otherPlayer.equals(player)) continue;
            if (team.equals(plugin.getTeamHandler().getTeam(otherPlayer))) continue;
            if (!carriesWool(otherPlayer, team)) continue;

            double distance = player.getLocation().distance(otherPlayer.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestWoolCarrier = otherPlayer;
            }
        }

        if (nearestWoolCarrier != null) {
            plugin.getMessageUtils().sendActionBarMessage(PluginUtil.colorize(plugin.getLanguageHandler().getMessage("ChatMessages.NearWoolDistance").replace("%Distance%", String.valueOf(nearestDistance))), player);
            return String.valueOf((int) nearestDistance);
        }

        return "0";
    }

    private boolean carriesWool(Player player, TeamHandler.Team team) {
        // Simulación: Verifica si el jugador tiene la lana robada según su equipo
        return (team == TeamHandler.Team.RED && hasRedOrPinkWool(player)) || (team == TeamHandler.Team.BLUE && hasBlueOrCyanWool(player));
    }

    private boolean hasRedOrPinkWool(@NotNull Player player) {
        // Aquí iría tu lógica real para verificar si lleva lana roja o rosa
        return player.getInventory().contains(XMaterial.RED_WOOL.get()) || player.getInventory().contains(XMaterial.PINK_WOOL.get());
    }

    private boolean hasBlueOrCyanWool(@NotNull Player player) {
        // Aquí iría tu lógica real para verificar si lleva lana azul o cyan
        return player.getInventory().contains(XMaterial.BLUE_WOOL.get()) || player.getInventory().contains(XMaterial.CYAN_WOOL.get());
    }

    public Map<Player, TeamHandler.Team> getPlayerTeams() {
        return playerTeams;
    }
}
