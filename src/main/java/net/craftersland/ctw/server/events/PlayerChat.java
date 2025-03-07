package net.craftersland.ctw.server.events;

import me.clip.placeholderapi.PlaceholderAPI;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerChat implements Listener {
    private final CTW ctw;
    private final Map<UUID, Long> globalChatCooldown;
    private static final int GLOBAL_CHAT_COOLDOWN = 1;


    public PlayerChat(final CTW ctw) {
        this.ctw = ctw;
        this.globalChatCooldown = new HashMap<>();
    }

    @EventHandler
    public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        if (!event.isAsynchronous()) {
            Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> sendChatMessage(event));
        } else {
            sendChatMessage(event);
        }
    }

    private void sendChatMessage(@NotNull AsyncPlayerChatEvent event) {
        sendMessage(event.getPlayer(), PlaceholderAPI.setPlaceholders(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', event.getMessage())));
    }

    private void sendMessage(Player player, @NotNull String message) {
        TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(player);
        boolean isGlobal = message.startsWith("!");

        if (isGlobal) {

            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            if (globalChatCooldown.containsKey(playerId)) {
                long lastTime = globalChatCooldown.get(playerId);
                if ((currentTime - lastTime) / 1000 < GLOBAL_CHAT_COOLDOWN) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalChatCooldown")));
                    return;
                }
            }

            globalChatCooldown.put(playerId, Long.valueOf(currentTime));

            message = message.substring(1).trim();
            String prefixTeam = this.ctw.getTeamHandler().isSpectator(player)
                    ? this.ctw.getLanguageHandler().getMessage("ChatMessages.SpectatorPrefix")
                    : this.ctw.getTeamHandler().isBlueTeam(player)
                    ? this.ctw.getLanguageHandler().getMessage("ChatMessages.BluePrefix")
                    : this.ctw.getLanguageHandler().getMessage("ChatMessages.RedPrefix");
            String globalMsg = this.ctw.getConfigHandler().getStringWithColor("ChatFormat.GlobalChatCmd")
                    .replace("<TeamPrefix>", prefixTeam)
                    .replace("<GlobalPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalPrefix"))
                    .replace("<PlayerName>", player.getName())
                    .replace("<Message>", message);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, globalMsg)));
            }
            return;
        }

        String teamFormat;
        List<Player> recipients;

        if (team == TeamHandler.Team.SPECTATOR) {
            teamFormat = "ChatFormat.Spectator";
            recipients = this.ctw.getTeamHandler().spectatorsCopy();
        } else {
            teamFormat = (team == TeamHandler.Team.BLUE) ? "ChatFormat.BlueTeam" : "ChatFormat.RedTeam";
            recipients = (team == TeamHandler.Team.BLUE) ? this.ctw.getTeamHandler().blueTeamCopy() : this.ctw.getTeamHandler().redTeamCopy();
        }

        String msg = this.ctw.getConfigHandler().getStringWithColor(teamFormat)
                .replace("<TeamPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.TeamPrefix"))
                .replace("<PlayerName>", player.getName())
                .replace("<Message>", message);

        String finalMsg = ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, msg));

        for (Player recipient : recipients) {
            recipient.sendMessage(finalMsg);
        }
    }
}
