package net.quazar.regionchat.util;

import org.bukkit.ChatColor;

public final class ColorUtils {
    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
