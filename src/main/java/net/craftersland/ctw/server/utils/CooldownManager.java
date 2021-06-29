package net.craftersland.ctw.server.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    public static final int DEFAULT_COOLDOWN = 30;
    private final Map<UUID, Integer> red = new HashMap<>();
    private final Map<UUID, Integer> pink = new HashMap<>();
    private final Map<UUID, Integer> cyan = new HashMap<>();
    private final Map<UUID, Integer> blue = new HashMap<>();


    public Integer getCooldown(UUID player, String color) {

        if (color.equals("red")) {
            return red.getOrDefault(player, 0);
        }
        if (color.equals("pink")) {

            return pink.getOrDefault(player, 0);
        }
        if (color.equals("cyan")) {

            return cyan.getOrDefault(player, 0);
        }
        if (color.equals("blue")) {

            return blue.getOrDefault(player, 0);
        }

        return null;
    }

    public void setCooldown(UUID player, int time, String color) {

        if (color.equals("red")) {
            if (time < 1) {
                red.remove(player);
            } else {
                red.put(player, time);
            }
        }
        if (color.equals("pink")) {

            if (time < 1) {
                pink.remove(player);
            } else {
                pink.put(player, time);
            }
        }
        if (color.equals("cyan")) {

            if (time < 1) {
                cyan.remove(player);
            } else {
                cyan.put(player, time);
            }
        }
        if (color.equals("blue")) {
            if (time < 1) {
                blue.remove(player);
            } else {
                blue.put(player, time);
            }
        }
    }
}
