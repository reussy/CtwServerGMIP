package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScoreboardHandler {
    private final CTW ctw;
    private Scoreboard scoreboard;
    private String timer;
    private String players;
    private String redWool;
    private String pinkWool;
    private String blueWool;
    private String cyanWool;
    private String woolNotPlaced;
    private String woolPickedUp;
    private String woolPlaced;
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
        this.timerTask();
    }

    static /* synthetic */ void access$1(final ScoreboardHandler scoreboardHandler, final String woolNotPlaced) {
        scoreboardHandler.woolNotPlaced = woolNotPlaced;
    }

    static /* synthetic */ void access$3(final ScoreboardHandler scoreboardHandler, final String redWool) {
        scoreboardHandler.redWool = redWool;
    }

    static /* synthetic */ void access$4(final ScoreboardHandler scoreboardHandler, final String pinkWool) {
        scoreboardHandler.pinkWool = pinkWool;
    }

    static /* synthetic */ void access$5(final ScoreboardHandler scoreboardHandler, final String blueWool) {
        scoreboardHandler.blueWool = blueWool;
    }

    static /* synthetic */ void access$6(final ScoreboardHandler scoreboardHandler, final String cyanWool) {
        scoreboardHandler.cyanWool = cyanWool;
    }

    static /* synthetic */ void access$7(final ScoreboardHandler scoreboardHandler, final String players) {
        scoreboardHandler.players = players;
    }

    static /* synthetic */ void access$8(final ScoreboardHandler scoreboardHandler, final String timer) {
        scoreboardHandler.timer = timer;
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
                final Objective obj = ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
                obj.setDisplayName(new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append(ScoreboardHandler.this.ctw.getMapHandler().currentMap).toString());
                final Score s0 = obj.getScore(ScoreboardHandler.this.timer);
                s0.setScore(10);
                final Score s2 = obj.getScore(ScoreboardHandler.this.players);
                s2.setScore(9);
                final Score s3 = obj.getScore(" ");
                s3.setScore(8);
                final Score s4 = obj.getScore(new StringBuilder().append(ChatColor.BLUE).append(ChatColor.BOLD).append(ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.BlueTeam")).toString());
                s4.setScore(7);
                final Score s5 = obj.getScore(ScoreboardHandler.this.cyanWool);
                s5.setScore(6);
                final Score s6 = obj.getScore(ScoreboardHandler.this.blueWool);
                s6.setScore(5);
                final Score s7 = obj.getScore("  ");
                s7.setScore(4);
                final Score s8 = obj.getScore(new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(ScoreboardHandler.this.ctw.getLanguageHandler().getMessage("Words.RedTeam")).toString());
                s8.setScore(3);
                final Score s9 = obj.getScore(ScoreboardHandler.this.pinkWool);
                s9.setScore(2);
                final Score s10 = obj.getScore(ScoreboardHandler.this.redWool);
                s10.setScore(1);
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
                    final String newTime = ChatColor.GRAY + "-= " + ChatColor.WHITE + timeString + ChatColor.GRAY + " =-";
                    if (!ScoreboardHandler.this.timer.matches(newTime)) {
                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.timer);
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(newTime).setScore(10);
                        ScoreboardHandler.access$8(ScoreboardHandler.this, newTime);
                    }
                } else {
                    final String newTime2 = ChatColor.GRAY + "-= " + ChatColor.WHITE + "--:--" + ChatColor.GRAY + " =-";
                    if (!ScoreboardHandler.this.timer.matches(newTime2)) {
                        ScoreboardHandler.this.scoreboard.resetScores(ScoreboardHandler.this.timer);
                        ScoreboardHandler.access$8(ScoreboardHandler.this, newTime2);
                        ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.timer).setScore(10);
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
                    ScoreboardHandler.this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(ScoreboardHandler.this.players).setScore(9);
                }
            }
        }, 15L, 20L);
    }

    public void resetWools() {
        this.woolNotPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolNotPlaced");
        final Objective obj = this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
        obj.setDisplayName(new StringBuilder().append(ChatColor.GOLD).append(ChatColor.BOLD).append(this.ctw.getMapHandler().currentMap).toString());
        this.scoreboard.resetScores(this.redWool);
        this.redWool = ChatColor.RED + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.redWool).setScore(1);
        this.scoreboard.resetScores(this.pinkWool);
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.pinkWool).setScore(2);
        this.scoreboard.resetScores(this.blueWool);
        this.blueWool = ChatColor.BLUE + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.blueWool).setScore(5);
        this.scoreboard.resetScores(this.cyanWool);
        this.cyanWool = ChatColor.DARK_AQUA + this.woolNotPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.cyanWool).setScore(6);
    }

    public void redWoolTaken() {
        this.woolPickedUp = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
        this.scoreboard.resetScores(this.redWool);
        this.redWool = ChatColor.RED + this.woolPickedUp + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.redWool).setScore(1);
    }

    public void pinkWoolTaken() {
        this.woolPickedUp = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
        this.scoreboard.resetScores(this.pinkWool);
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolPickedUp + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.pinkWool).setScore(2);
    }

    public void blueWoolTaken() {
        this.woolPickedUp = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
        this.scoreboard.resetScores(this.blueWool);
        this.blueWool = ChatColor.BLUE + this.woolPickedUp + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.blueWool).setScore(5);
    }

    public void cyanWoolTaken() {
        this.woolPickedUp = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPickedUp");
        this.scoreboard.resetScores(this.cyanWool);
        this.cyanWool = ChatColor.DARK_AQUA + this.woolPickedUp + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.cyanWool).setScore(6);
    }

    public void redWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.redWool);
        this.redWool = ChatColor.RED + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Red");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.redWool).setScore(1);
    }

    public void pinkWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.pinkWool);
        this.pinkWool = ChatColor.LIGHT_PURPLE + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Pink");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.pinkWool).setScore(2);
    }

    public void blueWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.blueWool);
        this.blueWool = ChatColor.BLUE + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Blue");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.blueWool).setScore(5);
    }

    public void cyanWoolPlaced() {
        this.woolPlaced = this.ctw.getLanguageHandler().getMessage("Scoreboard.WoolPlaced");
        this.scoreboard.resetScores(this.cyanWool);
        this.cyanWool = ChatColor.DARK_AQUA + this.woolPlaced + ChatColor.GRAY + " " + this.ctw.getLanguageHandler().getMessage("Words.Cyan");
        this.scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(this.cyanWool).setScore(6);
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
