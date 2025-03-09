package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ProtectedMoveHandler {
    private final CTW ctw;
    private final Set<Player> players;
    private final Set<Player> msgCooldown;

    public ProtectedMoveHandler(final CTW ctw) {
        this.players = new HashSet<>();
        this.msgCooldown = new HashSet<>();
        this.ctw = ctw;
    }

    public void addPlayerToList(final Player p) {
        this.players.add(p);
    }

    public void removePlayerFromList(final Player p) {
        this.players.remove(p);
    }

    public boolean isPlayerOnList(final Player p) {
        return this.players.contains(p);
    }

    public void sendWarningMsg(final Player p) {
        if (!this.msgCooldown.contains(p)) {
            Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
                this.msgCooldown.add(p);
                //p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.CantEnterArea").replaceAll("&", "§"));
                this.ctw.getMessageUtils().sendActionBarMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.CantEnterArea"), p);
                this.removeMsgCooldown(p);
                this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
            });
        }
    }

    private void removeMsgCooldown(final Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.ctw, () -> this.msgCooldown.remove(p), 100L);
    }
}
