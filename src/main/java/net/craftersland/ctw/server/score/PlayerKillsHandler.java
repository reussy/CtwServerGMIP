package net.craftersland.ctw.server.score;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerKillsHandler {
    private final CTW ctw;
    private final Map<Player, Integer> totalKills;
    private final Map<Player, Integer> bowKills;
    private final Map<Player, Integer> meleeKills;

    // Match stats
    private final HashMap<String, Integer> totalKillsMatch;
    private final HashMap<String, Integer> meleeKillsMatch;
    private final HashMap<String, Integer> bowKillsMatch;


    public PlayerKillsHandler(final CTW ctw) {
        this.totalKills = new HashMap<>();
        this.bowKills = new HashMap<>();
        this.meleeKills = new HashMap<>();

        // Match stats
        this.totalKillsMatch = new HashMap<>();
        this.meleeKillsMatch = new HashMap<>();
        this.bowKillsMatch = new HashMap<>();
        this.ctw = ctw;
    }

    public void saveAllData() {
        final Map<Player, Integer> data = new HashMap<>(this.totalKills);
        if (!data.isEmpty()) {
            for (final Player p : data.keySet()) {
                if (p != null && p.isOnline()) {
                    this.ctw.getDataHandler().setKills(p, this.meleeKills.get(p), this.bowKills.get(p));
                }
            }
        }
    }

    public HashMap<String, Integer> getTotalKillsMatch() {
        return totalKillsMatch;
    }

    public void orderKills() {

        List<Map.Entry<String, Integer>> top3 = totalKillsMatch.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).collect(Collectors.toList());

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player != null) {

                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8+--------------------------------------+"));
                player.sendMessage(" ");

                try {

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b       1ro Asesino &8- &7" + top3.get(0).getKey() + " &8- &e " + top3.get(0).getValue()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a         2do Asesino &8- &7" + top3.get(1).getKey() + " &8- &e " + top3.get(1).getValue()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d           3er Asesino &8- &7" + top3.get(2).getKey() + " &8- &e " + top3.get(2).getValue()));

                } catch (IndexOutOfBoundsException e) {
                    ctw.getSendMessage().sendCenteredMessage(player, "&cNo han habido jugadores suficientes...");
                }

                player.sendMessage(" ");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8+--------------------------------------+"));
            }
        });
    }

    public void resetTotalKillsMatch() {
        totalKillsMatch.clear();
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

    public void addBowKill(final @NotNull Player p) {
        int totalKills = this.totalKills.get(p) == null ? 0 : this.totalKills.get(p);
        int bowKills = this.bowKills.get(p) == null ? 0 : this.bowKills.get(p);
        try {
            bowKills = bowKills + 1;
            totalKills = totalKills + 1;
            this.addTotalKillMatch(p);
            this.totalKills.put(p, totalKills);
            this.bowKills.put(p, bowKills);

            this.addBowKillMatch(p);
            this.addTeamKill(p);
        } catch (Exception e) {
            if (p != null) {
                Bukkit.getScheduler().runTask(this.ctw, () -> p.kickPlayer(PlayerKillsHandler.this.ctw.getLanguageHandler().getMessage("KickMessages.Error")));
            }
            this.ctw.getLogger().severe("Error adding bow kill to player: " + Objects.requireNonNull(p).getName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addMeleeKill(final Player p) {
        int meleeKills = 0;
        if (this.meleeKills.containsKey(p)) {
            meleeKills = this.meleeKills.get(p);
        }
        this.meleeKills.put(p, meleeKills + 1);
        this.addMeleeKillMatch(p);

        int tkills = 0;
        if (this.totalKills.containsKey(p)) {
            tkills = this.totalKills.get(p);
        }
        this.addTotalKillMatch(p);
        this.totalKills.put(p, tkills + 1);
        this.addTeamKill(p);
    }

    public int getTotalKills(final Player p) {
        return this.totalKills.get(p);
    }

    public int getBowKills(final Player p) {
        return this.bowKills.get(p);
    }

    public int getMeleeKills(final Player p) {
        return this.meleeKills.get(p);
    }

    private void addTeamKill(final Player p) {
        if (this.ctw.getTeamHandler().isRedTeam(p)) {
            this.ctw.getTeamKillsHandler().addRedKill();
        } else if (this.ctw.getTeamHandler().isBlueTeam(p)) {
            this.ctw.getTeamKillsHandler().addBlueKill();
        }
    }

    public void addTotalKillMatch(final @NotNull Player p) {
        final String name = p.getName();
        if (!this.totalKillsMatch.containsKey(name)) {
            this.totalKillsMatch.put(name, 1);
        } else {
            this.totalKillsMatch.put(name, this.totalKillsMatch.get(name) + 1);
        }
    }

    public void addBowKillMatch(final @NotNull Player p) {
        final String name = p.getName();
        if (!this.bowKillsMatch.containsKey(name)) {
            this.bowKillsMatch.put(name, 1);
        } else {
            this.bowKillsMatch.put(name, this.bowKillsMatch.get(name) + 1);
        }
    }

    public void addMeleeKillMatch(final @NotNull Player p) {
        final String name = p.getName();
        if (!this.meleeKillsMatch.containsKey(name)) {
            this.meleeKillsMatch.put(name, 1);
        } else {
            this.meleeKillsMatch.put(name, this.meleeKillsMatch.get(name) + 1);
        }
    }

    public int getTotalKillsMatch(final @NotNull Player p) {
        String name = p.getName();
        if (this.totalKillsMatch.containsKey(name)) {
            return this.totalKillsMatch.get(name);
        }
        return 0;
    }

    public int getBowKillsMatch(final @NotNull Player p) {
        final String name = p.getName();
        if (this.bowKillsMatch.containsKey(name)) {
            return this.bowKillsMatch.get(name);
        }
        return 0;
    }

    public int getMeleeKillsMatch(final @NotNull Player p) {
        final String name = p.getName();
        if (this.meleeKillsMatch.containsKey(name)) {
            return this.meleeKillsMatch.get(name);
        }
        return 0;
    }
}
