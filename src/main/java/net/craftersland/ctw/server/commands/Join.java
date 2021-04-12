package net.craftersland.ctw.server.commands;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Join implements CommandExecutor {
    private final CTW ctw;

    public Join(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String cmdlabel, final String[] args) {
        if (!cmdlabel.equalsIgnoreCase("join")) {
            return false;
        }
        if (args.length != 0) {
            sender.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.JoinUsage"));
            return true;
        }
        if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (this.ctw.getTeamHandler().isSpectator(p)) {
                this.joinCmd(p);
            } else {
                this.ctw.getSoundHandler().sendFailedSound(p.getLocation(), p);
                p.sendMessage(this.ctw.getLanguageHandler().getMessage("ChatMessages.AreadyJoinedTeam"));
            }
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You can't run this command from server console!");
        return true;
    }

    private void joinCmd(final Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(this.ctw, new Runnable() {
            @Override
            public void run() {
                try {
                    final Inventory inv = Join.this.ctw.getJoinMenu().JoinMenuGUI(p);
                    p.openInventory(inv);
                    Join.this.ctw.getSoundHandler().sendConfirmSound(p.getLocation(), p);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
