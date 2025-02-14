package net.craftersland.ctw.server.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class GlobalChat implements CommandExecutor {
    private final CTW ctw;

    public GlobalChat(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final @NotNull String cmdlabel, final String[] args) {
        if (cmdlabel.equalsIgnoreCase("g")) {
            if (sender instanceof Player player) {
                if (args.length == 0) {
                    this.ctw.getSoundHandler().sendFailedSound(player.getLocation(), player);
                    player.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalChatUsage"));
                } else {
                    this.sendGlobalMsg(args, player);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You cant run this command from server console!");
            }
            return true;
        }
        return false;
    }

    private void sendGlobalMsg(final String[] msgList, final Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, () -> {
            StringBuilder msg = new StringBuilder();
            String[] val$msgList;
            for (int length = (val$msgList = msgList).length, i = 0; i < length; ++i) {
                final String s = val$msgList[i];
                msg.append(s).append(" ");
            }

            String prefixTeam = this.ctw.getTeamHandler().isSpectator(player)
                    ? this.ctw.getLanguageHandler().getMessage("ChatMessages.SpectatorPrefix")
                    : this.ctw.getTeamHandler().isBlueTeam(player)
                    ? this.ctw.getLanguageHandler().getMessage("ChatMessages.BluePrefix")
                    : this.ctw.getLanguageHandler().getMessage("ChatMessages.RedPrefix");

            String globalMsg = this.ctw.getConfigHandler().getStringWithColor("ChatFormat.GlobalChatCmd")
                    .replace("<TeamPrefix>", prefixTeam)
                    .replace("<GlobalPrefix>", this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalPrefix"))
                    .replace("<PlayerName>", player.getName())
                    .replace("<Message>", msg.toString());

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, globalMsg)));
        });
    }
}
