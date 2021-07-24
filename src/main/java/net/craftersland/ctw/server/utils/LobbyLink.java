package net.craftersland.ctw.server.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class LobbyLink {
    private final CTW ctw;

    public LobbyLink(final CTW ctw) {
        this.ctw = ctw;
    }

    public void sendMotd() {
        if (this.ctw.thisServerName == null) {
            this.getBungeeServerName();
        }
        if (!this.ctw.getLobbyServersHandler().getOnlineLobbys().isEmpty()) {
            for (final String s : this.ctw.getLobbyServersHandler().getOnlineLobbys()) {
                final ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF(s);
                out.writeUTF("map");
                final ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                final DataOutputStream msgout = new DataOutputStream(msgbytes);
                try {
                    msgout.writeUTF(this.ctw.thisServerName + "#" + this.ctw.getGameEngine().motd);
                    msgout.writeShort(123);
                    out.writeShort(msgbytes.toByteArray().length);
                    out.write(msgbytes.toByteArray());
                    Bukkit.getServer().sendPluginMessage(this.ctw, "BungeeCord", out.toByteArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getBungeeServerName() {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");
        Bukkit.getServer().sendPluginMessage(this.ctw, "BungeeCord", out.toByteArray());
    }
}
