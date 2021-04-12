package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamKillsHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Teams, Integer> teamKills;

    public TeamKillsHandler(final CTW ctw) {
        this.teamKills = new HashMap<TeamHandler.Teams, Integer>();
        this.ctw = ctw;
        this.resetScores();
    }

    public void resetScores() {
        this.teamKills.put(TeamHandler.Teams.RED, 0);
        this.teamKills.put(TeamHandler.Teams.BLUE, 0);
    }

    public void addBlueKill() {
        final int sc = this.teamKills.get(TeamHandler.Teams.BLUE);
        this.teamKills.put(TeamHandler.Teams.BLUE, sc + 1);
    }

    public void addRedKill() {
        final int sc = this.teamKills.get(TeamHandler.Teams.RED);
        this.teamKills.put(TeamHandler.Teams.RED, sc + 1);
    }

    public Integer getBlueKills() {
        final int sc = this.teamKills.get(TeamHandler.Teams.BLUE);
        return sc;
    }

    public Integer getRedKills() {
        final int sc = this.teamKills.get(TeamHandler.Teams.RED);
        return sc;
    }
}
