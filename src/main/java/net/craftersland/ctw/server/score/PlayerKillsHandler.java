package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerKillsHandler {
    private final CTW ctw;
    private final Map<Player, Integer> totalKills;
    private final Map<Player, Integer> bowKills;
    private final Map<Player, Integer> meleeKills;
    private final HashMap<String, Integer> kills;

    public PlayerKillsHandler(final CTW ctw) {
        this.totalKills = new HashMap<Player, Integer>();
        this.bowKills = new HashMap<Player, Integer>();
        this.meleeKills = new HashMap<Player, Integer>();
        this.kills = new HashMap<String, Integer>();
        this.ctw = ctw;
    }

    public void saveAllData() {
        final Map<Player, Integer> data = new HashMap<Player, Integer>(this.totalKills);
        if (!data.isEmpty()) {
            for (final Player p : data.keySet()) {
                if (p != null && p.isOnline()) {
                    this.ctw.getDataHandler().setKills(p, this.meleeKills.get(p), this.bowKills.get(p));
                }
            }
        }
    }

    public HashMap<String, Integer> getKills() {
        return kills;
    }

    public void orderKills() {

        List<Map.Entry<String, Integer>> top3 = kills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).collect(Collectors.toList());

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player != null) {

                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage("&8+--------------------------------------+");
                player.sendMessage(" ");

                if (top3.size() >= 2) {

                    player.sendMessage("&b       1ro Asesino &8- &7" + top3.get(0).getKey() + " &8- &e " + top3.get(0).getValue());
                    player.sendMessage("&a         2do Asesino &8- &7" + top3.get(1).getKey() + " &8- &e " + top3.get(1).getValue());
                    player.sendMessage("&d           3er Asesino &8- &7" + top3.get(2).getKey() + " &8- &e " + top3.get(2).getValue());

                } else {
                    ctw.getSendMessage().sendCenteredMessage(player, "&cNo han habido jugadores suficientes...");
                }
                player.sendMessage(" ");
                ctw.getSendMessage().sendCenteredMessage(player, "&8+--------------------------------------+");
            }
        });
    }

    public void resetKills() {

        kills.clear();

    }

    public void addKill(String name) {

        if (!kills.containsKey(name)) {

            kills.put(name, 1);

        } else {

            kills.put(name, kills.get(name) + 1);

        }
    }

    public void saveKillsToDatabasse(final Player p) {
        if (this.meleeKills.containsKey(p) && this.bowKills.containsKey(p)) {
            this.ctw.getDataHandler().setKills(p, this.meleeKills.get(p), this.bowKills.get(p));
            this.totalKills.remove(p);
            this.bowKills.remove(p);
            this.meleeKills.remove(p);
        }
    }

    public void loadKills(final Player p) {
        if (this.ctw.getDataHandler().hasAccount(p)) {
            final Integer[] kills = this.ctw.getDataHandler().getKills(p);
            this.meleeKills.put(p, kills[0]);
            this.bowKills.put(p, kills[1]);
            this.totalKills.put(p, kills[0] + kills[1]);
        } else {
            this.meleeKills.put(p, 0);
            this.bowKills.put(p, 0);
            this.totalKills.put(p, 0);
        }
    }

    public void addBowKill(final Player p) {
        final int kills = this.bowKills.get(p);
        final int tkills = this.totalKills.get(p);
        try {
            this.bowKills.put(p, kills + 1);
            this.totalKills.put(p, tkills + 1);
            this.addTeamKill(p);
        } catch (Exception e) {
            if (p != null) {
                Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
                    @Override
                    public void run() {
                        p.kickPlayer(PlayerKillsHandler.this.ctw.getLanguageHandler().getMessage("KickMessages.Error"));
                    }
                });
            }
        }
    }

    public void addMeleeKill(final Player p) {
        int kills = 0;
        if (this.meleeKills.containsKey(p)) {
            kills = this.meleeKills.get(p);
        }
        this.meleeKills.put(p, kills + 1);
        int tkills = 0;
        if (this.totalKills.containsKey(p)) {
            tkills = this.totalKills.get(p);
        }
        this.totalKills.put(p, tkills + 1);
        this.addTeamKill(p);
    }

    public int getTotalKills(final Player p) {
        final int kills = this.totalKills.get(p);
        return kills;
    }

    public int getBowKills(final Player p) {
        final int kills = this.bowKills.get(p);
        return kills;
    }

    public int getMeleeKills(final Player p) {
        final int kills = this.meleeKills.get(p);
        return kills;
    }

    private void addTeamKill(final Player p) {
        if (this.ctw.getTeamHandler().isRedTeam(p)) {
            this.ctw.getTeamKillsHandler().addRedKill();
        } else if (this.ctw.getTeamHandler().isBlueTeam(p)) {
            this.ctw.getTeamKillsHandler().addBlueKill();
        }
    }
}
