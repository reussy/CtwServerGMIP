package net.craftersland.ctw.server.utils;

import com.cryptomorin.xseries.XSound;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Methods to facilitate in-game messages
 *
 * @author Ricardo (reussy)
 */

public class PluginUtil {

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    @Contract("_ -> new")
    public static @NotNull String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Add color to the message given.
     *
     * @param message The message to colorize.
     * @return returns the message colorized.
     */
    public static @NotNull List<String> colorize(@NotNull List<String> message) {
        List<String> colorized = new ArrayList<>();
        message.forEach(line -> {
            line = ChatColor.translateAlternateColorCodes('&', line);
            colorized.add(line);
        });

        return colorized;
    }

    /**
     * Strip the colors in the message.
     *
     * @param message The message to strip.
     * @return returns the message without a color.
     */
    public static String fade(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Build a new text component.
     *
     * @param message The message to show.
     * @param command The command to be set in the chat to the human.
     * @param tooltip The message when the cursor is in the message.
     * @return returns the new text component.
     */
    public static @NotNull TextComponent buildTextComponent(String message, String command, String tooltip) {
        TextComponent textComponent = new TextComponent(colorize(message));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(colorize(tooltip))).create()));
        return textComponent;
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public static void send(Player player, String message) {
        if (message == null || player == null) return;

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        player.sendMessage(colorize(message));
    }

    /**
     * Send a colorized message to the player.
     *
     * @param player  The player related.
     * @param message The message to send.
     */
    public static void send(Player player, List<String> message) {
        if (message == null || player == null) return;

        message.forEach(line -> send(player, line));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, String message) {
        if (message == null || players.isEmpty()) return;

        players.forEach(player -> send(player, message));
    }

    /**
     * Send a colorized message to the player list.
     *
     * @param players The list of player's related.
     * @param message The message to send.
     */
    public static void send(Collection<Player> players, List<String> message) {
        if (message == null || players.isEmpty()) return;

        players.forEach(player -> message.forEach(line -> send(player, line)));
    }

    /**
     * Play a minecraft sound to the player.
     *
     * @param player The player related.
     * @param sound  The sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void play(Player player, String sound, float volume, float pitch) {

        if (sound == null || player == null) return;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Play a minecraft sound to the player list.
     *
     * @param players The collection of player's related.
     * @param sound   The sound to play.
     * @param volume  The volume of the sound.
     * @param pitch   The pitch of the sound.
     */
    public static void play(Collection<Player> players, String sound, float volume, float pitch) {

        if (sound == null) return;

        players.forEach(player -> play(player, sound, volume, pitch));
    }

    /**
     * Play a XSeries Sound parsed to a Minecraft Sound
     *
     * @param player The player related.
     * @param sound  The XSeries Sound to play.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public static void play(Player player, XSound sound, float volume, float pitch) {

        if (sound == null || player == null) return;

        sound.play(player, volume, pitch);
    }

    /**
     * Play a XSeries Sound parsed to a Minecraft Sound
     * This method follows the format SOUND:VOLUME:PITCH
     *
     * @param player The player related.
     * @param format The format of the sound.
     */
    public static void play(Player player, String format) {

        if (format == null || format.isEmpty() || format.equalsIgnoreCase("none")) return;

        String[] split = format.split(":");

        if (split.length != 3) {
            throw new IndexOutOfBoundsException("The format for the sound " + format + " is invalid. The format must be SOUND:VOLUME:PITCH");
        }

        Optional<XSound> xSound = XSound.matchXSound(split[0]);
        float volume = split[1].isEmpty() ? 1 : Float.parseFloat(split[1]);
        float pitch = split[2].isEmpty() ? 1 : Float.parseFloat(split[2]);

        xSound.ifPresent(value -> play(player, value, volume, pitch));
    }
}
