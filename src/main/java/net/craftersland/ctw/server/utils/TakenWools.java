package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TakenWools {
    private final CTW ctw;

    public TakenWools(final CTW ctw) {
        this.ctw = ctw;
    }

    public void redWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (!this.ctw.getWoolHandler().isRedTaken()) {
                this.ctw.getNewScoreboardHandler().redWoolTaken();
                this.ctw.getWoolHandler().addRedTakenByPlayer(p);
                this.ctw.getWoolHandler().setRedTaken();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.RedWoolCaptured.subtitle"));
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.RedWoolCaptureFirst").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            } else if (!this.ctw.getWoolHandler().hadRedTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addRedTakenByPlayer(p);
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.RedWoolCapture").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            }
        }
    }

    public void pinkWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (!this.ctw.getWoolHandler().isPinkTaken()) {
                this.ctw.getNewScoreboardHandler().pinkWoolTaken();
                this.ctw.getWoolHandler().addPinkTakenByPlayer(p);
                this.ctw.getWoolHandler().setPinkTaken();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.PinkWoolCaptured.subtitle"));
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.PinkWoolCaptureFirst").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            } else if (!this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addPinkTakenByPlayer(p);
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.PinkWoolCapture").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getTeamWoolsCaptured().addRedCaptured();
            }
        }
    }

    public void blueWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.BLUE) {
            if (!this.ctw.getWoolHandler().isBlueTaken()) {
                this.ctw.getNewScoreboardHandler().blueWoolTaken();
                this.ctw.getWoolHandler().addBlueTakenByPlayer(p);
                this.ctw.getWoolHandler().setBlueTaken();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.BlueWoolCaptured.subtitle"));
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.BlueWoolCaptureFirst").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            } else if (!this.ctw.getWoolHandler().hadBlueTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addBlueTakenByPlayer(p);
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.BlueWoolCapture").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            }
        }
    }

    public void cyanWoolTakenCheck(final Player p) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.BLUE) {
            if (!this.ctw.getWoolHandler().isCyanTaken()) {
                this.ctw.getNewScoreboardHandler().cyanWoolTaken();
                this.ctw.getWoolHandler().addCyanTakenByPlayer(p);
                this.ctw.getWoolHandler().setCyanTaken();
                this.ctw.getMessageUtils().broadcastTitleMessage(this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.title"), this.ctw.getLanguageHandler().getMessage("TitleMessages.CyanWoolCaptured.subtitle"));
                this.ctw.getSoundHandler().broadcastFireworkLaunchSound();
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.CyanWoolCaptureFirst").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getPlayerScoreHandler().addScore(p, 2);
                this.ctw.getEconomyHandler().addCoins(p, 25.0);
                this.ctw.getMessageUtils().sendScoreMessage(p, "+2", 25);
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            } else if (!this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                this.ctw.getWoolHandler().addCyanTakenByPlayer(p);
                this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.CyanWoolCapture").replaceAll("%PlayerName%", p.getName()));
                this.ctw.getTeamWoolsCaptured().addBlueCaptured();
            }
        }
    }

    public void checkForLostWool(final Player p, final List<ItemStack> items) {
        final TeamHandler.Teams team = this.ctw.getTeamHandler().getTeam(p);
        if (team == TeamHandler.Teams.RED) {
            if (this.ctw.getWoolHandler().hadRedTakenByPlayer(p) && this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                final byte data1 = 14;
                final byte data2 = 6;
                if (this.woolLost(items, data1) && this.woolLost(items, data2)) {

                    this.ctw.getWoolHandler().removeRedTakenByPlayer(p);
                    this.ctw.getWoolHandler().removePinkTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.AllWoolsLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);

                }
            } else if (this.ctw.getWoolHandler().hadRedTakenByPlayer(p)) {
                final byte data3 = 14;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeRedTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.RedWoolLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadPinkTakenByPlayer(p)) {
                final byte data3 = 6;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removePinkTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.PinkWoolLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            }
        } else if (team == TeamHandler.Teams.BLUE) {
            if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(p) && this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                final byte data1 = 11;
                final byte data2 = 9;
                if (this.woolLost(items, data1) && this.woolLost(items, data2)) {
                    this.ctw.getWoolHandler().removeBlueTakenByPlayer(p);
                    this.ctw.getWoolHandler().removeCyanTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.AllWoolsLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadBlueTakenByPlayer(p)) {
                final byte data3 = 11;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeBlueTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.BlueWoolLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            } else if (this.ctw.getWoolHandler().hadCyanTakenByPlayer(p)) {
                final byte data3 = 9;
                if (this.woolLost(items, data3)) {
                    this.ctw.getWoolHandler().removeCyanTakenByPlayer(p);
                    this.ctw.getMessageUtils().broadcastActionBarMessage(this.ctw.getLanguageHandler().getMessage("ActionBarMessages.CyanWoolLost").replaceAll("%PlayerName%", this.ctw.getMessageUtils().getTeamColorBolded(p)));
                    this.ctw.getEffectUtils().sendWoolLostEffect(p);
                }
            }
        }
    }

    private boolean woolLost(final List<ItemStack> items, final byte data) {
        for (final ItemStack i : items) {
            if (i != null && i.getType() == Material.WOOL && i.getData().getData() == data) {
                return true;
            }
        }
        return false;
    }
}
