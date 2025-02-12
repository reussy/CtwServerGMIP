package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WoolHandler {
    private final CTW ctw;
    private final Map<Wools, Boolean> woolsTaken;
    private final Map<Wools, Boolean> woolsPlaced;
    private final Map<Wools, Player> woolsPlacedByPlayer;
    private final List<Player> redTakenList;
    private final List<Player> pinkTakenList;
    private final List<Player> blueTakenList;
    private final List<Player> cyanTakenList;

    public WoolHandler(final CTW ctw) {
        this.woolsTaken = new HashMap<>();
        this.woolsPlaced = new HashMap<>();
        this.woolsPlacedByPlayer = new HashMap<>();
        this.redTakenList = new ArrayList<>();
        this.pinkTakenList = new ArrayList<>();
        this.blueTakenList = new ArrayList<>();
        this.cyanTakenList = new ArrayList<>();
        this.ctw = ctw;
        this.resetWoolsStats();
    }

    public void resetWoolsStats() {
        this.woolsTaken.put(Wools.RED, false);
        this.woolsTaken.put(Wools.PINK, false);
        this.woolsTaken.put(Wools.BLUE, false);
        this.woolsTaken.put(Wools.CYAN, false);
        this.woolsPlaced.put(Wools.RED, false);
        this.woolsPlaced.put(Wools.PINK, false);
        this.woolsPlaced.put(Wools.BLUE, false);
        this.woolsPlaced.put(Wools.CYAN, false);
        this.redTakenList.clear();
        this.pinkTakenList.clear();
        this.blueTakenList.clear();
        this.cyanTakenList.clear();
        this.woolsPlacedByPlayer.put(Wools.RED, null);
        this.woolsPlacedByPlayer.put(Wools.PINK, null);
        this.woolsPlacedByPlayer.put(Wools.BLUE, null);
        this.woolsPlacedByPlayer.put(Wools.CYAN, null);
    }

    public void removeWools() {
        Bukkit.getScheduler().runTaskLater(this.ctw, () -> {
            Block redWool = WoolHandler.this.ctw.getMapHandler().currentMapWorld.getBlockAt(WoolHandler.this.ctw.getMapConfigHandler().redWool);
            Block pinkWool = WoolHandler.this.ctw.getMapHandler().currentMapWorld.getBlockAt(WoolHandler.this.ctw.getMapConfigHandler().pinkWool);
            Block blueWool = WoolHandler.this.ctw.getMapHandler().currentMapWorld.getBlockAt(WoolHandler.this.ctw.getMapConfigHandler().blueWool);
            Block cyanWool = WoolHandler.this.ctw.getMapHandler().currentMapWorld.getBlockAt(WoolHandler.this.ctw.getMapConfigHandler().cyanWool);
            if (redWool != null) {
                redWool.setType(Material.AIR);
            }

            if (pinkWool != null) {
                pinkWool.setType(Material.AIR);
            }

            if (blueWool != null) {
                blueWool.setType(Material.AIR);
            }

            if (cyanWool != null) {
                cyanWool.setType(Material.AIR);
            }
        }, 10L);
    }

    public void setRedTaken() {
        this.woolsTaken.put(Wools.RED, true);
    }

    public void setPinkTaken() {
        this.woolsTaken.put(Wools.PINK, true);
    }

    public void setBlueTaken() {
        this.woolsTaken.put(Wools.BLUE, true);
    }

    public void setCyanTaken() {
        this.woolsTaken.put(Wools.CYAN, true);
    }

    public boolean isRedTaken() {
        return this.woolsTaken.get(Wools.RED);
    }

    public List<Player> listPlayersred() {

        return redTakenList;
    }

    public Player listPlayerRed() {

        return redTakenList.get(redTakenList.size() - 1);
    }

    public List<Player> listPlayerspink() {

        return pinkTakenList;
    }

    public List<Player> listPlayerscyan() {

        return cyanTakenList;
    }

    public List<Player> listPlayersblue() {

        return blueTakenList;
    }

    public boolean isPinkTaken() {
        return this.woolsTaken.get(Wools.PINK);
    }

    public boolean isBlueTaken() {
        return this.woolsTaken.get(Wools.BLUE);
    }

    public boolean isCyanTaken() {
        return this.woolsTaken.get(Wools.CYAN);
    }

    public boolean isRedPlaced() {
        return this.woolsPlaced.get(Wools.RED);
    }

    public void setRedPlaced(final Player p) {
        this.woolsPlaced.put(Wools.RED, true);
        this.woolsPlacedByPlayer.put(Wools.RED, p);
    }

    public boolean isPinkPlaced() {
        return this.woolsPlaced.get(Wools.PINK);
    }

    public void setPinkPlaced(final Player p) {
        this.woolsPlaced.put(Wools.PINK, true);
        this.woolsPlacedByPlayer.put(Wools.PINK, p);
    }

    public boolean isBluePlaced() {
        return this.woolsPlaced.get(Wools.BLUE);
    }

    public void setBluePlaced(final Player p) {
        this.woolsPlaced.put(Wools.BLUE, true);
        this.woolsPlacedByPlayer.put(Wools.BLUE, p);
    }

    public boolean isCyanPlaced() {
        return this.woolsPlaced.get(Wools.CYAN);
    }

    public void setCyanPlaced(final Player p) {
        this.woolsPlaced.put(Wools.CYAN, true);
        this.woolsPlacedByPlayer.put(Wools.CYAN, p);
    }

    public void addRedTakenByPlayer(final Player p) {
        this.redTakenList.add(p);
    }

    public void removeRedTakenByPlayer(final Player p) {
        this.redTakenList.remove(p);
    }

    public boolean hadRedTakenByPlayer(final Player p) {
        return this.redTakenList.contains(p);
    }

    public void addPinkTakenByPlayer(final Player p) {
        this.pinkTakenList.add(p);
    }

    public void removePinkTakenByPlayer(final Player p) {
        this.pinkTakenList.remove(p);
    }

    public boolean hadPinkTakenByPlayer(final Player p) {
        return this.pinkTakenList.contains(p);
    }

    public void addBlueTakenByPlayer(final Player p) {
        this.blueTakenList.add(p);
    }

    public void removeBlueTakenByPlayer(final Player p) {
        this.blueTakenList.remove(p);
    }

    public boolean hadBlueTakenByPlayer(final Player p) {
        return this.blueTakenList.contains(p);
    }

    public void addCyanTakenByPlayer(final Player p) {
        this.cyanTakenList.add(p);
    }

    public void removeCyanTakenByPlayer(final Player p) {
        this.cyanTakenList.remove(p);
    }

    public boolean hadCyanTakenByPlayer(final Player p) {
        return this.cyanTakenList.contains(p);
    }

    public Player getWhoPlacedRedWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wools.RED);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedPinkWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wools.PINK);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedBlueWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wools.BLUE);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedCyanWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wools.CYAN);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public enum Wools {
        RED("RED", 0),
        PINK("PINK", 1),
        BLUE("BLUE", 2),
        CYAN("CYAN", 3);

        Wools(final String s, final int n) {
        }
    }
}
