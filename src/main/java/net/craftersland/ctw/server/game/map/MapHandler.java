package net.craftersland.ctw.server.game.map;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.FileSystems;

public class MapHandler {
    private final CTW ctw;
    public String currentMap;
    public World currentMapWorld;
    private int mapIndex;
    private int playedMaps;
    private String mapToUnload;

    public MapHandler(final @NotNull CTW ctw) {
        this.mapIndex = 0;
        this.playedMaps = 1;
        this.ctw = ctw;
        this.currentMap = ctw.getConfigHandler().maps.get(this.mapIndex);
        this.createMapsFolder();
        this.loadFirstMap();
    }

    private void createMapsFolder() {
        final File mapsFolder = new File("Maps");
        if (!mapsFolder.exists()) {
            mapsFolder.mkdir();
        }
    }

    public int getPlayedMaps() {
        return this.playedMaps;
    }

    private void loadFirstMap() {
        final String copyMapName = "Map-" + this.currentMap;
        final File worldToCopy = new File("Maps" + FileSystems.getDefault().getSeparator() + this.currentMap);
        this.ctw.getWorldHandler().copyWorld(copyMapName, worldToCopy);
        this.ctw.getWorldHandler().loadWorld(copyMapName);
        Bukkit.getScheduler().runTaskLater(this.ctw, new Runnable() {
            @Override
            public void run() {
                MapHandler.this.currentMapWorld = Bukkit.getWorld(copyMapName);
                MapHandler.this.ctw.getMapConfigHandler().loadConfig(MapHandler.this.currentMap);
                MapHandler.this.ctw.getWoolHandler().removeWools();
                MapHandler.this.setMapTime();
                //MapHandler.this.ctw.getScoreboardHandler().startTimer();
                MapHandler.this.ctw.getGameEngine().gameStage = GameEngine.GameStages.RUNNING;
            }
        }, 20L);
    }

    private void setMapTime() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                try {
                    if (MapHandler.this.ctw.getMapConfigHandler().time.matches("day")) {
                        MapHandler.this.currentMapWorld.setTime(6000L);
                    } else if (MapHandler.this.ctw.getMapConfigHandler().time.matches("night")) {
                        MapHandler.this.currentMapWorld.setTime(18000L);
                    }
                } catch (Exception e) {
                    MapHandler.this.currentMapWorld.setTime(6000L);
                }
            }
        }, 10L);
    }

    private void nextMapIndex() {
        if (this.mapIndex >= this.ctw.getConfigHandler().maps.size() - 1) {
            this.mapIndex = 0;
        } else {
            ++this.mapIndex;
        }
        ++this.playedMaps;
    }

    public void getNextMap() {
        this.nextMapIndex();
        this.currentMap = this.ctw.getConfigHandler().maps.get(this.mapIndex);
    }

    public void loadNextMap() {
        this.mapToUnload = this.currentMapWorld.getName();
        final String copyMapName = "Map-" + this.currentMap;
        final File worldToCopy = new File("Maps" + FileSystems.getDefault().getSeparator() + this.currentMap);
        this.ctw.getWorldHandler().copyWorld(copyMapName, worldToCopy);
        Bukkit.getScheduler().runTaskLater(this.ctw, () -> MapHandler.this.ctw.getWorldHandler().loadWorld(copyMapName), 20L);
    }

    public void startNextMap() {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
            final String mapName = "Map-" + MapHandler.this.currentMap;
            MapHandler.this.currentMapWorld = Bukkit.getWorld(mapName);
            MapHandler.this.ctw.getMapConfigHandler().loadConfig(MapHandler.this.currentMap);
            MapHandler.this.setMapTime();
            MapHandler.this.ctw.getWoolHandler().resetWoolsStats();
            MapHandler.this.ctw.getNewScoreboardHandler().initializeVariables();
            //MapHandler.this.ctw.getScoreboardHandler().startTimer();
            MapHandler.this.ctw.getPlayerHandler().respawnAllPlayers();
            MapHandler.this.ctw.getWoolHandler().removeWools();
            MapHandler.this.ctw.getTeamScoreHandler().resetScores();
            MapHandler.this.ctw.getTeamKillsHandler().resetScores();
            MapHandler.this.ctw.getTeamWoolsCaptured().resetData();
            MapHandler.this.ctw.getTeamDamageHandler().resetData();
            Bukkit.getScheduler().runTaskLater(MapHandler.this.ctw, () -> {
                if (MapHandler.this.mapToUnload != null) {
                    MapHandler.this.ctw.getWorldHandler().unloadWorld(MapHandler.this.mapToUnload);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(MapHandler.this.ctw, () -> MapHandler.this.ctw.getWorldHandler().deleteWorld(MapHandler.this.mapToUnload), 20L);
                }
            }, 100L);
            for (NPC npc : CitizensAPI.getNPCRegistry()){
                npc.despawn();
                npc.spawn(npc.getStoredLocation());
            }
        });
    }
}
