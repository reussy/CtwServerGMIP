package net.craftersland.ctw.server.events.protection;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockExplode implements Listener {
    private final CTW ctw;

    public BlockExplode(final CTW ctw) {
        this.ctw = ctw;
    }

    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent event) {
        if (!this.ctw.getConfigHandler().getBoolean("Settings.allowBlockExplode")) {
            event.blockList().clear();
        } else {
            final List<Block> blocks = new ArrayList<>(event.blockList());
            for (final Block b : blocks) {
                if (this.ctw.getProtectionHandler().isAreaProtected(b.getLocation())) {
                    event.blockList().remove(b);
                } else {
                    if (b.getType() == Material.TNT) {
                        continue;
                    }
                    b.getLocation().getBlock().setType(Material.AIR);
                }
            }
        }
    }
}
