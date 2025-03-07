package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamVictoryHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Team, Integer> victoryStats;

    public TeamVictoryHandler(final CTW ctw) {
        this.victoryStats = new HashMap<TeamHandler.Team, Integer>();
        this.ctw = ctw;
        this.setInitialStats();
    }

    public void setInitialStats() {
        this.victoryStats.put(TeamHandler.Team.RED, 0);
        this.victoryStats.put(TeamHandler.Team.BLUE, 0);
    }

    public void addRedVictoryPoint() {
        final int count = this.victoryStats.get(TeamHandler.Team.RED);
        this.victoryStats.put(TeamHandler.Team.RED, count + 1);
    }

    public void addBlueVictoryPoint() {
        final int count = this.victoryStats.get(TeamHandler.Team.BLUE);
        this.victoryStats.put(TeamHandler.Team.BLUE, count + 1);
    }

    public Integer getRedVictoryScore() {
        final int count = this.victoryStats.get(TeamHandler.Team.RED);
        return count;
    }

    public Integer getBlueVictoryScore() {
        final int count = this.victoryStats.get(TeamHandler.Team.BLUE);
        return count;
    }
}
