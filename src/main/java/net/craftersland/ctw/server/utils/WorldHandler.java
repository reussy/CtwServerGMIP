package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.apache.commons.io.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldHandler {
    private final CTW ctw;
    private final List<World> loadedWorlds;

    public WorldHandler(final CTW ctw) {
        this.loadedWorlds = new ArrayList<World>();
        this.ctw = ctw;
    }

    public void loadWorld(final String worldName) {
        try {
            CTW.log.info("Loading world: " + worldName);
            WorldCreator worldCreator = new WorldCreator(worldName);


            //Bukkit.createWorld(WorldCreator.name(worldName));
            worldCreator.type(WorldType.FLAT);
            worldCreator.generatorSettings("2;0;1;");
            worldCreator.createWorld();
            final World w = Bukkit.getWorld(worldName);
            this.applyWorldSettings(w);
            this.removeEntitys(w);
            this.loadedWorlds.add(w);
        } catch (Exception e) {
            CTW.log.info("Failed to load world " + worldName + " .Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unloadWorld(final String worldName) {
        final World w = Bukkit.getWorld(worldName);
        if (w != null) {
            final List<Player> players = new ArrayList<Player>(w.getPlayers());
            if (!players.isEmpty()) {
                for (final Player p : players) {
                    if (p != null) {
                        if (this.ctw.isDisabling) {
                            if (!p.isOnline()) {
                                continue;
                            }
                            p.kickPlayer(Bukkit.getServer().getShutdownMessage());
                        } else {
                            if (!p.isOnline()) {
                                continue;
                            }
                            p.kickPlayer(ChatColor.RED + "Unloading world...");
                        }
                    }
                }
            }
            CTW.log.info("Unloading world: " + worldName);
            Bukkit.unloadWorld(w, false);
            this.loadedWorlds.remove(w);
        }
    }

    public void deleteWorld(final String worldName) {
        final File worldFolder = new File(worldName);
        if (worldFolder.exists()) {
            try {
                CTW.log.info("Deleting world: " + worldName);
                FileUtils.deleteDirectory(worldFolder);
            } catch (Exception e) {
                CTW.log.warning("Failed to delete world folder: " + worldName + " .Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void copyWorld(final String worldName, final File worldToCopy) {
        final File worldFolder = new File(worldName);
        if (worldFolder.exists()) {
            this.deleteWorld(worldName);
        }
        try {
            FileUtils.copyDirectory(worldToCopy, worldFolder);
        } catch (Exception e) {
            CTW.log.warning("Failed to copy world folder: " + worldName + " .Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unloadWorldsOnShutDown() {
        final List<World> worlds = new ArrayList<World>(this.loadedWorlds);
        if (!worlds.isEmpty()) {
            for (final World w : worlds) {
                if (w != null) {
                    this.unloadWorld(w.getName());
                    this.deleteWorld(w.getName());
                }
            }
        }
    }

    private void applyWorldSettings(final World w) {
        w.setAnimalSpawnLimit(0);
        w.setAutoSave(false);
        w.setDifficulty(Difficulty.EASY);
        w.setKeepSpawnInMemory(true);
        w.setMonsterSpawnLimit(0);
        w.setPVP(true);
        w.setWaterAnimalSpawnLimit(0);
        w.setStorm(false);
        w.setThundering(false);
        w.setWeatherDuration(Integer.MAX_VALUE);
        w.setTime(1000L);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setGameRuleValue("doMobSpawning", "false");
        w.setGameRuleValue("mobGriefing", "false");
        w.setGameRuleValue("showDeathMessages", "true");
    }

    private void removeEntitys(final World w) {
        final List<Entity> entities = new ArrayList<Entity>(w.getEntities());
        for (final Entity e : entities) {
            if (e.getType() != EntityType.ARMOR_STAND && e.getType() != EntityType.ITEM_FRAME && e.getType() != EntityType.LEASH_HITCH && e.getType() != EntityType.PAINTING) {
                e.remove();
            }
        }
    }
}
