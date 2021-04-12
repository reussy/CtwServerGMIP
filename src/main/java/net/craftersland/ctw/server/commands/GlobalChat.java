package net.craftersland.ctw.server.commands;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GlobalChat implements CommandExecutor {
    private final CTW ctw;

    public GlobalChat(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
        if (cmdlabel.equalsIgnoreCase("g")) {
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (args.length == 0) {
                    this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalChatUsage"));
                } else {
                    this.sendGlobalMsg(args, p);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You cant run this command from server console!");
            }
            return true;
        }
        return false;
    }

    private void sendGlobalMsg(final String[] msgList, final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                String msg = "";
                String[] val$msgList;
                for (int length = (val$msgList = msgList).length, i = 0; i < length; ++i) {
                    final String s = val$msgList[i];
                    msg = msg + s + " ";
                }
                String mFormat = GlobalChat.this.ctw.getConfigHandler().getStringWithColor("ChatFormat.GlobalChatCmd");
                mFormat = mFormat.replaceAll("<GlobalPrefix>", GlobalChat.this.ctw.getLanguageHandler().getMessage("ChatMessages.GlobalPrefix"));
                mFormat = mFormat.replaceAll("<PlayerName>", GlobalChat.this.ctw.getMessageUtils().getTeamColor(p));
                mFormat = mFormat.replaceAll("<Message>", msg);
                Bukkit.broadcastMessage(mFormat);
            }
        });
    }
}
