package net.craftersland.ctw.server.game;

import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import net.craftersland.ctw.server.CTW;
import org.bukkit.Location;

// TODO WE


public class ProtectionHandler {
    private CTW ctw;

    public ProtectionHandler(final CTW ctw) {
        this.ctw = ctw;
    }

    public boolean isAreaProtected(final Location l) {
        for (final CuboidRegionSelector cs : this.ctw.getMapConfigHandler().protectedAreas) {
            if (cs.contains(l)) {
                return true;
            }
            if (l.getBlockY() > this.ctw.getMapConfigHandler().maxHight || l.getBlockY() < this.ctw.getMapConfigHandler().minHight) {
                return true;
            }
            if (!this.ctw.getMapConfigHandler().gameArea.contains(l)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNoRedAccess(final Location l) {
        for (final CuboidSelection cs : this.ctw.getMapConfigHandler().redNoAccess) {
            if (cs.contains(l)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNoBlueAccess(final Location l) {
        for (final CuboidSelection cs : this.ctw.getMapConfigHandler().blueNoAccess) {
            if (cs.contains(l)) {
                return true;
            }
        }
        return false;
    }
}
