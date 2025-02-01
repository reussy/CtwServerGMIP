package net.craftersland.ctw.server.game.map;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.craftersland.ctw.server.CTW;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

public class MapConfigHandler {
    private final CTW ctw;
    public int maxPlayers;
    public String time;
    public Location spectatorSpawn;
    public Location redSpawn;
    public Location blueSpawn;
    public Location redWool;
    public Location pinkWool;
    public Location blueWool;
    public Location cyanWool;
    public Location redWonParticle;
    public Location blueWonParticle;
    public int maxHight;
    public int minHight;
    public List<CuboidSelection> protectedAreas;
    public List<CuboidSelection> redNoAccess;
    public List<CuboidSelection> blueNoAccess;
    public CuboidSelection gameArea;
    public ItemStack[] startupKit;

    public MapConfigHandler(final CTW ctw) {
        this.protectedAreas = new ArrayList<CuboidSelection>();
        this.redNoAccess = new ArrayList<CuboidSelection>();
        this.blueNoAccess = new ArrayList<CuboidSelection>();
        this.ctw = ctw;
        this.createMapConfigFolder();
    }

    public Location getSpectatorSpawn() {
        return this.spectatorSpawn;
    }

    private void createMapConfigFolder() {
        final File mapConfigFolder = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs");
        if (!mapConfigFolder.exists()) {
            mapConfigFolder.mkdir();
        }
    }

    public void reloadConfig() {
        this.protectedAreas.clear();
        this.redNoAccess.clear();
        this.blueNoAccess.clear();
        this.startupKit = null;
        this.loadConfig(this.ctw.getMapHandler().currentMap);
    }

    public void loadConfig(final String mapName) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin) this.ctw, (Runnable) new Runnable() {
            @Override
            public void run() {
                final File mapConfigFolder = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs");
                if (!mapConfigFolder.exists()) {
                    mapConfigFolder.mkdir();
                }
                final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + mapName + ".yml");
                if (!mapConfig.exists()) {
                    MapConfigHandler.this.ctw.saveResource("MapConfig.yml", false);
                    final File rawConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfig.yml");
                    try {
                        FileUtils.moveFileToDirectory(rawConfig, mapConfigFolder, false);
                        final File fileToRename = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + "MapConfig.yml");
                        fileToRename.renameTo(mapConfig);
                    } catch (Exception e) {
                        CTW.log.severe("Error on create map config function! Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
                MapConfigHandler.this.maxPlayers = cfg.getInt("MaxPlayersPerTeam");
                MapConfigHandler.this.time = cfg.getString("Time");
                final String[] spectatorSpawnString = cfg.getString("SpectatorSpawn").split("#");
                MapConfigHandler.this.spectatorSpawn = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(spectatorSpawnString[0]), Double.parseDouble(spectatorSpawnString[1]), Double.parseDouble(spectatorSpawnString[2]), Float.parseFloat(spectatorSpawnString[3]), Float.parseFloat(spectatorSpawnString[4]));
                final String[] redSpawnString = cfg.getString("RedTeam.Spawn").split("#");
                MapConfigHandler.this.redSpawn = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(redSpawnString[0]), Double.parseDouble(redSpawnString[1]), Double.parseDouble(redSpawnString[2]), Float.parseFloat(redSpawnString[3]), Float.parseFloat(redSpawnString[4]));
                final String[] blueSpawnString = cfg.getString("BlueTeam.Spawn").split("#");
                MapConfigHandler.this.blueSpawn = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(blueSpawnString[0]), Double.parseDouble(blueSpawnString[1]), Double.parseDouble(blueSpawnString[2]), Float.parseFloat(blueSpawnString[3]), Float.parseFloat(blueSpawnString[4]));
                final String[] redWoolString = cfg.getString("RedTeam.RedWool").split("#");
                MapConfigHandler.this.redWool = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(redWoolString[0]), (double) Integer.parseInt(redWoolString[1]), (double) Integer.parseInt(redWoolString[2]));
                final String[] pinkWoolString = cfg.getString("RedTeam.PinkWool").split("#");
                MapConfigHandler.this.pinkWool = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(pinkWoolString[0]), (double) Integer.parseInt(pinkWoolString[1]), (double) Integer.parseInt(pinkWoolString[2]));
                final String[] blueWoolString = cfg.getString("BlueTeam.BlueWool").split("#");
                MapConfigHandler.this.blueWool = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(blueWoolString[0]), (double) Integer.parseInt(blueWoolString[1]), (double) Integer.parseInt(blueWoolString[2]));
                final String[] cyanWoolString = cfg.getString("BlueTeam.CyanWool").split("#");
                MapConfigHandler.this.cyanWool = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(cyanWoolString[0]), (double) Integer.parseInt(cyanWoolString[1]), (double) Integer.parseInt(cyanWoolString[2]));
                final String[] redWonEffectString = cfg.getString("VictoryParticles.Red").split("#");
                MapConfigHandler.this.redWonParticle = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(redWonEffectString[0]), Double.parseDouble(redWonEffectString[1]), Double.parseDouble(redWonEffectString[2]), Float.parseFloat(redWonEffectString[3]), Float.parseFloat(redWonEffectString[4]));
                final String[] blueWonEffectString = cfg.getString("VictoryParticles.Blue").split("#");
                MapConfigHandler.this.blueWonParticle = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(blueWonEffectString[0]), Double.parseDouble(blueWonEffectString[1]), Double.parseDouble(blueWonEffectString[2]), Float.parseFloat(blueWonEffectString[3]), Float.parseFloat(blueWonEffectString[4]));
                MapConfigHandler.this.maxHight = cfg.getInt("ProtectedHight.maxHight");
                MapConfigHandler.this.minHight = cfg.getInt("ProtectedHight.minHight");
                final List<String> protectedString = (List<String>) cfg.getStringList("ProtectedAreas");
                if (!protectedString.isEmpty()) {
                    for (final String s : protectedString) {
                        final String[] data = s.split("#");
                        final Location l1 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(data[0]), (double) Integer.parseInt(data[1]), (double) Integer.parseInt(data[2]));
                        final Location l2 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(data[3]), (double) Integer.parseInt(data[4]), (double) Integer.parseInt(data[5]));
                        final CuboidSelection cs = new CuboidSelection(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, l1, l2);
                        MapConfigHandler.this.protectedAreas.add(cs);
                    }
                }
                final List<String> redNoAcc = (List<String>) cfg.getStringList("RedNoAccess");
                if (!redNoAcc.isEmpty()) {
                    for (final String s2 : redNoAcc) {
                        final String[] data2 = s2.split("#");
                        final Location l3 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(data2[0]), Double.parseDouble(data2[1]), Double.parseDouble(data2[2]));
                        final Location l4 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(data2[3]), Double.parseDouble(data2[4]), Double.parseDouble(data2[5]));
                        final CuboidSelection cs2 = new CuboidSelection(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, l3, l4);
                        MapConfigHandler.this.redNoAccess.add(cs2);
                    }
                }
                final List<String> blueNoAcc = (List<String>) cfg.getStringList("BlueNoAccess");
                if (!blueNoAcc.isEmpty()) {
                    for (final String s3 : blueNoAcc) {
                        final String[] data3 = s3.split("#");
                        final Location l5 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(data3[0]), Double.parseDouble(data3[1]), Double.parseDouble(data3[2]));
                        final Location l6 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, Double.parseDouble(data3[3]), Double.parseDouble(data3[4]), Double.parseDouble(data3[5]));
                        final CuboidSelection cs3 = new CuboidSelection(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, l5, l6);
                        MapConfigHandler.this.blueNoAccess.add(cs3);
                    }
                }
                final String[] arenaA = cfg.getString("ArenaArea").split("#");
                final Location arenaLoc1 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(arenaA[0]), (double) Integer.parseInt(arenaA[1]), (double) Integer.parseInt(arenaA[2]));
                final Location arenaLoc2 = new Location(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(arenaA[3]), (double) Integer.parseInt(arenaA[4]), (double) Integer.parseInt(arenaA[5]));
                MapConfigHandler.this.gameArea = new CuboidSelection(MapConfigHandler.this.ctw.getMapHandler().currentMapWorld, arenaLoc1, arenaLoc2);
                final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                for (int i = 0; i < 36; ++i) {
                    final ItemStack item = cfg.getItemStack("StartupKit." + i);
                    items.add(item);
                }
                MapConfigHandler.this.startupKit = items.toArray(new ItemStack[items.size()]);
                items.clear();
            }
        });
    }

    public List<String> getArea(final String path, final Location loc) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        final Set<String> areaList = new HashSet<String>(cfg.getStringList(path));
        final List<String> areas = new ArrayList<String>();
        if (!areaList.isEmpty()) {
            for (final String s : areaList) {
                final String[] data = s.split("#");
                final Location l1 = new Location(this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(data[0]), (double) Integer.parseInt(data[1]), (double) Integer.parseInt(data[2]));
                final Location l2 = new Location(this.ctw.getMapHandler().currentMapWorld, (double) Integer.parseInt(data[3]), (double) Integer.parseInt(data[4]), (double) Integer.parseInt(data[5]));
                final CuboidSelection cs = new CuboidSelection(this.ctw.getMapHandler().currentMapWorld, l1, l2);
                if (cs.contains(loc)) {
                    areas.add(s);
                }
            }
        }
        return areas;
    }

    public void removeArea(final String path, final String data) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        final Set<String> areaList = new HashSet<String>(cfg.getStringList(path));
        areaList.remove(data);
        cfg.set(path, (Object) Arrays.asList(areaList.toArray()));
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error removing region from config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addArea(final String path, final Selection sel) {
        final String data = String.valueOf(sel.getMinimumPoint().getBlockX()) + "#" + sel.getMinimumPoint().getBlockY() + "#" + sel.getMinimumPoint().getBlockZ() + "#" + sel.getMaximumPoint().getBlockX() + "#" + sel.getMaximumPoint().getBlockY() + "#" + sel.getMaximumPoint().getBlockZ();
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        final Set<String> areaList = new HashSet<String>(cfg.getStringList(path));
        areaList.add(data);
        cfg.set(path, (Object) Arrays.asList(areaList.toArray()));
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving region to config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setArenaArea(final String path, final Selection sel) {
        final String data = String.valueOf(sel.getMinimumPoint().getBlockX()) + "#" + sel.getMinimumPoint().getBlockY() + "#" + sel.getMinimumPoint().getBlockZ() + "#" + sel.getMaximumPoint().getBlockX() + "#" + sel.getMaximumPoint().getBlockY() + "#" + sel.getMaximumPoint().getBlockZ();
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set(path, (Object) data);
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving region to config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setString(final String data, final String path) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set(path, (Object) data);
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving data to config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setInteger(final int number, final String path) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set(path, (Object) number);
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving number to config! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setBlockLocation(final Location loc, final String path) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set(path, (Object) (String.valueOf(loc.getBlockX()) + "#" + loc.getBlockY() + "#" + loc.getBlockZ()));
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving block location! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setSpawnPoint(final Location loc, final String path) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set(path, (Object) (String.valueOf(loc.getX()) + "#" + loc.getY() + "#" + loc.getZ() + "#" + loc.getYaw() + "#" + loc.getPitch()));
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving spawn location! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveStartupKit(final ItemStack i, final int slot) {
        final File mapConfig = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "MapConfigs" + System.getProperty("file.separator") + this.ctw.getMapHandler().currentMap + ".yml");
        final FileConfiguration cfg = (FileConfiguration) YamlConfiguration.loadConfiguration(mapConfig);
        cfg.set("StartupKit." + slot, (Object) i);
        try {
            cfg.save(mapConfig);
        } catch (Exception e) {
            CTW.log.severe("Error saving startup kit! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
