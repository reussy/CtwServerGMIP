package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamage implements Listener {
    private final CTW ctw;

    public EntityDamage(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onEntityDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            this.checkEntity(event);
            if (event.getEntity() instanceof Player) {
                final Player p = (Player) event.getEntity();
                final Player damager = (Player) event.getDamager();
                this.ctw.getLastDamageHandler().setData(p, damager, "melee");
            }
        } else if (event.getDamager() instanceof Arrow) {
            this.checkEntity(event);
            if (event.getEntity() instanceof Player) {
                final Arrow ar = (Arrow) event.getDamager();
                if (ar.getShooter() instanceof Player) {
                    final Player p2 = (Player) event.getEntity();
                    final Player damager2 = (Player) ar.getShooter();
                    this.ctw.getLastDamageHandler().setData(p2, damager2, "bow");
                }
            }
        }
    }

    private void checkEntity(final EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.ITEM_FRAME) {
            final Location l = event.getEntity().getLocation();
            if (this.ctw.getProtectionHandler().isAreaProtected(l)) {
                event.setCancelled(true);
            }
        } else if (event.getEntityType() == EntityType.PLAYER) {
            final Player p = (Player) event.getEntity();
            this.ctw.getEffectUtils().playDamageEffect(p);
            this.ctw.getTeamDamageHandler().autoAddDmg(p, event.getFinalDamage());
        }
    }
}
