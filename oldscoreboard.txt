package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ScoreboardHandler {
    private final CTW ctw;
    private Scoreboard scoreboard;
    private String timer;
    private String players;
    private String totalplayers;
    private String redWool;
    private String pinkWool;
    private String blueWool;
    private String cyanWool;
    private String woolNotPlaced;
    private String woolPickedUp;
    private String woolPlaced;
    private int alertStatus;
    private long startTime;

    public ScoreboardHandler(final CTW ctw) {
        this.woolNotPlaced = "  \u2b1c";
        this.woolPickedUp = "  \u20de";
        this.woolPlaced = "  \u2b1b";
        this.startTime = 0L;
        this.ctw = ctw;
        this.createScoreboard();
        this.setInitialGameStats();
        this.createRedTeam();
        this.createBlueTeam();
        this.updateTeamCountTask();
        this.updatePlayerCountTask();
        this.timerTask();
        this.alerts();
        this.redWoolTaken();
        this.pinkWoolTaken();
        this.blueWoolTaken();
        this.cyanWoolTaken();
        ScoreboardHandler.this.woolPickedUp = ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
    }

    static void access$1(final ScoreboardHandler scoreboardHandler, final String woolNotPlaced) {
        scoreboardHandler.woolNotPlaced = woolNotPlaced;
    }

    static void access$3(final ScoreboardHandler scoreboardHandler, final String redWool) {
        scoreboardHandler.redWool = redWool;
    }

    static void access$4(final ScoreboardHandler scoreboardHandler, final String pinkWool) {
        scoreboardHandler.pinkWool = pinkWool;
    }

    static void access$5(final ScoreboardHandler scoreboardHandler, final String blueWool) {
        scoreboardHandler.blueWool = blueWool;
    }

    static void access$6(final ScoreboardHandler scoreboardHandler, final String cyanWool) {
        scoreboardHandler.cyanWool = cyanWool;
    }

    static void access$7(final ScoreboardHandler scoreboardHandler, final String players) {
        scoreboardHandler.players = players;
    }

    static void access$8(final ScoreboardHandler scoreboardHandler, final String timer) {
        scoreboardHandler.timer = timer;
    }

    static void access$9(final ScoreboardHandler scoreboardHandler, final String timer) {
        scoreboardHandler.totalplayers = String.valueOf(timer);
    }

    private void createScoreboard() {
        final ScoreboardManager sm = Bukkit.getScoreboardManager();
        final Scoreboard sc = sm.getNewScoreboard();
        final Objective objective1 = sc.registerNewObjective("GameStats", "dummy");
        objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective1.setDisplayName("Game Stats");
        final Objective objective2 = sc.registerNewObjective("Health", "dummy");
        objective2.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective2.setDisplayName(this.ctw.getLanguageHandler().getMessage("Scoreboard.Health").replaceAll("&", "§"));
        this.scoreboard = sc;
    }

    private void setInitialGameStats() {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                ScoreboardHandler.access$1(ScoreboardHandler.this, ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolNotPlaced"));
                ScoreboardHandler.access$3(ScoreboardHandler.this, ChatColor.RED + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Red"));
                ScoreboardHandler.access$4(ScoreboardHandler.this, ChatColor.LIGHT_PURPLE + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Pink"));
                ScoreboardHandler.access$5(ScoreboardHandler.this, ChatColor.BLUE + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Blue"));
                ScoreboardHandler.access$6(ScoreboardHandler.this, ChatColor.DARK_AQUA + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Cyan"));
                ScoreboardHandler.access$7(ScoreboardHandler.this, "  " + ChatColor.BLUE + "0" + ChatColor.GRAY + " vs " + ChatColor.RED + "0");
                ScoreboardHandler.access$8(ScoreboardHandler.this, ChatColor.GRAY + "-= " + ChatColor.WHITE + "--:--" + ChatColor.GRAY + " =-");
                ScoreboardHandler.access$9(ScoreboardHandler.this, String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + ScoreboardHandler.this.totalplayers + ChatColor.GOLD + ChatColor.BOLD + "]");
                final Objective obj = ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
                obj.setDisplayName(String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + "]");
                final Score space = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&m"));
                space.setScore(14);
                final Score date = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&7" + new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
                date.setScore(13);
                final Score s01 = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&l"));
                s01.setScore(12);
                final Score s2 = obj.getScore(ScoreboardHandler.this.timer);
                s2.setScore(11);
                final Score s3 = obj.getScore(ScoreboardHandler.this.players);
                s3.setScore(10);
                final Score s4 = obj.getScore(" ");
                s4.setScore(9);
                final Score s5 = obj.getScore(new StringBuilder().append(ChatColor.BLUE).append(ChatColor.BOLD).append(ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.BlueTeam")).toString());
                s5.setScore(8);
                final Score s6 = obj.getScore(ScoreboardHandler.this.cyanWool);
                s6.setScore(7);
                final Score s7 = obj.getScore(ScoreboardHandler.this.blueWool);
                s7.setScore(6);
                final Score s8 = obj.getScore(ChatColor.translateAlternateColorCodes('&', "&r"));
                s8.setScore(5);
                final Score s9 = obj.getScore(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.RedTeam")).toString());
                s9.setScore(4);
                final Score s10 = obj.getScore(ScoreboardHandler.this.pinkWool);
                s10.setScore(3);
                final Score s11 = obj.getScore(ScoreboardHandler.this.redWool);
                s11.setScore(2);
                final Score s12 = obj.getScore("  ");
                s12.setScore(1);
                final Score s13 = obj.getScore("§emc.gamesmadeinpola.com");
                s13.setScore(0);
            }
        });
    }

    public void updateHealthTask() {
        final Objective obj = this.scoreboard.getObjective("Health");
        obj.setDisplayName(this.ctw.getLanguageHandler().getMessage("Scoreboard.Health").replaceAll("&", "§"));
        final List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
        for (final Player p : onlinePlayers) {
            if (p.isOnline()) {
                final Score score = obj.getScore(p.getName());
                final int hp = (int) p.getHealth();
                score.setScore(hp);
            }
        }
    }

    public void startTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        this.startTime = 0L;
    }

    public void timerTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                if (ScoreboardHandler.this.startTime != 0L) {
                    final long currentTime = System.currentTimeMillis();
                    final long time = currentTime - ScoreboardHandler.this.startTime;
                    final Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    final String timeString = new SimpleDateFormat("mm:ss").format(cal.getTime());
                    final String newTime = String.valueOf(ChatColor.WHITE) + ChatColor.BOLD + ScoreboardHandler.this.ctw.getMapHandler().currentMap + ChatColor.WHITE + ": " + timeString;
                    if (!ScoreboardHandler.this.timer.matches(newTime)) {
                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.timer);
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(newTime).setScore(11);
                        ScoreboardHandler.access$8(ScoreboardHandler.this, newTime);
                    }
                } else {
                    final String newTime2 = ChatColor.GRAY + "-= " + ChatColor.WHITE + "--:--" + ChatColor.GRAY + " =-";
                    if (!ScoreboardHandler.this.timer.matches(newTime2)) {
                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.timer);
                        ScoreboardHandler.access$8(ScoreboardHandler.this, newTime2);
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.timer).setScore(11);
                    }
                }
            }
        }, 18L, 20L);
    }

    public void updateTeamCountTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                final String players2 = "  " + ChatColor.BLUE + ScoreboardHandler.this.ctw.getTeamHandler().countBlueTeam() + ChatColor.GRAY + " vs " + ChatColor.RED + ScoreboardHandler.this.ctw.getTeamHandler().countRedTeam();
                if (!players2.matches(ScoreboardHandler.this.players)) {
                    ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.players);
                    ScoreboardHandler.access$7(ScoreboardHandler.this, players2);
                    ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.players).setScore(10);
                }
            }
        }, 15L, 20L);
    }

    public void updatePlayerCountTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                final String players2 = String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + "]";
                ScoreboardHandler.access$9(ScoreboardHandler.this, players2);
                final Objective obj = ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
                obj.setDisplayName(String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + "]");
            }
        }, 15L, 20L);
    }

    public void resetWools() {
        this.woolNotPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolNotPlaced");
        final Objective obj = this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
        obj.setDisplayName(String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + "CTW [" + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size() + ChatColor.GOLD + ChatColor.BOLD + "]");
        this.scoreboard.resetScores(this.redWool);
        this.redWool = ChatColor.RED + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.redWool).setScore(2);
        this.scoreboard.resetScores(this.pinkWool);
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.pinkWool).setScore(3);
        this.scoreboard.resetScores(this.blueWool);
        this.blueWool = ChatColor.BLUE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.blueWool).setScore(6);
        this.scoreboard.resetScores(this.cyanWool);
        this.cyanWool = ChatColor.DARK_AQUA + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.cyanWool).setScore(7);

        this.redWoolTaken();
        this.pinkWoolTaken();
        this.blueWoolTaken();
        this.cyanWoolTaken();
        ScoreboardHandler.this.woolPickedUp = ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
    }

    public void alerts() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.ctw, new Runnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (this.ticks == 0) {
                    ScoreboardHandler.this.alertStatus = 0;
                }
                if (this.ticks == 1) {
                    ScoreboardHandler.this.alertStatus = 1;
                }
                ++this.ticks;
                if (this.ticks == 2) {
                    this.ticks = 0;
                }
            }
        }, 0L, 20);
    }

    public void redWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ScoreboardHandler.this.ctw.getWoolHandler().isRedPlaced()) {
                    if (ScoreboardHandler.this.ctw.getWoolHandler().listPlayersred().size() == 0) {

                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.redWool);
                        ScoreboardHandler.this.redWool = ChatColor.RED + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Red");
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.redWool).setScore(2);
                    } else {
                        if (ScoreboardHandler.this.alertStatus == 0) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.redWool);
                            ScoreboardHandler.this.redWool = ChatColor.RED + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Red") + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.redWool).setScore(2);
                        }
                        if (ScoreboardHandler.this.alertStatus == 1) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.redWool);
                            ScoreboardHandler.this.redWool = ChatColor.RED + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Red") + ChatColor.RED + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.redWool).setScore(2);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 20L, 20);
    }

    public void pinkWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ScoreboardHandler.this.ctw.getWoolHandler().isPinkPlaced()) {
                    if (ScoreboardHandler.this.ctw.getWoolHandler().listPlayerspink().size() == 0) {

                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.pinkWool);
                        ScoreboardHandler.this.pinkWool = ChatColor.LIGHT_PURPLE + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Pink");
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.pinkWool).setScore(3);
                    } else {
                        if (ScoreboardHandler.this.alertStatus == 0) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.pinkWool);
                            ScoreboardHandler.this.pinkWool = ChatColor.LIGHT_PURPLE + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Pink") + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.pinkWool).setScore(3);
                        }
                        if (ScoreboardHandler.this.alertStatus == 1) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.pinkWool);
                            ScoreboardHandler.this.pinkWool = ChatColor.LIGHT_PURPLE + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Pink") + ChatColor.RED + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.pinkWool).setScore(3);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 20L, 20);
    }


    public void blueWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ScoreboardHandler.this.ctw.getWoolHandler().isBluePlaced()) {
                    if (ScoreboardHandler.this.ctw.getWoolHandler().listPlayersblue().size() == 0) {

                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.blueWool);
                        ScoreboardHandler.this.blueWool = ChatColor.BLUE + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Blue");
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.blueWool).setScore(6);
                    } else {
                        if (ScoreboardHandler.this.alertStatus == 0) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.blueWool);
                            ScoreboardHandler.this.blueWool = ChatColor.BLUE + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Blue") + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.blueWool).setScore(6);
                        }
                        if (ScoreboardHandler.this.alertStatus == 1) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.blueWool);
                            ScoreboardHandler.this.blueWool = ChatColor.BLUE + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Blue") + ChatColor.RED + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.blueWool).setScore(6);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 20L, 20);
    }

    public void cyanWoolTaken() {
        new BukkitRunnable() {
            public void run() {
                if (!ScoreboardHandler.this.ctw.getWoolHandler().isCyanPlaced()) {
                    if (ScoreboardHandler.this.ctw.getWoolHandler().listPlayerscyan().size() == 0) {

                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.cyanWool);
                        ScoreboardHandler.this.cyanWool = ChatColor.DARK_AQUA + ScoreboardHandler.this.woolNotPlaced + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Cyan");
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.cyanWool).setScore(7);
                    } else {
                        if (ScoreboardHandler.this.alertStatus == 0) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.cyanWool);
                            ScoreboardHandler.this.cyanWool = ChatColor.DARK_AQUA + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Cyan") + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.cyanWool).setScore(7);
                        }
                        if (ScoreboardHandler.this.alertStatus == 1) {

                            ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.cyanWool);
                            ScoreboardHandler.this.cyanWool = ChatColor.DARK_AQUA + ScoreboardHandler.this.woolPickedUp + ChatColor.GRAY + " " + ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.Cyan") + ChatColor.RED + " \u26a0";
                            ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.cyanWool).setScore(7);
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.ctw, 20L, 20);
    }

    public void redWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.redWool);
        this.redWool = ChatColor.RED + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.redWool).setScore(2);
    }

    public void pinkWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.pinkWool);
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.pinkWool).setScore(3);
    }

    public void blueWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.blueWool);
        this.blueWool = ChatColor.BLUE + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.blueWool).setScore(6);
    }

    public void cyanWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.cyanWool);
        this.cyanWool = ChatColor.DARK_AQUA + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.cyanWool).setScore(7);
    }

    private void createRedTeam() {
        final Team team = this.scoreboard.registerNewTeam("red");
        team.setPrefix(new StringBuilder().append(ChatColor.RED).toString());
        team.setDisplayName(new StringBuilder().append(ChatColor.RED).toString());
        team.setCanSeeFriendlyInvisibles(true);
        team.setAllowFriendlyFire(false);
    }

    private void createBlueTeam() {
        final Team team = this.scoreboard.registerNewTeam("blue");
        team.setPrefix(new StringBuilder().append(ChatColor.BLUE).toString());
        team.setDisplayName(new StringBuilder().append(ChatColor.BLUE).toString());
        team.setCanSeeFriendlyInvisibles(true);
        team.setAllowFriendlyFire(false);
    }

    public void addToRedTeam(final Player p) {
        this.scoreboard.getTeam("red").addEntry(p.getName());
    }

    public void addToBlueTeam(final Player p) {
        this.scoreboard.getTeam("blue").addEntry(p.getName());
    }

    public void removeFromTeams(final Player p) {
        this.scoreboard.getTeam("red").removeEntry(p.getName());
        this.scoreboard.getTeam("blue").removeEntry(p.getName());
    }

    public void setScoreboardToPlayer(final Player p) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                try {
                    p.setScoreboard(ScoreboardHandler.this.scoreboard);
                } catch (Exception ex) {
                }
            }
        });
    }
}
