package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.utils.StartupKit;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GameEngine {
    private final CTW ctw;
    public GameStages gameStage;
    public String motd;
    private int countdown;

    public GameEngine(@NotNull CTW ctw) {
        this.ctw = ctw;
        this.countdown = 35;
        this.motd = formatColor(ctw.getLanguageHandler().getMessage("MOTD-Status.Loading"));
        this.gameStage = GameStages.LOADING;
        scheduleTasks();
    }

    private void scheduleTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(ctw, () -> {
            if (gameStage == GameStages.COUNTDOWN) {
                countdownStage();
            } else if (gameStage == GameStages.RUNNING) {
                checkForWoolsPlaced();
                motd = formatColor(ctw.getLanguageHandler().getMessage("MOTD-Status.Status")
                        .replace("%MapName%", ctw.getMapHandler().currentMap));
            }
            ctw.getJoinMenu().menuUpdateTask();
            ctw.getRestartHandler().checkMemoryUsage();
        }, 20L, 20L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(ctw, () -> ctw.map = ctw.getMapHandler().currentMap, 20L, 1000L);
    }

    private void checkForWoolsPlaced() {
        if (ctw.getWoolHandler().isRedPlaced() && ctw.getWoolHandler().isPinkPlaced()) {
            endGame(GameStages.COUNTDOWN, this::redTeamWonStage);
        } else if (ctw.getWoolHandler().isBluePlaced() && ctw.getWoolHandler().isCyanPlaced()) {
            endGame(GameStages.COUNTDOWN, this::blueTeamWonStage);
        }
    }

    private void endGame(GameStages nextStage, @NotNull Runnable victoryMethod) {
        gameStage = GameStages.IDLE;
        victoryMethod.run();
        gameStage = nextStage;
    }

    private void countdownStage() {
        try {
            if (countdown == 30) {
                ctw.getMapHandler().getNextMap();
                this.ctw.getPlayerKillsHandler().orderKills();
                checkForServerRestart();
                broadcastCountdown("CountdownStart");
            } else if (countdown == 20 || countdown == 10) {
                broadcastCountdown("CountdownProgress");
                ctw.getMapHandler().loadNextMap();
            } else if (countdown <= 5 && countdown > 0) {
                broadcastCountdown("CountdownLast5sec");
            } else if (countdown == 0) {
                startGame();
            }
            countdown--;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkForServerRestart() {
        if (ctw.getConfigHandler().getInteger("Settings.ServerRestartAfterGamesPlayed") != 0
                && ctw.getMapHandler().getPlayedMaps() > ctw.getConfigHandler().getInteger("Settings.ServerRestartAfterGamesPlayed")) {
            ctw.getRestartHandler().serverStop();
        }
    }

    private void broadcastCountdown(@NotNull String key) {
        if (key.equals("CountdownStart")) {
            for (int i = 0; i < ctw.getLanguageHandler().getMessageList("ChatMessages.CountdownStart").size(); i++) {
                String message = formatColor(ctw.getLanguageHandler().getMessageList("ChatMessages.CountdownStart").get(i)
                        .replace("%MapName%", ctw.getMapHandler().currentMap));
                Bukkit.broadcastMessage(message);
            }
            return;
        }

        String message = formatColor(ctw.getLanguageHandler().getMessage("ChatMessages." + key)
                .replace("%MapName%", ctw.getMapHandler().currentMap)
                .replace("%countdown%", String.valueOf(countdown)));
        Bukkit.broadcastMessage(message);
    }

    private void startGame() {

        this.ctw.getMapHandler().startNextMap();
        ctw.getPlayerKillsHandler().resetTotalKillsMatch();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (this.ctw.getTeamHandler().isSpectator(player)) return;
            this.ctw.getEconomyHandler().resetCoins(player);
        });

        countdown = 35;
        gameStage = GameStages.RUNNING;
        ctw.getMessageUtils().broadcastTitleMessage(ctw.getLanguageHandler().getMessage("TitleMessages.CountdownOver.title"),
                ctw.getLanguageHandler().getMessage("TitleMessages.CountdownOver.subtitle"));
        ctw.getSoundHandler().broadcastLevelUpSound();
        ctw.map = ctw.getMapHandler().currentMap;
        Bukkit.getScheduler().runTaskLater(ctw, () -> changeGameMode(GameMode.SURVIVAL), 5L);
        Bukkit.getOnlinePlayers().forEach(StartupKit::setUnbreakableArmor);
    }

    private void redTeamWonStage() {
        declareVictory(TeamHandler.Teams.RED, "RedVictory");
    }

    private void blueTeamWonStage() {
        declareVictory(TeamHandler.Teams.BLUE, "BlueVictory");
    }

    private void declareVictory(TeamHandler.Teams team, String victoryKey) {
        try {
            List<Player> winners = (team == TeamHandler.Teams.RED) ? ctw.getTeamHandler().redTeamCopy() : ctw.getTeamHandler().blueTeamCopy();
            //winners.forEach(p -> Bukkit.dispatchCommand(ctw.getServer().getConsoleSender(), "mysterydust add " + p.getName() + " 12"));
            setWonSpectators(winners); // ??
            ctw.getSoundHandler().broadcastDragon();
            ctw.getMessageUtils().broadcastGameStats();
            ctw.getEffectUtils().sendTextParticles(team);
            Bukkit.getScheduler().runTaskLaterAsynchronously(ctw, () -> ctw.getMessageUtils().broadcastTitleMessage(
                    formatColor(ctw.getLanguageHandler().getMessage("TitleMessages." + victoryKey + ".title")),
                    formatColor(ctw.getLanguageHandler().getMessage("TitleMessages." + victoryKey + ".subtitle"))), 30L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(ctw, () -> changeGameMode(GameMode.SPECTATOR), 5L);
    }

    private void setWonSpectators(@NotNull List<Player> players) {
        players.forEach(p -> ctw.getPlayerHandler().playerSetWonSpectator(p));
    }

    private void changeGameMode(GameMode gameMode) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (ctw.getTeamHandler().isBlueTeam(player) || ctw.getTeamHandler().isRedTeam(player)) {
                player.setGameMode(gameMode);
            }
        });
    }

    @Contract(pure = true)
    private @NotNull String formatColor(@NotNull String message) {
        return message.replaceAll("&", "§");
    }

    public enum GameStages {
        LOADING, RUNNING, NEXTMAP, IDLE, COUNTDOWN
    }
}
