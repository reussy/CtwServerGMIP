package net.craftersland.ctw.server;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PAPI extends PlaceholderExpansion {

    private final CTW plugin;

    public PAPI(CTW plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "ctw";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GMIP";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        if (player == null) {
            return "";
        }

        //General placeholders

        if (params.equalsIgnoreCase("map")) {
            return plugin.getMapHandler().currentMap;
        }

        if (params.equalsIgnoreCase("red_victories")) {
            return plugin.getTeamScoreHandler().getRedScore() + "";
        }

        if (params.equalsIgnoreCase("blue_victories")) {
            return plugin.getTeamScoreHandler().getBlueScore() + "";
        }

        if (params.equalsIgnoreCase("red_players")) {
            return plugin.getTeamHandler().redTeamCopy().size() + "";
        }

        if (params.equalsIgnoreCase("blue_players")) {
            return plugin.getTeamHandler().blueTeamCopy().size() + "";
        }

        // Player specific placeholders

        if (!player.isOnline()) {
            return "Offline";
        }

        if (params.equalsIgnoreCase("player_melee_kills")) {
            return plugin.getPlayerKillsHandler().getMeleeKills(player.getPlayer()) + "";
        }

        if (params.equalsIgnoreCase("player_bow_kills")) {
            return plugin.getPlayerKillsHandler().getBowKills(player.getPlayer()) + "";
        }

        if (params.equalsIgnoreCase("player_total_kills")) {
            return plugin.getPlayerKillsHandler().getTotalKills(player.getPlayer()) + "";
        }

        if (params.equalsIgnoreCase("player_match_melee_kills")) {
            return plugin.getPlayerKillsHandler().getMeleeKillsMatch(player.getPlayer()) + "";
        }

        if (params.equalsIgnoreCase("player_match_bow_kills")) {
            return plugin.getPlayerKillsHandler().getBowKillsMatch(player.getPlayer()) + "";
        }

        if (params.equalsIgnoreCase("player_match_total_kills")) {
            return plugin.getPlayerKillsHandler().getKillsMatch(player.getPlayer()) + "";
        }

        return "...";
    }
}
