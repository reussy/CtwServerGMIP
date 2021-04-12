package net.craftersland.ctw.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private final CTW ctw;
    public List<String> maps;

    public ConfigHandler(final CTW ctw) {
        this.maps = new ArrayList<String>();
        this.ctw = ctw;
        this.loadConfig();
    }

    public void loadConfig() {
        final File pluginFolder = new File("plugins" + System.getProperty("file.separator") + "CTWserver");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        final File configFile = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "config.yml");
        if (!configFile.exists()) {
            CTW.log.info("No config file found! Creating new one...");
            this.ctw.saveDefaultConfig();
        }
        try {
            this.ctw.getConfig().load(configFile);
            CTW.log.info("Config file loaded!");
        } catch (Exception e) {
            CTW.log.info("Could not load config file! You need to regenerate the config! Error: " + e.getMessage());
            e.printStackTrace();
        }
        this.maps = this.getStringList("GameMaps");
    }

    public String getString(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate '" + key + "' in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return "errorCouldNotLocateInConfigYml: " + key;
        }
        return this.ctw.getConfig().getString(key);
    }

    public String getStringWithColor(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate " + key + " in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return "errorCouldNotLocateInConfigYml:" + key;
        }
        return this.ctw.getConfig().getString(key).replaceAll("&", "§");
    }

    public Integer getInteger(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate '" + key + "' in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return null;
        }
        return this.ctw.getConfig().getInt(key);
    }

    public Boolean getBoolean(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate '" + key + "' in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return null;
        }
        return this.ctw.getConfig().getBoolean(key);
    }

    public Double getDouble(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate '" + key + "' in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return null;
        }
        return this.ctw.getConfig().getDouble(key);
    }

    public List<String> getStringList(final String key) {
        if (!this.ctw.getConfig().contains(key)) {
            this.ctw.getLogger().severe("Could not locate '" + key + "' in the config.yml inside of the CTWserver folder! (Try generating a new one by deleting the current)");
            return null;
        }
        return this.ctw.getConfig().getStringList(key);
    }
}
