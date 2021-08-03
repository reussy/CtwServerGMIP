package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;

public class Tasks {
    private final CTW ctw;

    public Tasks(final CTW ctw) {
        this.ctw = ctw;
        this.tenSecTask();

    }

    private void tenSecTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                Tasks.this.ctw.getSpectatorTask().sendSpectatorMessage();
                Tasks.this.ctw.getLobbyServersHandler().updateLobbysStatus();
                Tasks.this.ctw.getLobbyLink().sendMotd();
                Tasks.this.ctw.getEffectUtils().checkForVipEffects();
            }
        }, 20L, 200L);
    }

    private void GameStage() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                System.out.print(Tasks.this.ctw.getGameEngine().gameStage);
            }
        }, 20L, 40);
    }


}
