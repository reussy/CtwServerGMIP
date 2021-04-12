package net.craftersland.ctw.server;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class LanguageHandler {
    private final CTW ctw;
    private final Language lang;
    private final File data;
    private FileConfiguration config;

    public LanguageHandler(final CTW ctw) {
        this.ctw = ctw;
        this.lang = Language.EN;
        this.data = new File("plugins" + System.getProperty("file.separator") + "CTWserver" + System.getProperty("file.separator") + "Lang-EN.yml");
        this.loadLangFile();
    }

    public boolean loadLangFile() {
        if (!this.data.exists()) {
            CTW.log.info("Generating language file: " + this.lang);
            this.ctw.saveResource("Lang-EN.yml", false);
        }
        CTW.log.info("Loading language file: " + this.lang);
        try {
            this.config = YamlConfiguration.loadConfiguration(this.data);
            return true;
        } catch (Exception e) {
            CTW.log.severe("Error loading language file! Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getMessage(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your language file: " + key + " .Please re-generate your language file!");
            return "Msg not found!";
        }
        return this.config.getString(key).replaceAll("&", "§");
    }

    public List<String> getMessageList(final String key) {
        if (!this.config.contains(key)) {
            CTW.log.warning("Could not locate in your language file: " + key + " .Please re-generate your language file!");
            return null;
        }
        return this.config.getStringList(key);
    }

    private enum Language {
        EN("EN", 0);

        Language(final String s, final int n) {
        }
    }
}
