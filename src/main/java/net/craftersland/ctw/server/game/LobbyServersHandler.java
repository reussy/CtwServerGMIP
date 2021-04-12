package net.craftersland.ctw.server.game;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

public class LobbyServersHandler {
    private final CTW ctw;
    private final Map<String, String> lobbysAddress;
    private final Set<String> onlineLobbys;
    private final Map<String, Integer> lobbysPlayerCount;
    private final Map<Integer, String> mapForSorting;
    private String lobbyToTeleport;

    public LobbyServersHandler(final CTW ctw) {
        this.lobbysAddress = new HashMap<String, String>();
        this.onlineLobbys = new HashSet<String>();
        this.lobbysPlayerCount = new HashMap<String, Integer>();
        this.mapForSorting = new HashMap<Integer, String>();
        this.lobbyToTeleport = "lobby";
        this.ctw = ctw;
        this.lobbyToTeleport = ctw.getConfigHandler().getStringList("LobbyServers").get(0);
    }

    public Set<String> getOnlineLobbys() {
        return this.onlineLobbys;
    }

    public String getLobbyToTeleport() {
        return this.lobbyToTeleport;
    }

    private void sortLobbyForTeleport() {
        final Object[] sorted = this.mapForSorting.keySet().toArray();
        Arrays.sort(sorted);
        if (!this.mapForSorting.isEmpty()) {
            this.lobbyToTeleport = this.mapForSorting.get(sorted[0]);
            this.mapForSorting.clear();
        }
    }

    public void updateLobbysStatus() {
        if (this.lobbysAddress.isEmpty()) {
            this.requestLobbysAddresses();
        }
        this.onlineLobbys.clear();
        if (!this.lobbysAddress.isEmpty()) {
            for (final String s : this.lobbysAddress.keySet()) {
                final String[] address = this.lobbysAddress.get(s).split(":");
                try {
                    final Socket sock = new Socket(address[0], Integer.parseInt(address[1]));
                    final DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                    final DataInputStream in = new DataInputStream(sock.getInputStream());
                    out.write(254);
                    final StringBuffer str = new StringBuffer();
                    int b;
                    while ((b = in.read()) != -1) {
                        if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                            str.append((char) b);
                        }
                    }
                    final String[] data = str.toString().split("§");
                    sock.close();
                    final int onlineIndex = data.length - 2;
                    final int onlineP = Integer.parseInt(data[onlineIndex]);
                    this.onlineLobbys.add(s);
                    this.lobbysPlayerCount.put(s, onlineP);
                    this.mapForSorting.put(onlineP, s);
                } catch (Exception e) {
                    this.lobbysPlayerCount.put(s, 0);
                    continue;
                } finally {
                    this.sortLobbyForTeleport();
                }
                this.sortLobbyForTeleport();
            }
        }
    }

    public void saveLobbyAddresses(final String server, final String address) {
        this.lobbysAddress.put(server, address);
    }

    private void requestLobbysAddresses() {
        for (final String s : this.ctw.getConfigHandler().getStringList("LobbyServers")) {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ServerIP");
            out.writeUTF(s);
            Bukkit.getServer().sendPluginMessage(this.ctw, "BungeeCord", out.toByteArray());
        }
    }
}
