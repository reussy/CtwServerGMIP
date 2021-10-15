package net.craftersland.ctw.server.utils;

import de.slikey.effectlib.effect.BleedEffect;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.effect.TraceEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import net.craftersland.ctw.server.CTW;
import net.craftersland.ctw.server.game.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectUtils {
    private CTW ctw;
    private Map<Player, TraceEffect> vipEffects;

    public EffectUtils(final CTW ctw) {
        this.ctw = ctw;
        this.vipEffects = new HashMap<Player, TraceEffect>();
    }

    public void sendWoolLostEffect(final Player p) {
        final Location l = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 0.5, p.getLocation().getZ(), 1.0f, -90.0f);
        final CylinderEffect effect = new CylinderEffect(this.ctw.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(l));
        effect.particle = ParticleEffect.REDSTONE;
        effect.duration = 40;
        effect.start();
        ctw.getSoundHandler().broadcastDragonWings();

    }

    public void woolPlacedEffect(final Location l) {
        final CylinderEffect effect = new CylinderEffect(this.ctw.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(l));
        effect.particle = ParticleEffect.FIREWORKS_SPARK;
        effect.duration = 40;
        effect.start();
    }

    public void playDamageEffect(final Player p) {
        final Location l = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 0.5, p.getLocation().getZ(), 0.0f, -90.0f);
        final BleedEffect effect = new BleedEffect(this.ctw.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(l));
        effect.iterations = 1;
        effect.start();
    }

    public void sendTextParticles(final TeamHandler.Teams victoryTeam) {
        final TextEffect effect = new TextEffect(this.ctw.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(this.ctw.getMapConfigHandler().redWonParticle));
        if (victoryTeam == TeamHandler.Teams.RED) {
            effect.text = this.ctw.getLanguageHandler().getMessage("Words.RedTeamWon");
        } else if (victoryTeam == TeamHandler.Teams.BLUE) {
            effect.text = this.ctw.getLanguageHandler().getMessage("Words.BlueTeamWon");
        }
        effect.particle = ParticleEffect.FLAME;
        effect.period = 20;
        effect.start();
        final TextEffect effect2 = new TextEffect(this.ctw.getEffectManager());
        effect2.setDynamicOrigin(new DynamicLocation(this.ctw.getMapConfigHandler().blueWonParticle));
        if (victoryTeam == TeamHandler.Teams.RED) {
            effect2.text = this.ctw.getLanguageHandler().getMessage("Words.RedTeamWon");
        } else if (victoryTeam == TeamHandler.Teams.BLUE) {
            effect2.text = this.ctw.getLanguageHandler().getMessage("Words.BlueTeamWon");
        }
        effect2.particle = ParticleEffect.FLAME;
        effect2.period = 20;
        effect2.start();
    }

    public void startVipParticles(final Player p) {

        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) this.ctw, (Runnable) new Runnable() {
            @Override
            public void run() {
                final DynamicLocation loc = new DynamicLocation((Entity) p);
                loc.addOffset(new Vector(0.0, -1.0, 0.0));
                final TraceEffect effect = new TraceEffect(EffectUtils.this.ctw.getEffectManager());
                effect.setDynamicOrigin(loc);
                effect.setDynamicTarget(loc);
                effect.particle = ParticleEffect.REDSTONE;

                if (EffectUtils.this.ctw.getTeamHandler().isBlueTeam(p)) {
                    effect.color = Color.fromRGB(85, 85, 255);
                } else if (EffectUtils.this.ctw.getTeamHandler().isRedTeam(p)) {
                    effect.color = Color.fromRGB(255, 85, 85);
                }
                effect.period = 3;
                effect.iterations = 200;
                effect.maxWayPoints = 5;
                effect.start();
                EffectUtils.this.vipEffects.put(p, effect);
            }
        }, 10L);
    }

    public void checkForVipEffects() {
        if (!this.vipEffects.isEmpty()) {
            final List<Player> vipEffectsCopy = new ArrayList<Player>(this.vipEffects.keySet());
            for (final Player p : vipEffectsCopy) {
                if (p != null && p.isOnline()) {
                    final TraceEffect effect = this.vipEffects.get(p);
                    if (effect == null || !effect.isDone()) {
                        continue;
                    }
                    this.startVipParticles(p);
                }
            }
        }
    }

    public void removeEffectsOnDisconnect(final Player p) {
        if (this.vipEffects.containsKey(p)) {
            final TraceEffect effect = this.vipEffects.get(p);
            if (effect != null) {
                effect.cancel();
            }
            this.vipEffects.remove(p);
        }
    }

    public void addArrowTrail(final Arrow ar, final Player p) {
        final TraceEffect effect = new TraceEffect(this.ctw.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation((Entity) ar));
        effect.setDynamicTarget(new DynamicLocation((Entity) ar));
        effect.particle = ParticleEffect.REDSTONE;
        if (this.ctw.getTeamHandler().isBlueTeam(p)) {
            effect.color = Color.fromRGB(85, 85, 255);
        } else if (this.ctw.getTeamHandler().isRedTeam(p)) {
            effect.color = Color.fromRGB(255, 85, 85);
        }
        effect.iterations = 100;
        effect.start();
    }
}
