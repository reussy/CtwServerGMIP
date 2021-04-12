package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamWoolsCaptured {
    private final CTW ctw;
    private final Map<TeamHandler.Teams, Integer> woolsCaptured;

    public TeamWoolsCaptured(final CTW ctw) {
        this.woolsCaptured = new HashMap<TeamHandler.Teams, Integer>();
        this.ctw = ctw;
        this.resetData();
    }

    public void resetData() {
        this.woolsCaptured.put(TeamHandler.Teams.RED, 0);
        this.woolsCaptured.put(TeamHandler.Teams.BLUE, 0);
    }

    public void addBlueCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Teams.BLUE);
        this.woolsCaptured.put(TeamHandler.Teams.BLUE, sc + 1);
    }

    public void addRedCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Teams.RED);
        this.woolsCaptured.put(TeamHandler.Teams.RED, sc + 1);
    }

    public Integer getBlueCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Teams.BLUE);
        return sc;
    }

    public Integer getRedCaptured() {
        final int sc = this.woolsCaptured.get(TeamHandler.Teams.RED);
        return sc;
    }
}
