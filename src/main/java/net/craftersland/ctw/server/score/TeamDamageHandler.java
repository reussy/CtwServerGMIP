package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeamDamageHandler {
    private final CTW ctw;
    private final Map<TeamHandler.Teams, Integer> damage;

    public TeamDamageHandler(final CTW ctw) {
        this.damage = new HashMap<TeamHandler.Teams, Integer>();
        this.ctw = ctw;
        this.resetData();
    }

    public void resetData() {
        this.damage.put(TeamHandler.Teams.RED, 0);
        this.damage.put(TeamHandler.Teams.BLUE, 0);
    }

    public void autoAddDmg(final Player deadPlayer, final Double dmg) {
        if (this.ctw.getTeamHandler().isBlueTeam(deadPlayer)) {
            this.addRedDmg(dmg.intValue());
        } else if (this.ctw.getTeamHandler().isRedTeam(deadPlayer)) {
            this.addBlueDmg(dmg.intValue());
        }
    }

    public void addBlueDmg(final Integer dmg) {
        final int sc = this.damage.get(TeamHandler.Teams.BLUE);
        this.damage.put(TeamHandler.Teams.BLUE, sc + 1);
    }

    public void addRedDmg(final Integer dmg) {
        final int sc = this.damage.get(TeamHandler.Teams.RED);
        this.damage.put(TeamHandler.Teams.RED, sc + 1);
    }

    public Integer getBlueDmg() {
        final int sc = this.damage.get(TeamHandler.Teams.BLUE);
        return sc;
    }

    public Integer getRedDmg() {
        final int sc = this.damage.get(TeamHandler.Teams.RED);
        return sc;
    }
}
