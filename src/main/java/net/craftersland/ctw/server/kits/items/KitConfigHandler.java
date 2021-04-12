package net.craftersland.ctw.server.kits.items;

import net.craftersland.ctw.server.CTW;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitConfigHandler {
    private final CTW ctw;
    private final File cfgFile;
    private FileConfiguration config;

    public KitConfigHandler(final CTW ctw) {
        this.ctw = ctw;
        this.cfgFile = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "kits.yml");
        this.loadConfig();
    }

    public boolean loadConfig() {
        if (!this.cfgFile.exists()) {
            CTW.log.info("Generating the kits file...");
            this.ctw.saveResource("kits.yml", false);
        }
        CTW.log.info("Loading the kits config...");
        try {
            this.config = YamlConfiguration.loadConfiguration(this.cfgFile);
            return true;
        } catch (Exception e) {
            CTW.log.severe("Error loading the kits config! Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getString(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your kits file: " + key + " .Please re-generate your kits file!");
            return "Not Found";
        }
        return this.config.getString(key);
    }

    public List<String> getStringList(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your kits file: " + key + " .Please re-generate your kits file!");
            return new ArrayList<String>();
        }
        return this.config.getStringList(key);
    }

    public Boolean getBoolean(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your kits file: " + key + " .Please re-generate your kits file!");
            return false;
        }
        return this.config.getBoolean(key);
    }

    public Double getDouble(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your kits file: " + key + " .Please re-generate your kits file!");
            return 0.0;
        }
        return this.config.getDouble(key);
    }

    public Integer getInteger(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your kits file: " + key + " .Please re-generate your kits file!");
            return 0;
        }
        return this.config.getInt(key);
    }
}
