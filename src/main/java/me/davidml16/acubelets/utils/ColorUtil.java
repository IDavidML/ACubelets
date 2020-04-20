package me.davidml16.acubelets.utils;

import org.bukkit.ChatColor;

public class ColorUtil {
    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
