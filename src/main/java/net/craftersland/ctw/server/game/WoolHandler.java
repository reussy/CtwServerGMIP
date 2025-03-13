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
    private boolean isRedTaken, isPinkTaken, isBlueTaken, isCyanTaken, isRedPlaced, isPinkPlaced, isBluePlaced, isCyanPlaced;
    private final Map<Wool, Boolean> woolsTaken;
    private final Map<Wool, Boolean> woolsPlaced;
    private final Map<Wool, Player> woolsPlacedByPlayer;
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
        this.woolsTaken.put(Wool.RED, false);
        this.woolsTaken.put(Wool.PINK, false);
        this.woolsTaken.put(Wool.BLUE, false);
        this.woolsTaken.put(Wool.CYAN, false);
        this.woolsPlaced.put(Wool.RED, false);
        this.woolsPlaced.put(Wool.PINK, false);
        this.woolsPlaced.put(Wool.BLUE, false);
        this.woolsPlaced.put(Wool.CYAN, false);
        this.redTakenList.clear();
        this.pinkTakenList.clear();
        this.blueTakenList.clear();
        this.cyanTakenList.clear();
        this.woolsPlacedByPlayer.put(Wool.RED, null);
        this.woolsPlacedByPlayer.put(Wool.PINK, null);
        this.woolsPlacedByPlayer.put(Wool.BLUE, null);
        this.woolsPlacedByPlayer.put(Wool.CYAN, null);
    }

    public void removeWoolsPlaced() {
        Bukkit.getScheduler().runTaskLater(this.ctw, () -> {
            Block redWool = this.ctw.getMapHandler().currentMapWorld.getBlockAt(this.ctw.getMapConfigHandler().redWool);
            Block pinkWool = this.ctw.getMapHandler().currentMapWorld.getBlockAt(this.ctw.getMapConfigHandler().pinkWool);
            Block blueWool = this.ctw.getMapHandler().currentMapWorld.getBlockAt(this.ctw.getMapConfigHandler().blueWool);
            Block cyanWool = this.ctw.getMapHandler().currentMapWorld.getBlockAt(this.ctw.getMapConfigHandler().cyanWool);
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

    public boolean isWoolTaken(final Wool wool) {
        if (wool == Wool.RED){
            return this.isRedTaken;
        } else if (wool == Wool.PINK){
            return this.isPinkTaken;
        } else if (wool == Wool.BLUE){
            return this.isBlueTaken;
        } else if (wool == Wool.CYAN){
            return this.isCyanTaken;
        } else {
            return false;
        }
    }

    public boolean isWoolPlaced(final Wool wool) {
        if (wool == Wool.RED){
            return this.isRedPlaced;
        } else if (wool == Wool.PINK){
            return this.isPinkPlaced;
        } else if (wool == Wool.BLUE){
            return this.isBluePlaced;
        } else if (wool == Wool.CYAN){
            return this.isCyanPlaced;
        } else {
            return false;
        }
    }

    public void setWoolTaken(final Wool wool) {
        if (wool == Wool.RED){
            this.isRedTaken = true;
        } else if (wool == Wool.PINK){
            this.isPinkTaken = true;
        } else if (wool == Wool.BLUE){
            this.isBlueTaken = true;
        } else if (wool == Wool.CYAN){
            this.isCyanTaken = true;
        }
    }

    public void setWoolPlaced(final Wool wool) {
        if (wool == Wool.RED){
            this.isRedPlaced = true;
        } else if (wool == Wool.PINK){
            this.isPinkPlaced = true;
        } else if (wool == Wool.BLUE){
            this.isBluePlaced = true;
        } else if (wool == Wool.CYAN){
            this.isCyanPlaced = true;
        }
    }

    public void setRedTaken() {
        this.woolsTaken.put(Wool.RED, true);
    }

    public void setPinkTaken() {
        this.woolsTaken.put(Wool.PINK, true);
    }

    public void setBlueTaken() {
        this.woolsTaken.put(Wool.BLUE, true);
    }

    public void setCyanTaken() {
        this.woolsTaken.put(Wool.CYAN, true);
    }

    public boolean isRedTaken() {
        return this.woolsTaken.get(Wool.RED);
    }

    public List<Player> getRedPlayers() {
        return redTakenList;
    }

    public List<Player> getPinkPlayers() {
        return pinkTakenList;
    }

    public List<Player> getCyanPlayers() {
        return cyanTakenList;
    }

    public List<Player> getBluePlayers() {

        return blueTakenList;
    }

    public boolean isPinkTaken() {
        return this.woolsTaken.get(Wool.PINK);
    }

    public boolean isBlueTaken() {
        return this.woolsTaken.get(Wool.BLUE);
    }

    public boolean isCyanTaken() {
        return this.woolsTaken.get(Wool.CYAN);
    }

    public boolean isRedPlaced() {
        return this.woolsPlaced.get(Wool.RED);
    }

    public void setRedPlaced(final Player p) {
        this.woolsPlaced.put(Wool.RED, true);
        this.woolsPlacedByPlayer.put(Wool.RED, p);
    }

    public boolean isPinkPlaced() {
        return this.woolsPlaced.get(Wool.PINK);
    }

    public void setPinkPlaced(final Player p) {
        this.woolsPlaced.put(Wool.PINK, true);
        this.woolsPlacedByPlayer.put(Wool.PINK, p);
    }

    public boolean isBluePlaced() {
        return this.woolsPlaced.get(Wool.BLUE);
    }

    public void setBluePlaced(final Player p) {
        this.woolsPlaced.put(Wool.BLUE, true);
        this.woolsPlacedByPlayer.put(Wool.BLUE, p);
    }

    public boolean isCyanPlaced() {
        return this.woolsPlaced.get(Wool.CYAN);
    }

    public void setCyanPlaced(final Player p) {
        this.woolsPlaced.put(Wool.CYAN, true);
        this.woolsPlacedByPlayer.put(Wool.CYAN, p);
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
        final Player p = this.woolsPlacedByPlayer.get(Wool.RED);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedPinkWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wool.PINK);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedBlueWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wool.BLUE);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public Player getWhoPlacedCyanWool() {
        final Player p = this.woolsPlacedByPlayer.get(Wool.CYAN);
        if (p != null && p.isOnline()) {
            return p;
        }
        return null;
    }

    public enum Wool {
        RED("RED", 0),
        PINK("PINK", 1),
        BLUE("BLUE", 2),
        CYAN("CYAN", 3);

        Wool(final String s, final int n) {
        }
    }
}
