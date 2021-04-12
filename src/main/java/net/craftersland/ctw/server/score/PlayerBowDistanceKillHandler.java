package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerBowDistanceKillHandler {
    private final CTW ctw;
    private final Map<Player, Integer> distanceRecord;

    public PlayerBowDistanceKillHandler(final CTW ctw) {
        this.distanceRecord = new HashMap<Player, Integer>();
        this.ctw = ctw;
    }

    public void saveAllData() {
        final Map<Player, Integer> data = new HashMap<Player, Integer>(this.distanceRecord);
        if (!data.isEmpty()) {
            for (final Player p : data.keySet()) {
                if (p != null && p.isOnline()) {
                    this.ctw.getDataHandler().setWoolsPlaced(p, this.distanceRecord.get(p));
                }
            }
        }
    }

    public void saveDataToDatabasse(final Player p) {
        if (this.distanceRecord.containsKey(p)) {
            this.ctw.getDataHandler().setBowDistanceKill(p, this.distanceRecord.get(p));
            this.distanceRecord.remove(p);
        }
    }

    public void loadData(final Player p) {
        if (this.ctw.getDataHandler().hasAccount(p)) {
            final int count = this.ctw.getDataHandler().getBowDistanceKill(p);
            this.distanceRecord.put(p, count);
        } else {
            this.distanceRecord.put(p, 0);
        }
    }

    public void setDistanceKill(final Player p, final Integer distance) {
        final int count = this.distanceRecord.get(p);
        if (distance > count) {
            this.distanceRecord.put(p, distance);
        }
    }

    public Integer getDistanceKill(final Player p) {
        final int count = this.distanceRecord.get(p);
        return count;
    }
}
