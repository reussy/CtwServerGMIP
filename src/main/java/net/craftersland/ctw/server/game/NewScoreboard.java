package net.craftersland.ctw.server.game;

import dev.jcsoftware.jscoreboards.JGlobalScoreboard;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

public class NewScoreboard {

    private JGlobalScoreboard scoreboard;
    private String timer;
    private int startTime;
    private final CTW ctw;
    private String players;
    private String teamblue;
    private String teamred;
    private String date;
    private String cyanWool;
    private String blueWool;
    private String redWool;
    private String pinkWool;
    private int alertStatus;
    private String woolPickedUp;
    private String woolNotPlaced;
    private String woolPlaced;


    public NewScoreboard(final CTW ctw) {

        this.ctw = ctw;
        this.initializeVariables();
        alerts();
        this.timerTask();
        this.teamCountTask();
        this.setScoreboard();

    }


    public void alerts() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (this.ticks == 0) {
                    NewScoreboard.this.alertStatus = 0;
                }
                if (this.ticks == 1) {
                    NewScoreboard.this.alertStatus = 1;
                }
                ++this.ticks;
                if (this.ticks == 2) {
                    this.ticks = 0;
                }
            }
        }, 0L, 20);
    }


    public void initializeVariables() {

        this.startTime = 0;
        this.timer = "cargando";
        this.players = "cargando";
        this.woolNotPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolNotPlaced");
        this.woolPickedUp = ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.cyanWool = ChatColor.DARK_AQUA + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.blueWool = ChatColor.BLUE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.redWool = ChatColor.RED + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");

        this.teamblue = String.valueOf(ChatColor.BLUE) + ChatColor.BOLD + NewScoreboard.this.ctw.getLanguageHandler().getMessage("Words.BlueTeam");
        this.teamred = String.valueOf(ChatColor.RED) + ChatColor.BOLD + NewScoreboard.this.ctw.getLanguageHandler().getMessage("Words.RedTeam");
        this.date = ChatColor.translateAlternateColorCodes('&', "&7" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

    }


    public void setScoreboard() {
        this.scoreboard = new JGlobalScoreboard(

                () -> String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + "]",

                () -> Arrays.asList(" ", date, " ", timer, players, " ", teamblue, cyanWool, blueWool, " ", teamred, pinkWool, redWool, " ", "&emc.gamesmadeinpola.com")

        );
    }


    public void teamCountTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, () -> {
            players = "  " + ChatColor.BLUE + NewScoreboard.this.ctw.getTeamHandler().countBlueTeam() + ChatColor.GRAY + " vs " + ChatColor.RED + NewScoreboard.this.ctw.getTeamHandler().countRedTeam();
        }, 18L, 20L);

    }

    public void timerTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, () -> {

            String newTime;

            int time = this.startTime;

            int hour = time / 3600;
            int min = ((time - hour * 3600) / 60);
            int sec = time - (hour * 3600 + min * 60);


            if (hour == 0) {

                if (min < 10) {
                    if (sec < 10) {

                        newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + "0" + min + ":" + "0" + sec;

                    } else {

                        newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + "0" + min + ":" + sec;

                    }
                } else {

                    if (sec < 10) {

                        newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + min + ":" + "0" + sec;

                    } else {

                        newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + min + ":" + sec;
                    }
                }


            } else {

                if (min < 10) {

                    newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + "0" + min + ":" + sec;
                } else {
                    newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + hour + ":" + min + ":" + sec;

                }


                newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + "0" + min + ":" + sec;
            }

            NewScoreboard.this.timer = newTime;
            scoreboard.updateScoreboard();
            this.startTime++;

        }, 18L, 20L);
    }

    public void cyanWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!NewScoreboard.this.ctw.getWoolHandler().isCyanPlaced()) {
                    if (NewScoreboard.this.ctw.getWoolHandler().listPlayerscyan().size() == 0) {

                        NewScoreboard.this.cyanWool = ChatColor.DARK_AQUA + NewScoreboard.this.woolNotPlaced + ChatColor.GRAY + " " + NewScoreboard.this.ctw.getLanguageHandler().getMessage("Words.Cyan");

                    } else {
                        if (NewScoreboard.this.alertStatus == 0) {
                            NewScoreboard.this.cyanWool = ChatColor.DARK_AQUA + NewScoreboard.this.woolPickedUp + ChatColor.GRAY + " " + NewScoreboard.this.ctw.getLanguageHandler().getMessage("Words.Cyan") + " \u26a0";
                        }
                        if (NewScoreboard.this.alertStatus == 1) {


                            NewScoreboard.this.cyanWool = ChatColor.DARK_AQUA + NewScoreboard.this.woolPickedUp + ChatColor.GRAY + " " + NewScoreboard.this.ctw.getLanguageHandler().getMessage("Words.Cyan") + ChatColor.RED + " \u26a0";

                        }
                    }
                } else {

                    cyanWool = ChatColor.DARK_AQUA + woolPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Cyan");
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 0, 20);
    }

    public void blueWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ctw.getWoolHandler().isBluePlaced()) {
                    if (ctw.getWoolHandler().listPlayersblue().size() == 0) {

                        blueWool = ChatColor.BLUE + woolNotPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Blue");

                    } else {
                        if (alertStatus == 0) {


                            blueWool = ChatColor.BLUE + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Blue") + " \u26a0";

                        }
                        if (alertStatus == 1) {

                            blueWool = ChatColor.BLUE + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Blue") + ChatColor.RED + " \u26a0";

                        }
                    }
                } else {
                    blueWool = ChatColor.BLUE + woolPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Blue");
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 0, 20);
    }

    public void redWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ctw.getWoolHandler().isRedPlaced()) {
                    if (ctw.getWoolHandler().listPlayersred().size() == 0) {

                        redWool = ChatColor.RED + woolNotPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Red");

                    } else {
                        if (alertStatus == 0) {


                            redWool = ChatColor.RED + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Red") + " \u26a0";

                        }
                        if (alertStatus == 1) {

                            redWool = ChatColor.RED + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Red") + ChatColor.RED + " \u26a0";

                        }
                    }
                } else {
                    redWool = ChatColor.RED + woolPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Red");
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 0, 20);
    }

    public void pinkWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ctw.getWoolHandler().isPinkPlaced()) {
                    if (ctw.getWoolHandler().listPlayerspink().size() == 0) {

                        pinkWool = ChatColor.LIGHT_PURPLE + woolNotPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Pink");

                    } else {
                        if (alertStatus == 0) {


                            pinkWool = ChatColor.LIGHT_PURPLE + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Pink") + " \u26a0";

                        }
                        if (alertStatus == 1) {

                            pinkWool = ChatColor.LIGHT_PURPLE + woolPickedUp + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Pink") + ChatColor.RED + " \u26a0";

                        }
                    }
                } else {
                    pinkWool = ChatColor.LIGHT_PURPLE + woolPlaced + ChatColor.GRAY + " " + ctw.getLanguageHandler().getMessage("Words.Pink");
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 0, 20);
    }


    public void addPlayer(Player player) {

        this.scoreboard.addPlayer(player);
        this.scoreboard.updateScoreboard();

    }

    public void removePlayer(Player player) {

        this.scoreboard.removePlayer(player);
        this.scoreboard.updateScoreboard();

    }

}
