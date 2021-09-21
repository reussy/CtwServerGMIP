package net.craftersland.ctw.server.game;

import net.craftersland.ctw.server.CTW;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class KillStreakHandler {
    private final CTW ctw;
    private final Map<Player, Integer> streak;

    public KillStreakHandler(final CTW ctw) {
        this.streak = new HashMap<Player, Integer>();
        this.ctw = ctw;
    }

    public void resetData(final Player p) {
        this.streak.put(p, 0);
    }




    public void addStreakKill(final Player p) {
        if (this.streak.containsKey(p)) {
            final int data = this.streak.get(p);
            this.streak.put(p, data + 1);
        } else {
            this.streak.put(p, 1);
        }
        this.checkStreak(p);
    }

    public Integer getStreak(final Player p) {
        return this.streak.get(p);
    }

    public void removeOnDisconnect(final Player p) {
        this.streak.remove(p);
    }

    private void checkStreak(final Player p) {
        final int data = this.streak.get(p);
        if (data == 2) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.DoubleKill.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.DoubleKill.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendDoubleKillSound(p.getLocation(), p);
        } else if (data == 3) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.TripleKill.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.TripleKill.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendTripleKillSound(p.getLocation(), p);
        } else if (data == 4) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.QuadraKill.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.QuadraKill.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendQuadraKillSound(p.getLocation(), p);
        } else if (data == 5) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PentaKill.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.PentaKill.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendPentaKillSound(p.getLocation(), p);
        } else if (data == 6) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.KillingSpree.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.KillingSpree.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendKillingSpreeSound(p.getLocation(), p);
        } else if (data >= 7) {
            this.ctw.getMessageUtils().sendTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.Unstoppable.title").replaceAll("&", "§"), this.ctw.getLanguageHandler().getMessage("TitleMessages.Unstoppable.subtitle").replaceAll("&", "§"), p);
            this.ctw.getSoundHandler().sendUnstoppableSound(p.getLocation(), p);
        }
    }
}
