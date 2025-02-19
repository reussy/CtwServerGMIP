package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Joining implements Listener {
    private final CTW ctw;

    public Joining(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onLogin(final @NotNull PlayerLoginEvent event) {

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;
        
        Player player = event.getPlayer();
        Inventory joinTeamMenu = this.ctw.getJoinMenu().JoinMenuGUI(player);

        this.ctw.getPlayerHandler().addSpectator(player);
        this.ctw.getNewScoreboardHandler().addPlayer(player);

        Bukkit.getScheduler().runTaskLater(this.ctw, () -> {
            //this.ctw.getScoreboardHandler().setScoreboardToPlayer(p);
            this.ctw.getPlayerHandler().sendJoinMessage(player);
            this.ctw.getMessageUtils().sendTabTitleFooter(player);
            this.ctw.getPlayerScoreHandler().loadInitialScore(player);
            this.ctw.getPlayerScoreHandler().setEffect(player);
            this.ctw.getPlayerKillsHandler().loadKills(player);
            this.ctw.getPlayerWoolsPlacedHandler().loadData(player);
            this.ctw.getPlayerBowDistanceKillHandler().loadData(player);
            this.ctw.getKillStreakHandler().resetData(player);
        }, 1L);

        // Load achievements for player
        this.ctw.getWoolAchievementHandler().loadInitialAchievements(player);
        this.ctw.getShooterAchievementHandler().loadInitialAchievements(player);
        this.ctw.getMeleeAchievementHandler().loadInitialAchievements(player);
        this.ctw.getOverpoweredAchievementHandler().loadInitialAchievements(player);
        this.ctw.getDistanceAchievementHandler().loadInitialAchievements(player);
        
        Bukkit.getScheduler().runTaskLater(this.ctw, () -> player.openInventory(joinTeamMenu), 15L);
    }
}
