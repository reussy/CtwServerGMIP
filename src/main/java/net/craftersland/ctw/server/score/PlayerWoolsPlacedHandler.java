package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerWoolsPlacedHandler {
    private final CTW ctw;
    private final Map<Player, Integer> woolsPlaced;

    public PlayerWoolsPlacedHandler(final CTW ctw) {
        this.woolsPlaced = new HashMap<Player, Integer>();
        this.ctw = ctw;
    }

    public void saveAllData() {
        final Map<Player, Integer> data = new HashMap<Player, Integer>(this.woolsPlaced);
        if (!data.isEmpty()) {
            for (final Player p : data.keySet()) {
                if (p != null && p.isOnline()) {
                    this.ctw.getDataHandler().setWoolsPlaced(p, this.woolsPlaced.get(p));
                }
            }
        }
    }

    public void saveDataToDatabasse(final Player p) {
        if (this.woolsPlaced.containsKey(p)) {
            this.ctw.getDataHandler().setWoolsPlaced(p, this.woolsPlaced.get(p));
            this.woolsPlaced.remove(p);
        }
    }

    public void loadData(final Player p) {
        if (this.ctw.getDataHandler().hasAccount(p)) {
           final int count = this.ctw.getDataHandler().getWoolsPlaced(p);
            this.woolsPlaced.put(p, count);
        } else {
            this.woolsPlaced.put(p, 0);
        }
    }

    public void addWoolPlaced(final Player p) {
        int count = this.woolsPlaced.get(p);
        this.woolsPlaced.put(p, count + 1);
    }

    public Integer getWoolsPlaced(final Player p) {
        return this.woolsPlaced.get(p);
    }
}
