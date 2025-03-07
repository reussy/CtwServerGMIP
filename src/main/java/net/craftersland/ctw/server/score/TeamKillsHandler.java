package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamKillsHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Team, Integer> teamKills;

    public TeamKillsHandler(final CTW ctw) {
        this.teamKills = new HashMap<TeamHandler.Team, Integer>();
        this.ctw = ctw;
        this.resetScores();
    }

    public void resetScores() {
        this.teamKills.put(TeamHandler.Team.RED, 0);
        this.teamKills.put(TeamHandler.Team.BLUE, 0);
    }

    public void addBlueKill() {
        final int sc = this.teamKills.get(TeamHandler.Team.BLUE);
        this.teamKills.put(TeamHandler.Team.BLUE, sc + 1);
    }

    public void addRedKill() {
        final int sc = this.teamKills.get(TeamHandler.Team.RED);
        this.teamKills.put(TeamHandler.Team.RED, sc + 1);
    }

    public Integer getBlueKills() {
        final int sc = this.teamKills.get(TeamHandler.Team.BLUE);
        return sc;
    }

    public Integer getRedKills() {
        final int sc = this.teamKills.get(TeamHandler.Team.RED);
        return sc;
    }
}
