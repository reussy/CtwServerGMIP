package net.craftersland.ctw.server;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler {
    private final CTW pl;

    public SoundHandler(final CTW plugin) {
        this.pl = plugin;
    }

    public void sendFailedSound(final Location l, final Player p) {
        p.playSound(l, Sound.NOTE_PLING, 3.0f, 3.0f);
    }

    public void sendCompleteSound(final Location l, final Player p) {
        p.playSound(l, Sound.LEVEL_UP, 3.0f, 3.0f);
    }

    public void sendConfirmSound(final Location l, final Player p) {
        p.playSound(l, Sound.GLASS, 3.0f, 3.0f);
    }

    public void sendBaseDrumSound(final Location l, final Player p) {
        p.playSound(l, Sound.NOTE_BASS, 1.0f, 5.0f);
    }

    public void sendCreeperPrimedSound(final Location l, final Player p) {
        p.playSound(l, Sound.CREEPER_DEATH, 5.0f, 1.0f);
    }

    public void sendChestOpenSound(final Location l, final Player p) {
        p.playSound(l, Sound.CHEST_OPEN, 5.0f, 10.0f);
    }

    public void sendAnvilLandSound(final Location l, final Player p) {
        p.playSound(l, Sound.ANVIL_LAND, 3.0f, 3.0f);
    }

    public void sendItemPickupSound(final Location l, final Player p) {
        p.playSound(l, Sound.ITEM_PICKUP, 3.0f, 1.0f);
    }

    public void sendLavaPopSound(final Location l, final Player p) {
        p.playSound(l, Sound.LAVA_POP, 1.0f, 5.0f);
    }

    public void sendDoubleKillSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.CREEPER_DEATH, 1.0f, 1.0f);
        }
    }

    public void sendTripleKillSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.SKELETON_DEATH, 1.0f, 1.0f);
        }
    }

    public void sendQuadraKillSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.ENDERMAN_DEATH, 1.0f, 1.0f);
        }
    }

    public void sendPentaKillSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.HORSE_ZOMBIE_DEATH, 1.0f, 1.0f);
        }
    }

    public void sendKillingSpreeSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.HORSE_ZOMBIE_DEATH, 1.0f, 1.0f);
        }
    }

    public void sendUnstoppableSound(final Location l, final Player p) {
        if (this.pl.getConfigHandler().getBoolean("Settings.EnableKillStreakSounds")) {
            p.playSound(l, Sound.BAT_DEATH, 1.0f, 1.0f);
        }
    }

    public void broadcastLevelUpSound() {
        this.broadcastSound(Sound.LEVEL_UP);
    }

    public void broadcastAnvilLandSound() {
        this.broadcastSound(Sound.ANVIL_LAND);
    }

    public void broadcastArrowHitPlayerSound() {
        this.broadcastSound(Sound.SUCCESSFUL_HIT);
    }

    public void broadcastFireworkLaunchSound() {
        this.broadcastSound(Sound.FIREWORK_LAUNCH);
    }

    private void broadcastSound(final Sound s) {
        Bukkit.getScheduler().runTaskAsynchronously(this.pl, new Runnable() {
            @Override
            public void run() {
                final List<Player> op = new ArrayList<Player>(Bukkit.getOnlinePlayers());
                for (final Player p : op) {
                    if (p.isOnline()) {
                        p.playSound(p.getLocation(), s, 1.0f, 1.0f);
                    }
                }
            }
        });
    }
}
