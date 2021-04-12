package net.craftersland.ctw.server.events;

import net.craftersland.ctw.server.CTW;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPing implements Listener {
    private final CTW ctw;

    public ServerPing(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onServerList(final ServerListPingEvent event) {
        event.setMotd(this.ctw.getGameEngine().motd.replaceAll("&", "§"));
    }
}
