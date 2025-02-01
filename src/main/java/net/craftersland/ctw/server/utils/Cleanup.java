package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

public class Cleanup {
    private final CTW ctw;

    public Cleanup(final CTW ctw) {
        this.ctw = ctw;
    }

    public void cleanup() {
        if (this.ctw.getConfigHandler().getBoolean("DataCleanup.Enabled")) {
            CTW.log.info("Running data cleanup...");
            final List<String> path = this.ctw.getConfigHandler().getStringList("DataCleanup.CleanUp-Paths");
            int count = 1;
            final int amount = path.size();
            for (final String s : path) {
                CTW.log.info("Cleanup stage: " + count + "/" + amount);
                final File data = new File(s);
                if (data.exists()) {
                    try {
                        if (data.isDirectory()) {
                            FileUtils.deleteDirectory(data);
                        } else if (data.isFile()) {
                            FileUtils.forceDelete(data);
                        }
                    } catch (Exception e) {
                        CTW.log.warning("Cleanup error for: " + s + " Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    CTW.log.warning("Cleanup error! Could not find: " + s);
                }
                ++count;
            }
            CTW.log.info("Data cleanup complete!");
        } else {
            CTW.log.info("Data cleanup is disabled.");
        }
    }
}
