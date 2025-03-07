package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamWoolsCaptured {
    private final CTW ctw;
    private final Map<TeamHandler.Team, Integer> woolsCaptured;

    public TeamWoolsCaptured(final CTW ctw) {
        this.woolsCaptured = new HashMap<TeamHandler.Team, Integer>();
        this.ctw = ctw;
        this.resetData();
    }

    public void resetData() {
        this.woolsCaptured.put(TeamHandler.Team.RED, 0);
        this.woolsCaptured.put(TeamHandler.Team.BLUE, 0);
    }

    public void addBlueCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Team.BLUE);
        this.woolsCaptured.put(TeamHandler.Team.BLUE, sc + 1);
    }

    public void addRedCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Team.RED);
        this.woolsCaptured.put(TeamHandler.Team.RED, sc + 1);
    }

    public Integer getBlueCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Team.BLUE);
        return sc;
    }

    public Integer getRedCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Team.RED);
        return sc;
    }
}
