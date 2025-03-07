package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamDamageHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Team, Integer> damage;

    public TeamDamageHandler(final CTW ctw) {
        this.damage = new HashMap<TeamHandler.Team, Integer>();
        this.ctw = ctw;
        this.resetData();
    }

    public void resetData() {
        this.damage.put(TeamHandler.Team.RED, 0);
        this.damage.put(TeamHandler.Team.BLUE, 0);
    }

    public void autoAddDmg(final Player deadPlayer, final Double dmg) {
        if (this.ctw.getTeamHandler().isBlueTeam(deadPlayer)) {
            this.addRedDmg(dmg.intValue());
        } else if (this.ctw.getTeamHandler().isRedTeam(deadPlayer)) {
            this.addBlueDmg(dmg.intValue());
        }
    }

    public void addBlueDmg(final Integer dmg) {
        final int sc = this.damage.get(TeamHandler.Team.BLUE);
        this.damage.put(TeamHandler.Team.BLUE, sc + 1);
    }

    public void addRedDmg(final Integer dmg) {
        final int sc = this.damage.get(TeamHandler.Team.RED);
        this.damage.put(TeamHandler.Team.RED, sc + 1);
    }

    public Integer getBlueDmg() {
        final int sc = this.damage.get(TeamHandler.Team.BLUE);
        return sc;
    }

    public Integer getRedDmg() {
        final int sc = this.damage.get(TeamHandler.Team.RED);
        return sc;
    }
}
