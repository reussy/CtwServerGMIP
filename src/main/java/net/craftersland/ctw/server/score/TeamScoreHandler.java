package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamScoreHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Teams, Integer> earnedScore;

    public TeamScoreHandler(final CTW ctw) {
        this.earnedScore = new HashMap<TeamHandler.Teams, Integer>();
        this.ctw = ctw;
        this.resetScores();
    }

    public void resetScores() {
        this.earnedScore.put(TeamHandler.Teams.RED, 0);
        this.earnedScore.put(TeamHandler.Teams.BLUE, 0);
    }

    public void addBlueScore(final Integer score) {
        final int sc = this.earnedScore.get(TeamHandler.Teams.BLUE);
        this.earnedScore.put(TeamHandler.Teams.BLUE, sc + score);
    }

    public void addRedScore(final Integer score) {
        final int sc = this.earnedScore.get(TeamHandler.Teams.RED);
        this.earnedScore.put(TeamHandler.Teams.RED, sc + score);
    }

    public Integer getBlueScore() {
        final int sc = this.earnedScore.get(TeamHandler.Teams.BLUE);
        return sc;
    }

    public Integer getRedScore() {
        final int sc = this.earnedScore.get(TeamHandler.Teams.RED);
        return sc;
    }
}
