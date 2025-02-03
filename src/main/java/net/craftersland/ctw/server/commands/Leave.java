package net.craftersland.ctw.server.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Leave implements CommandExecutor {
    private final CTW ctw;

    public Leave(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
        if (cmdlabel.equalsIgnoreCase("leave")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    final Player p = (Player) sender;
                    this.ctw.getSoundHandler().sendCreeperPrimedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.LeaveMsg"));
                    this.teleportToLobby(p);
                } else {
                    sender.sendMessage(ChatColor.RED + "You can't run this command from server console!");
                }
            }
            return true;
        }
        return false;
    }

    private void teleportToLobby(final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
            final ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(Leave.this.ctw.getLobbyServersHandler().getLobbyToTeleport());
            p.sendPluginMessage(Leave.this.ctw, "BungeeCord", out.toByteArray());
        });
    }
}
