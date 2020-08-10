package me.davidml16.acubelets.utils;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

    public static String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String removeColors(String msg) {
        return msg == null ? null : STRIP_COLOR_PATTERN.matcher(msg).replaceAll("");
    }

    private static Map<ChatColor, ColorSet<Integer, Integer, Integer>> colorMap = new HashMap<ChatColor, ColorSet<Integer, Integer, Integer>>();

    static {
        colorMap.put(ChatColor.BLACK, new ColorSet<Integer, Integer, Integer>(0, 0, 0));
        colorMap.put(ChatColor.DARK_BLUE, new ColorSet<Integer, Integer, Integer>(0, 0, 170));
        colorMap.put(ChatColor.DARK_GREEN, new ColorSet<Integer, Integer, Integer>(0, 170, 0));
        colorMap.put(ChatColor.DARK_AQUA, new ColorSet<Integer, Integer, Integer>(0, 170, 170));
        colorMap.put(ChatColor.DARK_RED, new ColorSet<Integer, Integer, Integer>(170, 0, 0));
        colorMap.put(ChatColor.DARK_PURPLE, new ColorSet<Integer, Integer, Integer>(170, 0, 170));
        colorMap.put(ChatColor.GOLD, new ColorSet<Integer, Integer, Integer>(255, 170, 0));
        colorMap.put(ChatColor.GRAY, new ColorSet<Integer, Integer, Integer>(170, 170, 170));
        colorMap.put(ChatColor.DARK_GRAY, new ColorSet<Integer, Integer, Integer>(85, 85, 85));
        colorMap.put(ChatColor.BLUE, new ColorSet<Integer, Integer, Integer>(85, 85, 255));
        colorMap.put(ChatColor.GREEN, new ColorSet<Integer, Integer, Integer>(85, 255, 85));
        colorMap.put(ChatColor.AQUA, new ColorSet<Integer, Integer, Integer>(85, 255, 255));
        colorMap.put(ChatColor.RED, new ColorSet<Integer, Integer, Integer>(255, 85, 85));
        colorMap.put(ChatColor.LIGHT_PURPLE, new ColorSet<Integer, Integer, Integer>(255, 85, 255));
        colorMap.put(ChatColor.YELLOW, new ColorSet<Integer, Integer, Integer>(255, 255, 85));
        colorMap.put(ChatColor.WHITE, new ColorSet<Integer, Integer, Integer>(255, 255, 255));
    }

    public static class ColorSet<I extends Number, I1 extends Number, I2 extends Number> {
        Integer red = 0;
        Integer green = 0;
        Integer blue = 0;

        ColorSet(Integer red, Integer green, Integer blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public Integer getRed() {
            return red;
        }

        public Integer getGreen() {
            return green;
        }

        public Integer getBlue() {
            return blue;
        }

    }

    public static ChatColor fromRGB(int r, int g, int b) {
        TreeMap<Integer, ChatColor> closest = new TreeMap<Integer, ChatColor>();
        colorMap.forEach((color, set) -> {
            int red = Math.abs(r - set.getRed());
            int green = Math.abs(g - set.getGreen());
            int blue = Math.abs(b - set.getBlue());
            closest.put(red + green + blue, color);
        });
        return closest.firstEntry().getValue();
    }

    public static ChatColor getColorByText(String text) {
        for (ChatColor color : colorMap.keySet())
            if (text.contains("&" + color.getChar())) return color;
        return ChatColor.WHITE;
    }

    public static ColorSet<Integer, Integer, Integer> getRGBbyColor(ChatColor color) {
        return colorMap.get(color);
    }

}
