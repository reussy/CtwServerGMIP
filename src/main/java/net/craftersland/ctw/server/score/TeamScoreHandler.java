package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;

import java.util.HashMap;
import java.util.Map;

public class TeamScoreHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Team, Integer> earnedScore;

    public TeamScoreHandler(final CTW ctw) {
        this.earnedScore = new HashMap<TeamHandler.Team, Integer>();
        this.ctw = ctw;
        this.resetScores();
    }

    public void resetScores() {
        this.earnedScore.put(TeamHandler.Team.RED, 0);
        this.earnedScore.put(TeamHandler.Team.BLUE, 0);
    }

    public void addBlueScore(final Integer score) {
        final int sc = this.earnedScore.get(TeamHandler.Team.BLUE);
        this.earnedScore.put(TeamHandler.Team.BLUE, sc + score);
    }

    public void addRedScore(final Integer score) {
        final int sc = this.earnedScore.get(TeamHandler.Team.RED);
        this.earnedScore.put(TeamHandler.Team.RED, sc + score);
    }

    public Integer getBlueScore() {
        final int sc = this.earnedScore.get(TeamHandler.Team.BLUE);
        return sc;
    }

    public Integer getRedScore() {
        final int sc = this.earnedScore.get(TeamHandler.Team.RED);
        return sc;
    }
}
