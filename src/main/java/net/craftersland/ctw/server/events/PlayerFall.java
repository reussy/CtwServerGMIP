package net.craftersland.ctw.server.events;


import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerFall implements Listener {
    private final CTW ctw;

    public PlayerFall(final CTW ctw) {
        this.ctw = ctw;
    }

    /*
        * Cancel fall damage if the player falls less than 7 blocks
     */
    @EventHandler
    public void onPlayerFall(@NotNull EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (player.getFallDistance() <= 7) {
                    e.setCancelled(true);
                }
            }

        }
    }
}
