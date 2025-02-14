package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreHandler {
    private final CTW ctw;
    private final Map<Player, Integer> score;
    private final Map<Player, String> effect;

    public PlayerScoreHandler(final CTW ctw) {
        this.score = new HashMap<>();
        this.effect = new HashMap<>();
        this.ctw = ctw;
    }

    public void setScore(final Player p, final Integer sc) {
        this.score.put(p, sc);
    }

    public void addScore(final Player p, final Integer sc) {
        if (!this.score.containsKey(p)) {
            this.score.put(p, sc);
            this.addTeamScore(p, sc);
        } else {
            int currentScore = this.score.getOrDefault(p, 0);
            this.score.put(p, currentScore + sc);
            this.addTeamScore(p, sc);
        }
    }

    public void takeScore(final Player p, final Integer sc) {
        if (!this.score.containsKey(p)) {
            this.score.put(p, sc);
        } else {
            int currentScore = this.score.getOrDefault(p, 0);
            this.score.put(p, currentScore - sc);
        }
    }

    public void removeScore(final Player p) {
        this.score.remove(p);
    }

    public String getEffect(Player p) {
        return this.effect.get(p);
    }

    public void removeEffect(Player p) {
        this.effect.remove(p);
    }

    public Integer getScore(final Player p) {
        try {
            return this.score.get(p);
        } catch (Exception e) {
            if (p != null) {
                Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
                    @Override
                    public void run() {
                        p.kickPlayer(PlayerScoreHandler.this.ctw.getLanguageHandler().getMessage("KickMessages.Error"));
                    }
                });
            }
            return 0;
        }
    }

    public Map<Player, Integer> getOnlineScores(final Player p) {
        final Map<Player, Integer> scoresCopy = new HashMap<Player, Integer>(this.score);
        return scoresCopy;
    }

    public void loadInitialScore(final Player p) {
        if (this.ctw.getDataHandler().hasAccount(p)) {
            final int sc = this.ctw.getDataHandler().getScore(p);
            this.score.put(p, sc);
        } else {
            this.score.put(p, 0);
        }
    }

    public void setEffect(final Player p) {
        if (this.ctw.getDataHandler().hasAccount(p)) {
            final String sc = this.ctw.getDataHandler().getEffect(p);
            this.effect.put(p, sc);
        }
    }

    public void saveScoreToDatabase(final Player p) {
        if (this.score.containsKey(p)) {
            final int sc = this.score.get(p);
            this.ctw.getDataHandler().setScore(p, sc);
            this.score.remove(p);
        }
    }

    public void saveAllScores() {
        final Map<Player, Integer> scoresCopy = new HashMap<Player, Integer>(this.score);
        for (final Player p : scoresCopy.keySet()) {
            if (p != null && p.isOnline()) {
                final int sc = scoresCopy.get(p);
                this.ctw.getDataHandler().setScore(p, sc);
            }
        }
    }

    private void addTeamScore(final Player p, final Integer sc) {
        if (this.ctw.getTeamHandler().isRedTeam(p)) {
            this.ctw.getTeamScoreHandler().addRedScore(sc);
        } else if (this.ctw.getTeamHandler().isBlueTeam(p)) {
            this.ctw.getTeamScoreHandler().addBlueScore(sc);
        }
    }
}
