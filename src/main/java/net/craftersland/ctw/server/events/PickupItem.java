package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.GameEngine;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupItem implements Listener {
    public CTW ctw;

    public PickupItem(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {

        if (this.ctw.getGameEngine().gameStage == GameEngine.GameStages.COUNTDOWN) {
            event.setCancelled(true);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {

            if (event.getItem().getItemStack().getType() == Material.WOOL) {
                final ItemStack item = event.getItem().getItemStack();
                final Player p = event.getPlayer();
                TeamHandler.Team team = this.ctw.getTeamHandler().getTeam(p);

                // Cancelar si la lana no es de su equipo
                if (team == TeamHandler.Team.RED) {
                    if (item.getData().getData() == 9 || item.getData().getData() == 11) {
                        event.setCancelled(true);
                        return;
                    }
                } else if (team == TeamHandler.Team.BLUE) {
                    if (item.getData().getData() == 14 || item.getData().getData() == 6) {
                        event.setCancelled(true);
                        return;
                    }
                }

                ctw.getWoolDistanceTracker().getPlayerTeams().put(p, team);
                ctw.getTakenWools().woolTakenCheck(p, item.getData().getData());
            }
        });
    }
}
