package net.craftersland.ctw.server.utils;

import io.puharesource.mc.titlemanager.api.ActionbarTitleObject;
import io.puharesource.mc.titlemanager.api.TabTitleObject;
import io.puharesource.mc.titlemanager.api.TitleObject;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageUtils {
    private final CTW ctw;

    public MessageUtils(final CTW ctw) {
        this.ctw = ctw;
    }

    public void broadcastTitleMessage(final String title, final String subtitle) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new TitleObject(title, subtitle).broadcast();
            }
        });
    }

    public void sendTitleMessage(final String title, final String subtitle, final Player p) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new TitleObject(title, subtitle).send(p);
            }
        });
    }

    public void broadcastActionBarMessage(final String msg) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new ActionbarTitleObject(msg).broadcast();
            }
        });
    }

    public void sendActionBarMessage(final String msg, final Player p) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new ActionbarTitleObject(msg).send(p);
            }
        });
    }

    public String getTeamColor(final Player p) {
        String name = p.getName();
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            name = ChatColor.RED + name;
        } else if (team == TeamHandler.Teams.BLUE) {
            name = ChatColor.BLUE + name;
        }
        return name;
    }

    public String getTeamColorBolded(final Player p) {
        String name = p.getName();
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            name = new StringBuilder().append(ChatColor.RED).append(ChatColor.BOLD).append(name).toString();
        } else if (team == TeamHandler.Teams.BLUE) {
            name = new StringBuilder().append(ChatColor.BLUE).append(ChatColor.BOLD).append(name).toString();
        }
        return name;
    }

    public void broadcastTabTitleFooter() {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new TabTitleObject(MessageUtils.this.ctw.getTabList().getTabTitle(), MessageUtils.this.ctw.getTabList().getTabFooter()).broadcast();
            }
        });
    }

    public void sendTabTitleFooter(final Player p) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                new TabTitleObject(MessageUtils.this.ctw.getTabList().getTabTitle(), MessageUtils.this.ctw.getTabList().getTabFooter()).send(p);
            }
        });
    }

    public void sendScoreMessage(final Player p, final String score, final Integer coins) {
        String msg = this.ctw.getLanguageHandler().getMessage("ChatMessages.ScoreMessage").replaceAll("%points%", score);
        msg = msg.replaceAll("%score%", this.ctw.getPlayerScoreHandler().getScore(p).toString());
        if (coins != null) {
            if (p.hasPermission("CTW.3xCoinMultiplier")) {
                final int coin = coins * 3;
                String cMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.Coins3xMultiplier").replaceAll("%coins%", new StringBuilder(String.valueOf(coin)).toString());
                cMsg = cMsg.replaceAll("%balance%", this.ctw.getEconomyHandler().getCoins(p).toString());
                msg = msg + cMsg;
            } else if (p.hasPermission("CTW.2xCoinMultiplier")) {
                final int coin = coins * 2;
                String cMsg = this.ctw.getLanguageHandler().getMessage("ChatMessages.Coins2xMultiplier").replaceAll("%coins%", new StringBuilder(String.valueOf(coin)).toString());
                cMsg = cMsg.replaceAll("%balance%", this.ctw.getEconomyHandler().getCoins(p).toString());
                msg = msg + cMsg;
            } else {
                String cMsg2 = this.ctw.getLanguageHandler().getMessage("ChatMessages.CoinsNoMultiplier").replaceAll("%coins%", new StringBuilder().append(coins).toString());
                cMsg2 = cMsg2.replaceAll("%balance%", this.ctw.getEconomyHandler().getCoins(p).toString());
                msg = msg + cMsg2;
            }
        }
        p.sendMessage(msg);
    }

    public void broadcastGameStats() {
        final List<String> mList = new ArrayList<String>();
        for (String s : this.ctw.getLanguageHandler().getMessageList("ChatMessages.GameStats")) {
            s = s.replaceAll("&", "§");
            s = s.replaceAll("%RedTeamVictory%", this.ctw.getTeamVictoryHandler().getRedVictoryScore().toString());
            s = s.replaceAll("%BlueTeamVictory%", this.ctw.getTeamVictoryHandler().getBlueVictoryScore().toString());
            s = s.replaceAll("%RedTeamScore%", this.ctw.getTeamScoreHandler().getRedScore().toString());
            s = s.replaceAll("%BlueTeamScore%", this.ctw.getTeamVictoryHandler().getBlueVictoryScore().toString());
            s = s.replaceAll("%RedTeamKills%", this.ctw.getTeamKillsHandler().getRedKills().toString());
            s = s.replaceAll("%BlueTeamKills%", this.ctw.getTeamKillsHandler().getBlueKills().toString());
            s = s.replaceAll("%RedTeamDamage%", this.ctw.getTeamDamageHandler().getRedDmg().toString());
            s = s.replaceAll("%BlueTeamDamage%", this.ctw.getTeamDamageHandler().getBlueDmg().toString());
            s = s.replaceAll("%RedTeamWools%", this.ctw.getTeamWoolsCaptured().getRedCaptured().toString());
            s = s.replaceAll("%BlueTeamWools%", this.ctw.getTeamWoolsCaptured().getBlueCaptured().toString());
            mList.add(s);
        }
        for (final String msg : mList) {
            Bukkit.broadcastMessage(msg);
        }
        mList.clear();
    }
}
