package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private final CTW ctw;
    private int countdown;
    public GameStages gameStage;
    public String motd;

    public GameEngine(final CTW ctw) {
        this.countdown = 35;
        this.ctw = ctw;
        this.motd = ctw.getLanguageHandler().getMessage("MOTD-Status.Loading").replaceAll("&", "§");
        this.gameStage = GameStages.LOADING;
        this.gameEngineTask();
    }

    private void gameEngineTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                GameEngine.this.ctw.getScoreboardHandler().updateHealthTask();
                if (GameEngine.this.gameStage != GameStages.LOADING && GameEngine.this.gameStage != GameStages.IDLE) {
                    if (GameEngine.this.gameStage == GameStages.COUNTDOWN) {
                        GameEngine.this.countdownStage();
                    } else if (GameEngine.this.gameStage == GameStages.RUNNING) {
                        GameEngine.this.checkForWoolsPlaced();
                        final String rawMotd = GameEngine.this.ctw.getLanguageHandler().getMessage("MOTD-Status.Status").replace("%MapName%", GameEngine.this.ctw.getMapHandler().currentMap);
                        GameEngine.this.motd = rawMotd.replaceAll("&", "§");
                    }
                }
                GameEngine.this.ctw.getJoinMenu().menuUpdateTask();
                GameEngine.this.ctw.getRestartHandler().checkMemoryUsage();
            }
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, () -> {

            ctw.map = this.ctw.getMapHandler().currentMap;
        }, 20L, 1000);
    }

    private void checkForWoolsPlaced() {
        if (this.ctw.getWoolHandler().isRedPlaced() && this.ctw.getWoolHandler().isPinkPlaced()) {
            this.gameStage = GameStages.IDLE;
            this.redTeamWonStage();
            this.gameStage = GameStages.COUNTDOWN;
        } else if (this.ctw.getWoolHandler().isBluePlaced() && this.ctw.getWoolHandler().isCyanPlaced()) {
            this.gameStage = GameStages.IDLE;
            this.blueTeamWonStage();
            this.gameStage = GameStages.COUNTDOWN;
        }
    }

    private void countdownStage() {
        try {
            if (this.countdown == 30) {
                this.ctw.getMapHandler().getNextMap();
                if (this.ctw.getConfigHandler().getInteger("Settings.ServerRestartAfterGamesPlayed") != 0 && this.ctw.getMapHandler().getPlayedMaps() > this.ctw.getConfigHandler().getInteger("Settings.ServerRestartAfterGamesPlayed")) {
                    this.ctw.getRestartHandler().serverStop();
                }
                final List<String> rawMSG = new ArrayList<String>(this.ctw.getLanguageHandler().getMessageList("ChatMessages.CountdownStart"));
                final List<String> processedMSG = new ArrayList<String>();
                if (!rawMSG.isEmpty()) {
                    for (String s : rawMSG) {
                        s = s.replace("%MapName%", ChatColor.YELLOW + this.ctw.getMapHandler().currentMap);
                        processedMSG.add(s.replace("%countdown%", new StringBuilder().append(this.countdown).toString()));
                    }
                    for (final String s : processedMSG) {
                        Bukkit.broadcastMessage(s.replaceAll("&", "§"));
                    }
                }
                String rawTitle1 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownStart.title").replace("%MapName%", this.ctw.getMapHandler().currentMap);
                rawTitle1 = rawTitle1.replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                String rawsubtitle1 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownStart.subtitle").replace("%MapName%", this.ctw.getMapHandler().currentMap);
                rawsubtitle1 = rawsubtitle1.replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                this.ctw.getMessageUtils().broadcastTitleMessage(rawTitle1, rawsubtitle1);
                this.ctw.getSoundHandler().broadcastAnvilLandSound();
                this.ctw.getMessageUtils().broadcastTabTitleFooter();
                --this.countdown;
            } else if (this.countdown == 20) {
                final String rawMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.CountdownProgress").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                Bukkit.broadcastMessage(rawMsg.replaceAll("&", "§"));
                final String rawTitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownProgress.title").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                final String rawsubtitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownProgress.subtitle").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                this.ctw.getMessageUtils().broadcastTitleMessage(rawTitle2, rawsubtitle2);
                this.ctw.getSoundHandler().broadcastArrowHitPlayerSound();
                this.ctw.getMapHandler().loadNextMap();
                --this.countdown;
            } else if (this.countdown == 10) {
                final String rawMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.CountdownProgress").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                Bukkit.broadcastMessage(rawMsg.replaceAll("&", "§"));
                final String rawTitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownProgress.title").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                final String rawsubtitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownProgress.subtitle").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                this.ctw.getMessageUtils().broadcastTitleMessage(rawTitle2, rawsubtitle2);
                this.ctw.getSoundHandler().broadcastArrowHitPlayerSound();
                --this.countdown;
            } else if (this.countdown <= 5 && this.countdown > 1) {
                final String rawMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.CountdownLast5sec").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                Bukkit.broadcastMessage(rawMsg.replaceAll("&", "§"));
                final String rawTitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownLast5sec.title").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                final String rawsubtitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownLast5sec.subtitle").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                this.ctw.getMessageUtils().broadcastTitleMessage(rawTitle2, rawsubtitle2);
                this.ctw.getSoundHandler().broadcastArrowHitPlayerSound();
                --this.countdown;
            } else if (this.countdown == 1) {
                final String rawMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.CountdownLast5sec").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                Bukkit.broadcastMessage(rawMsg.replaceAll("&", "§"));
                final String rawTitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownLast5sec.title").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                final String rawsubtitle2 = this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownLast5sec.subtitle").replace("%countdown%", new StringBuilder().append(this.countdown).toString());
                this.ctw.getMessageUtils().broadcastTitleMessage(rawTitle2, rawsubtitle2);
                this.ctw.getSoundHandler().broadcastArrowHitPlayerSound();
                this.ctw.getMapHandler().startNextMap();
                --this.countdown;
            } else if (this.countdown == 0) {
                this.countdown = 35;
                this.gameStage = GameStages.RUNNING;
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownOver.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.CountdownOver.subtitle").replaceAll("&", "§"));
                this.ctw.getSoundHandler().broadcastLevelUpSound();
                ctw.map = this.ctw.getMapHandler().currentMap;

            } else {
                --this.countdown;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redTeamWonStage() {
        try {
            this.ctw.getTeamVictoryHandler().addRedVictoryPoint();
            final List<Player> red = this.ctw.getTeamHandler().redTeamCopy();
            final List<Player> blue = this.ctw.getTeamHandler().blueTeamCopy();
            this.setWonSpectators(red, blue);
            this.spawnRedFireworks(red);
            final List<String> rawMsg = new ArrayList<String>(this.ctw.getLanguageHandler().getMessageList("ChatMessages.RedVictory"));
            if (!rawMsg.isEmpty()) {
                for (final String s : rawMsg) {
                    Bukkit.broadcastMessage(s.replaceAll("&", "§"));
                }
            }
            this.ctw.getMessageUtils().broadcastGameStats();
            this.ctw.getEffectUtils().sendTextParticles(TeamHandler.Teams.RED);
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
                @Override
                public void run() {
                    GameEngine.this.ctw.getMessageUtils().broadcastTitleMessage(GameEngine.this.ctw.getLanguageHandler().getMessage("TitleMessages.RedVictory.title").replaceAll("&", "§"), GameEngine.this.ctw.getLanguageHandler().getMessage("TitleMessages.RedVictory.subtitle").replaceAll("&", "§"));
                    GameEngine.this.ctw.getSoundHandler().broadcastLevelUpSound();
                }
            }, 30L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void blueTeamWonStage() {
        try {
            this.ctw.getTeamVictoryHandler().addBlueVictoryPoint();
            final List<Player> red = this.ctw.getTeamHandler().redTeamCopy();
            final List<Player> blue = this.ctw.getTeamHandler().blueTeamCopy();
            this.setWonSpectators(red, blue);
            this.spawnBlueFireworks(blue);
            final List<String> rawMsg = new ArrayList<String>(this.ctw.getLanguageHandler().getMessageList("ChatMessages.BlueVictory"));
            if (!rawMsg.isEmpty()) {
                for (final String s : rawMsg) {
                    Bukkit.broadcastMessage(s.replaceAll("&", "§"));
                }
            }
            this.ctw.getMessageUtils().broadcastGameStats();
            this.ctw.getEffectUtils().sendTextParticles(TeamHandler.Teams.BLUE);
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
                @Override
                public void run() {
                    GameEngine.this.ctw.getMessageUtils().broadcastTitleMessage(GameEngine.this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueVictory.title"), GameEngine.this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueVictory.subtitle"));
                    GameEngine.this.ctw.getSoundHandler().broadcastLevelUpSound();
                }
            }, 30L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void spawnRedFireworks(final List<Player> red) {
        if (!red.isEmpty()) {
            for (final Player p : red) {
                if (p != null && p.isOnline()) {
                    this.ctw.getFireworks().spawnRedFirework(p.getLocation());
                    this.ctw.getPlayerScoreHandler().addScore(p, this.ctw.getConfigHandler().getInteger("Rewards.Score.victory"));
                    this.ctw.getEconomyHandler().addCoins(p, (double) this.ctw.getConfigHandler().getInteger("Rewards.Coins.victory"));
                    this.ctw.getMessageUtils().sendScoreMessage(p, "+" + this.ctw.getConfigHandler().getInteger("Rewards.Coins.victory"), this.ctw.getConfigHandler().getInteger("Rewards.Score.victory"));
                }
            }
        }
    }

    private void spawnBlueFireworks(final List<Player> blue) {
        if (!blue.isEmpty()) {
            for (final Player p : blue) {
                if (p != null && p.isOnline()) {
                    this.ctw.getFireworks().spawnBlueFirework(p.getLocation());
                    this.ctw.getPlayerScoreHandler().addScore(p, this.ctw.getConfigHandler().getInteger("Rewards.Score.victory"));
                    this.ctw.getEconomyHandler().addCoins(p, (double) this.ctw.getConfigHandler().getInteger("Rewards.Coins.victory"));
                    this.ctw.getMessageUtils().sendScoreMessage(p, "+" + this.ctw.getConfigHandler().getInteger("Rewards.Coins.victory"), this.ctw.getConfigHandler().getInteger("Rewards.Score.victory"));
                }
            }
        }
    }

    private void setWonSpectators(final List<Player> red, final List<Player> blue) {
        if (!red.isEmpty()) {
            for (final Player p : red) {
                if (p != null && p.isOnline()) {
                    this.ctw.getPlayerHandler().playerSetWonSpectator(p);
                }
            }
        }
        if (!blue.isEmpty()) {
            for (final Player p : blue) {
                if (p != null && p.isOnline()) {
                    this.ctw.getPlayerHandler().playerSetWonSpectator(p);
                }
            }
        }
    }

    public enum GameStages {
        LOADING("LOADING", 0),
        RUNNING("RUNNING", 1),
        NEXTMAP("NEXTMAP", 2),
        IDLE("IDLE", 3),
        COUNTDOWN("COUNTDOWN", 4);

        GameStages(final String s, final int n) {
        }
    }
}
