package net.craftersland.ctw.server.utils;

import net.craftersland.ctw.server.CTW;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class Fireworks {
    private final CTW ctw;

    public Fireworks(final CTW ctw) {
        this.ctw = ctw;
    }

    public void spawnRedFirework(final Location l) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                final FireworkEffect.Builder builder = FireworkEffect.builder();
                final FireworkEffect effect = builder.flicker(true).trail(false).with(FireworkEffect.Type.STAR).withColor(Color.fromRGB(247, 3, 27)).withColor(Color.fromRGB(247, 3, 199)).withFade(Color.WHITE).build();
                final Firework f = (Firework) l.getWorld().spawn(l, (Class) Firework.class);
                final FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(effect);
                fm.setPower(0);
                f.setFireworkMeta(fm);
            }
        });
    }

    public void spawnBlueFirework(final Location l) {
        Bukkit.getScheduler().runTask(this.ctw, new Runnable() {
            @Override
            public void run() {
                final FireworkEffect.Builder builder = FireworkEffect.builder();
                final FireworkEffect effect = builder.flicker(true).trail(false).with(FireworkEffect.Type.STAR).withColor(Color.fromRGB(52, 3, 247)).withColor(Color.fromRGB(3, 150, 247)).withFade(Color.WHITE).build();
                final Firework f = (Firework) l.getWorld().spawn(l, (Class) Firework.class);
                final FireworkMeta fm = f.getFireworkMeta();
                fm.addEffect(effect);
                fm.setPower(0);
                f.setFireworkMeta(fm);
            }
        });
    }
}
