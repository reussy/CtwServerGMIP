package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LastDamageHandler {
    private final Map<Player, Long> lastDamageTime;
    private final Map<Player, Player> lastDamager;
    private final Map<Player, String> weaponUsed;

    public LastDamageHandler() {
        this.lastDamageTime = new HashMap<>();
        this.lastDamager = new HashMap<>();
        this.weaponUsed = new HashMap<>();
    }

    public void setData(final Player p, final Player damager, final String weaponType) {
        this.lastDamageTime.put(p, System.currentTimeMillis());
        this.lastDamager.put(p, damager);
        this.weaponUsed.put(p, weaponType);
    }

    public void removeData(final Player p) {
        this.lastDamageTime.remove(p);
        this.lastDamager.remove(p);
        this.weaponUsed.remove(p);
    }

    public Player getKiller(final Player p) {
        if (!this.lastDamageTime.containsKey(p)) {
            return null;
        }
        if (System.currentTimeMillis() - this.lastDamageTime.get(p) > 10000L) {
            return null;
        }
        return this.lastDamager.get(p);
    }

    public String getWeaponType(final Player p) {
        return this.weaponUsed.get(p);
    }
}
