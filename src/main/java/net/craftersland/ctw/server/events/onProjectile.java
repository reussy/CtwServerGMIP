package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.PlayerProjectile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onProjectile implements Listener {

    private final CTW ctw;

    public onProjectile(final CTW ctw) {
        this.ctw = ctw;
    }


    @EventHandler
    public void onShoot(EntityDamageByEntityEvent e) {

        Entity arro = e.getDamager();

        if (arro.getType() == EntityType.FISHING_HOOK) {

            Projectile arrow = (Projectile) arro;
            AddDistance(e, arrow);
        }

        if (arro.getType() == EntityType.ARROW) {

            Arrow arrow = (Arrow) arro;
            AddDistance(e, arrow);
        }

        if (arro.getType() == EntityType.SNOWBALL) {

            Snowball arrow = (Snowball) arro;
            AddDistance(e, arrow);
        }

    }

    private void AddDistance(EntityDamageByEntityEvent e, Projectile arrow) {
        if (arrow.getShooter() instanceof Player && e.getEntity() instanceof Player) {

            Player damaged = (Player) arrow.getShooter();
            Player damager = (Player) e.getEntity();

            if (arrow.getType() == EntityType.ARROW) {

                if (this.ctw.getTeamHandler().getTeam(damaged) != this.ctw.getTeamHandler().getTeam(damager)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!damager.isDead()) {
                                if (!(damager.getHealth() == damager.getMaxHealth()) || !(((int) damager.getHealth()) == 0)) {
                                    damaged.sendMessage(ChatColor.GRAY + "La vida de " + ChatColor.GREEN + damager.getName() + ChatColor.GRAY + " es " + ChatColor.GREEN + (int) damager.getHealth() + ChatColor.RED + "❤");
                                }
                            }
                        }
                    }.runTaskLater(ctw, 5);
                }
            }

            int distance = (int) damaged.getLocation().distance(damager.getLocation());

            if (!ctw.playerProjectile.containsKey(damaged.getUniqueId())) {

                PlayerProjectile pj = new PlayerProjectile(distance, damager.getUniqueId());
                ctw.playerProjectile.put(damaged.getUniqueId(), pj);
                pj.setProjectile(damager.getUniqueId(), distance);

            } else {

                PlayerProjectile pj = ctw.playerProjectile.get(damaged.getUniqueId());
                pj.setProjectile(damager.getUniqueId(), distance);

            }
        }
    }

}
