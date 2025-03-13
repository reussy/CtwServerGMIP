package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamWoolsCaptured {
    private final Map<TeamHandler.Team, Integer> woolsCaptured;

    public TeamWoolsCaptured() {
        this.woolsCaptured = new HashMap<>();
        this.resetData();
    }

    public void resetData() {
        this.woolsCaptured.put(TeamHandler.Team.RED, 0);
        this.woolsCaptured.put(TeamHandler.Team.BLUE, 0);
    }

    public void addBlueWoolsCapture() {
        final int wools = this.woolsCaptured.get(TeamHandler.Team.BLUE);
        if (wools == 0) {
            this.woolsCaptured.put(TeamHandler.Team.RED, 0);
        }
        this.woolsCaptured.put(TeamHandler.Team.BLUE, wools + 1);
    }

    public void addRedWoolsCaptured() {
        final int wools = this.woolsCaptured.get(TeamHandler.Team.RED);
        if (wools == 0) {
            this.woolsCaptured.put(TeamHandler.Team.BLUE, 0);
        }
        this.woolsCaptured.put(TeamHandler.Team.RED, wools + 1);
    }

    public Integer getBlueWoolsCaptured() {
        return this.woolsCaptured.get(TeamHandler.Team.BLUE);
    }

    public Integer getRedWoolsCaptured() {
        return this.woolsCaptured.get(TeamHandler.Team.RED);
    }
}
