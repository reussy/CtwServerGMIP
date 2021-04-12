package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.Plugin;

public class ShootBow implements Listener {
    public CTW ctw;

    public ShootBow(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onShootBow(final EntityShootBowEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                if (event.getEntity() instanceof Player) {
                    final Player p = (Player) event.getEntity();
                    if (p.hasPermission("CTW.particles")) {
                        final Arrow ar = (Arrow) event.getProjectile();
                        ShootBow.this.ctw.getEffectUtils().addArrowTrail(ar, p);
                    }
                }
            }
        });
    }
}
