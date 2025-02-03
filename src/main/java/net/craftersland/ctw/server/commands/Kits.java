package net.craftersland.ctw.server.commands;

import net.craftersland.ctw.server.CTW;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kits implements CommandExecutor {
    private final CTW ctw;

    public Kits(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
        if (cmdlabel.equalsIgnoreCase("shop")) {
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (args.length == 0) {
                    this.ctw.getKitHandler().sendKitMenu(p);
                } else {
                    this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                    p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.KitUsage"));
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You cant run this command from server console!");
            }
            return true;
        }
        return false;
    }
}
