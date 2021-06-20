package net.craftersland.ctw.server.commands;


// TODO Dependencia WE

import com.sk89q.worldedit.bukkit.selections.Selection;
import net.craftersland.ctw.server.CTW;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Setup implements CommandExecutor {
    private final CTW ctw;

    public Setup(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
        if (!cmdlabel.equalsIgnoreCase("setup")) {
            return false;
        }
        if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (p.hasPermission("CTW.admin")) {
                if (args.length == 0) {
                    this.sendSetupInfo(p);
                } else if (args.length == 1) {
                    if (args[0].matches("setspectatorspawn")) {
                        this.setSpawnPoint("spectator", p);
                    } else if (args[0].matches("setredspawn")) {
                        this.setSpawnPoint("red", p);
                    } else if (args[0].matches("setbluespawn")) {
                        this.setSpawnPoint("blue", p);
                    } else if (args[0].matches("setredparticles")) {
                        this.setSpawnPoint("redparticles", p);
                    } else if (args[0].matches("setblueparticles")) {
                        this.setSpawnPoint("blueparticles", p);
                    } else if (args[0].matches("setredwool")) {
                        this.setBlockLoc("redwool", p);
                    } else if (args[0].matches("setpinkwool")) {
                        this.setBlockLoc("pinkwool", p);
                    } else if (args[0].matches("setbluewool")) {
                        this.setBlockLoc("bluewool", p);
                    } else if (args[0].matches("setcyanwool")) {
                        this.setBlockLoc("cyanwool", p);
                    } else if (args[0].matches("setteammaxplayers")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetTeamMaxPlayersUsage"));
                    } else if (args[0].matches("setmaxhight")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetMaxHightUsage"));
                    } else if (args[0].matches("setminhight")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetMinHightUsage"));
                    } else if (args[0].matches("settime")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetTimeUsage"));
                    } else if (args[0].matches("setarenaarea")) {
                            Selection sel = this.ctw.getWorldEditPlugin().getSelection(p);
                        if (sel != null) {
                            this.ctw.getMapConfigHandler().setArenaArea("ArenaArea", sel);
                            this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupAreaSet"));
                        } else {
                            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSelectionError"));
                        }
                    } else if (args[0].matches("setprotectedarea")) {
                        this.addArea("protected", p);
                    } else if (args[0].matches("setrednoaccess")) {
                        this.addArea("rednoaccess", p);
                    } else if (args[0].matches("setbluenoaccess")) {
                        this.addArea("bluenoaccess", p);
                    } else if (args[0].matches("areainfo")) {
                        this.getAreas(p);
                    } else if (args[0].matches("delprotectedarea")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.DelProtectedAreaUsage"));
                    } else if (args[0].matches("delrednoaccess")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.DelRedNoAccessUsage"));
                    } else if (args[0].matches("delbluenoaccess")) {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.DelBlueNoAccessUsage"));
                    } else if (args[0].matches("setstartupkit")) {
                        this.saveStartKit(p);
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupKitSet"));
                    } else if (args[0].matches("setredwoolspawner")) {
                        this.setItemDropMobSpawner(p, 14);
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSpawnerSet"));
                    } else if (args[0].matches("setpinkwoolspawner")) {
                        this.setItemDropMobSpawner(p, 6);
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSpawnerSet"));
                    } else if (args[0].matches("setbluewoolspawner")) {
                        this.setItemDropMobSpawner(p, 11);
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSpawnerSet"));
                    } else if (args[0].matches("setcyanwoolspawner")) {
                        this.setItemDropMobSpawner(p, 9);
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSpawnerSet"));
                    } else if (args[0].matches("save")) {
                        this.save();
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSave"));
                    } else if (args[0].matches("reload")) {
                        this.ctw.getConfigHandler().loadConfig();
                        this.ctw.getLanguageHandler().loadLangFile();
                        this.ctw.getKitConfigHandler().loadConfig();
                        this.ctw.getDisableRecipe().reloadDisabledRecipes();
                        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.ReloadCmd"));
                    } else {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupCmdNotFound"));
                    }
                } else if (args.length == 2) {
                    if (args[0].matches("setteammaxplayers")) {
                        this.setNumberToConfig(args[1], p, "MaxPlayersPerTeam");
                    } else if (args[0].matches("setmaxhight")) {
                        this.setNumberToConfig(args[1], p, "ProtectedHight.maxHight");
                    } else if (args[0].matches("setminhight")) {
                        this.setNumberToConfig(args[1], p, "ProtectedHight.minHight");
                    } else if (args[0].matches("settime")) {
                        this.settime(args[1], p);
                    } else if (args[0].matches("delprotectedarea")) {
                        this.removeArea(args[1], p, "ProtectedAreas");
                    } else if (args[0].matches("delrednoaccess")) {
                        this.removeArea(args[1], p, "RedNoAccess");
                    } else if (args[0].matches("delbluenoaccess")) {
                        this.removeArea(args[1], p, "BlueNoAccess");
                    } else {
                        this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupCmdNotFound"));
                    }
                } else {
                    this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupCmdNotFound"));
                }
            } else {
                this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.NoSetupPermission"));
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Only a player can run the setup commands!");
        return true;
    }

    private void save() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.deleteDirectory(new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"));
                    FileUtils.deleteDirectory(new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"));
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"));
                        FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"));
                        FileUtils.copyFile(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"));
                    } catch (Exception e2) {
                        CTW.log.warning("Error saving map! Error: " + e2.getMessage());
                        e2.printStackTrace();
                    }
                    return;
                } finally {
                    try {
                        FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"));
                        FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"));
                        FileUtils.copyFile(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"));
                    } catch (Exception e2) {
                        CTW.log.warning("Error saving map! Error: " + e2.getMessage());
                        e2.printStackTrace();
                    }
                }
                try {
                    FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "data"));
                    FileUtils.copyDirectory(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "region"));
                    FileUtils.copyFile(new File("Map-" + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"), new File("Maps" + System.getProperty("file.separator") + Setup.this.ctw.getMapHandler().currentMap + System.getProperty("file.separator") + "level.dat"));
                } catch (Exception e2) {
                    CTW.log.warning("Error saving map! Error: " + e2.getMessage());
                    e2.printStackTrace();
                }
            }
        }, 20L);
        this.ctw.getMapConfigHandler().reloadConfig();
    }

    private void setItemDropMobSpawner(final Player p, final int woolValue) {
        final Block block = p.getTargetBlock((Set<Material>) null, 6);
        block.setType(Material.COMMAND);
        final CommandBlock cb = (CommandBlock) block.getLocation().getBlock().getState();
        cb.setCommand("/setblock ~ ~ ~ minecraft:mob_spawner 0 replace {EntityId:Item,SpawnData:{Item:{id:35,Count:1,Damage:" + woolValue + "}},SpawnRange:0,SpawnCount:1,MinSpawnDelay:180,MaxSpawnDelay:180,RequiredPlayerRange:8,MaxNearbyEntities:2}");
        cb.update();
        final Block blockBelow = new Location(block.getLocation().getWorld(), block.getLocation().getX(), block.getLocation().getY() - 1.0, block.getLocation().getZ()).getBlock();
        blockBelow.setType(Material.REDSTONE_BLOCK);
    }

    private void saveStartKit(final Player p) {
        for (int i = 0; i < p.getInventory().getSize(); ++i) {
            final ItemStack item = p.getInventory().getItem(i);
            this.ctw.getMapConfigHandler().saveStartupKit(item, i);
        }
    }

    private void removeArea(final String areaNumber, final Player p, final String path) {
        try {
            final int number = Integer.parseInt(areaNumber);
            final List<String> areas = this.ctw.getMapConfigHandler().getArea(path, p.getLocation());
            if (!areas.isEmpty() && areas.size() - 1 >= number) {
                this.ctw.getMapConfigHandler().removeArea(path, areas.get(number));
                this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupAreaDeleted"));
                return;
            }
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupWrongAreaNo"));
        } catch (Exception e) {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(ChatColor.RED + "Error processing the number! Make sure you type a number! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getAreas(final Player p) {
        if (this.ctw.getMapConfigHandler().getArea("ProtectedAreas", p.getLocation()).isEmpty() && this.ctw.getMapConfigHandler().getArea("RedNoAccess", p.getLocation()).isEmpty() && this.ctw.getMapConfigHandler().getArea("BlueNoAccess", p.getLocation()).isEmpty()) {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupNoArea"));
        } else {
            final List<String> msg = new ArrayList<String>();
            final List<String> protectedAreas = this.ctw.getMapConfigHandler().getArea("ProtectedAreas", p.getLocation());
            final List<String> noRedArea = this.ctw.getMapConfigHandler().getArea("RedNoAccess", p.getLocation());
            final List<String> noBlueArea = this.ctw.getMapConfigHandler().getArea("BlueNoAccess", p.getLocation());
            if (!protectedAreas.isEmpty()) {
                msg.add(new StringBuilder().append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD).append("Protected areas:").toString());
                int no = 0;
                for (final String s : protectedAreas) {
                    msg.add(" " + no + ". " + s);
                    ++no;
                }
            }
            if (!noRedArea.isEmpty()) {
                msg.add(new StringBuilder().append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD).append("Red no access areas:").toString());
                int no = 0;
                for (final String s : noRedArea) {
                    msg.add(" " + no + ". " + s);
                    ++no;
                }
            }
            if (!noBlueArea.isEmpty()) {
                msg.add(new StringBuilder().append(ChatColor.DARK_PURPLE).append(ChatColor.BOLD).append("Blue no access areas:").toString());
                int no = 0;
                for (final String s : noBlueArea) {
                    msg.add(" " + no + ". " + s);
                    ++no;
                }
            }
            for (final String sm : msg) {
                p.sendMessage(sm);
                this.ctw.getSoundHandler().sendCompleteSound(p.getLocation(), p);
            }
        }
    }

    private void addArea(final String data, final Player p) {

        //RegionSelector sel =this.ctw.getWorldEditPlugin().getSession(p).getRegionSelector(this.ctw.getWorldEditPlugin().getSession(p).getSelectionWorld());

        final Selection sel = this.ctw.getWorldEditPlugin().getSelection(p);
        if (sel != null) {
            if (data.matches("protected")) {
                this.ctw.getMapConfigHandler().addArea("ProtectedAreas", sel);
            } else if (data.matches("rednoaccess")) {
                this.ctw.getMapConfigHandler().addArea("RedNoAccess", sel);
            } else if (data.matches("bluenoaccess")) {
                this.ctw.getMapConfigHandler().addArea("BlueNoAccess", sel);
            }
            this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupAreaSet"));
        } else {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupSelectionError"));
        }
    }

    private void settime(final String data, final Player p) {
        if (data.matches("day")) {
            this.ctw.getMapConfigHandler().setString("day", "Time");
            this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupTimeSet"));
        } else if (data.matches("night")) {
            this.ctw.getMapConfigHandler().setString("night", "Time");
            this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupTimeSet"));
        } else {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(ChatColor.RED + "Time options are: day or night");
        }
    }

    private void setNumberToConfig(final String rawNumber, final Player p, final String path) {
        try {
            final int number = Integer.parseInt(rawNumber);
            this.ctw.getMapConfigHandler().setInteger(number, path);
            this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
            p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupNumberSet"));
        } catch (Exception e) {
            this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            p.sendMessage(ChatColor.RED + "Error processing the number! Make sure you type a number! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setBlockLoc(final String blockLoc, final Player p) {
        if (blockLoc.matches("redwool")) {
            this.ctw.getMapConfigHandler().setBlockLocation(p.getTargetBlock((Set<Material>) null, 6).getLocation(), "RedTeam.RedWool");
        } else if (blockLoc.matches("pinkwool")) {
            this.ctw.getMapConfigHandler().setBlockLocation(p.getTargetBlock((Set<Material>) null, 6).getLocation(), "RedTeam.PinkWool");
        } else if (blockLoc.matches("bluewool")) {
            this.ctw.getMapConfigHandler().setBlockLocation(p.getTargetBlock((Set<Material>) null, 6).getLocation(), "BlueTeam.BlueWool");
        } else if (blockLoc.matches("cyanwool")) {
            this.ctw.getMapConfigHandler().setBlockLocation(p.getTargetBlock((Set<Material>) null, 6).getLocation(), "BlueTeam.CyanWool");
        }
        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupPointSet").replaceAll("&", "§"));
    }

    private void setSpawnPoint(final String spawn, final Player p) {
        if (spawn.matches("spectator")) {
            this.ctw.getMapConfigHandler().setSpawnPoint(p.getLocation(), "SpectatorSpawn");
        } else if (spawn.matches("red")) {
            this.ctw.getMapConfigHandler().setSpawnPoint(p.getLocation(), "RedTeam.Spawn");
        } else if (spawn.matches("blue")) {
            this.ctw.getMapConfigHandler().setSpawnPoint(p.getLocation(), "BlueTeam.Spawn");
        } else if (spawn.matches("redparticles")) {
            this.ctw.getMapConfigHandler().setSpawnPoint(p.getLocation(), "VictoryParticles.Red");
        } else if (spawn.matches("blueparticles")) {
            this.ctw.getMapConfigHandler().setSpawnPoint(p.getLocation(), "VictoryParticles.Blue");
        }
        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
        p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.SetupPointSet"));
    }

    private void sendSetupInfo(final Player p) {
        this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
        for (final String s : this.ctw.getLanguageHandler().getMessageList("ChatMessages.SetupHelp")) {
            p.sendMessage(s.replaceAll("&", "§"));
        }
    }
}
