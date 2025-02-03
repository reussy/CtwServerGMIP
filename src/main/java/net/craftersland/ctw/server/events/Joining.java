package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;

public class Joining implements Listener {
    private final CTW ctw;

    public Joining(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        final Player p = event.getPlayer();
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, () -> {
            if (event.getResult().toString().equals("ALLOWED")) {
                Joining.this.ctw.getPlayerHandler().addSpectator(p);
                //Joining.this.ctw.getScoreboardHandler().setScoreboardToPlayer(p);
                Joining.this.ctw.getNewScoreboardHandler().addPlayer(p);
                Joining.this.ctw.getPlayerHandler().sendJoinMessage(p);
                Joining.this.ctw.getMessageUtils().sendTabTitleFooter(p);
                Joining.this.ctw.getPlayerScoreHandler().loadInitialScore(p);
                Joining.this.ctw.getPlayerScoreHandler().setEffect(p);
                Joining.this.ctw.getPlayerKillsHandler().loadKills(p);
                Joining.this.ctw.getPlayerWoolsPlacedHandler().loadData(p);
                Joining.this.ctw.getPlayerBowDistanceKillHandler().loadData(p);
                Joining.this.ctw.getWoolAchievementHandler().loadInitialAchievements(p);
                Joining.this.ctw.getShooterAchievementHandler().loadInitialAchievements(p);
                Joining.this.ctw.getMeleeAchievementHandler().loadInitialAchievements(p);
                Joining.this.ctw.getOverpoweredAchievementHandler().loadInitialAchievements(p);
                Joining.this.ctw.getDistanceAchievementHandler().loadInitialAchievements(p);
                Joining.this.ctw.getKillStreakHandler().resetData(p);

            }
        }, 1L);


        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, () -> {
            if (event.getResult().toString().equals("ALLOWED")) {
                final Inventory inv = Joining.this.ctw.getJoinMenu().JoinMenuGUI(p);
                p.openInventory(inv);

            }
        }, 30);
    }
}
