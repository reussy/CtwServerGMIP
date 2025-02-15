package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;

public class ShootBow implements Listener {
    public CTW ctw;

    public ShootBow(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onShootBow(final @NotNull EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.hasPermission("CTW.particles")) {
                Arrow ar = (Arrow) event.getProjectile();
                ShootBow.this.ctw.getEffectUtils().addArrowTrail(ar, player);
            }
        }
    }
}
