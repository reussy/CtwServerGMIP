package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;

public class TabList {
    private final CTW ctw;

    public TabList(final CTW ctw) {
        this.ctw = ctw;
    }

    public String getTabTitle() {
        String title = this.ctw.getLanguageHandler().getMessage("TabList.Header");
        title = title.replaceAll("%MapName%", this.ctw.getMapHandler().currentMap);
        return title;
    }

    public String getTabFooter() {
        String footer = this.ctw.getLanguageHandler().getMessage("TabList.Footer");
        footer = footer.replaceAll("%RedTeamVictory%", this.ctw.getTeamVictoryHandler().getRedVictoryScore().toString());
        footer = footer.replaceAll("%BlueTeamVictory%", this.ctw.getTeamVictoryHandler().getBlueVictoryScore().toString());
        return footer;
    }
}
