package net.craftersland.ctw.server.game;

import java.util.HashMap;
import java.util.UUID;

public class PlayerProjectile {

    private final HashMap<UUID, Integer> projectile = new HashMap<>();

    public PlayerProjectile(int distance, UUID damaged) {
        projectile.put(damaged, distance);

    }

    public void setProjectile(UUID damaged, int distance) {

        if (this.projectile.containsKey(damaged)) {

            projectile.remove(damaged);

        }
        projectile.put(damaged, distance);
    }

    public int getProjectile(UUID damaged) {

        return projectile.get(damaged);

    }
}
