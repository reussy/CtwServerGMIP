package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;

public class RestartHandler {
    private final CTW pl;
    private static double usagePercent1;
    private static double usagePercent2;
    private static int checkIndex;
    private static long startTime;
    private static long lastCheck;

    static {
        RestartHandler.usagePercent1 = 0.0;
        RestartHandler.usagePercent2 = 0.0;
        RestartHandler.checkIndex = 1;
    }

    public RestartHandler(final CTW plugin) {
        this.pl = plugin;
        RestartHandler.startTime = (RestartHandler.lastCheck = System.currentTimeMillis());
    }

    public void checkMemoryUsage() {
        if ((System.currentTimeMillis() - RestartHandler.lastCheck) / 1000L > 60L) {
            RestartHandler.lastCheck = System.currentTimeMillis();
            final double maxMemory = (double) (Runtime.getRuntime().maxMemory() / 1024L);
            final double freeMemory = (double) (Runtime.getRuntime().freeMemory() / 1024L);
            final double usedMemory = maxMemory - freeMemory;
            final double usagePercentage = usedMemory / maxMemory * 100.0;
            final long serverUptime = System.currentTimeMillis() - RestartHandler.startTime;
            if (RestartHandler.checkIndex == 1) {
                RestartHandler.usagePercent1 = usagePercentage;
            } else {
                RestartHandler.usagePercent2 = usagePercentage;
            }
            ++RestartHandler.checkIndex;
            if (RestartHandler.checkIndex >= 3) {
                RestartHandler.checkIndex = 1;
            }
            CTW.log.info("Memory usage - Check 1: " + RestartHandler.usagePercent1 + "% - Check 2: " + RestartHandler.usagePercent2 + "%");
            if (RestartHandler.usagePercent1 >= 95.0 && RestartHandler.usagePercent2 >= 95.0 && this.pl.getConfigHandler().getBoolean("Settings.ServerLowMemoryRestart")) {
                this.serverStop();
            } else if (serverUptime / 1000L > 7200L && Bukkit.getOnlinePlayers().size() == 0) {
                this.serverStop();
            }
        }
    }

    public void serverStop() {
        Bukkit.getServer().shutdown();
    }
}
