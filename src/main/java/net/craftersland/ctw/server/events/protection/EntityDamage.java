package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EntityDamage implements Listener {
    private final CTW ctw;

    public EntityDamage(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onEntityDamage(final @NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            this.checkEntity(event);
            if (event.getEntity() instanceof Player) {
                final Player p = (Player) event.getEntity();
                final Player damager = (Player) event.getDamager();

                this.ctw.getLastDamageHandler().setData(p, damager, "melee");

                if (ctw.getTeamHandler().getTeam(p) == ctw.getTeamHandler().getTeam(damager)) {

                    event.setCancelled(true);

                }
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

    @EventHandler
    public void onEntityTeam(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            final Player p = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();

            if (ctw.getTeamHandler().getTeam(p) == ctw.getTeamHandler().getTeam(damager)) {

                event.setCancelled(true);

            }
        } else if (event.getDamager() instanceof Projectile) {

            final Player p = (Player) ((Projectile) event.getDamager()).getShooter();
            final Player damager = (Player) event.getEntity();

            if (ctw.getTeamHandler().getTeam(p) == ctw.getTeamHandler().getTeam(damager)) {

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItem(PlayerItemDamageEvent event) {

        ItemStack weapon = event.getItem();

        if (weapon.getType() == Material.DIAMOND_SWORD || weapon.getType() == Material.GOLD_SWORD || weapon.getType() == Material.STONE_SWORD || weapon.getType() == Material.IRON_SWORD || weapon.getType() == Material.WOOD_SWORD) {
            weapon.getItemMeta().spigot().setUnbreakable(true);
            event.getPlayer().updateInventory();
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
            //this.ctw.getEffectUtils().playDamageEffect(p);
            this.ctw.getTeamDamageHandler().autoAddDmg(p, event.getFinalDamage());
        }
    }
}

