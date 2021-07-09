package net.craftersland.ctw.server.events;


import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFall implements Listener {
    private final CTW ctw;

    public PlayerFall(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent e){

        if(e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();

            if(e.getCause() == EntityDamageEvent.DamageCause.FALL){

                if(player.getFallDistance() <= 10) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
