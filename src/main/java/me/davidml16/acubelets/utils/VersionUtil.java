package me.davidml16.acubelets.utils;

import org.bukkit.Bukkit;

public class VersionUtil {

    private static final int VERSION = Integer.parseInt(getMajorVersion(Bukkit.getVersion()).substring(2));

    private static final boolean ISFLAT = supports(13);

    public static boolean isNewVersion() {
        return ISFLAT;
    }

    public static boolean isOneEight() {
        return !supports(9);
    }

    public static double getVersion() {
        return VERSION;
    }

    public static boolean supports(int version) {
        return VERSION >= version;
    }

    public static boolean between(int version1, int version2) {
        return VERSION >= version1 && VERSION <= version2;
    }

    public static String getMajorVersion(String version) {
        int index = version.lastIndexOf("MC:");
        if (index != -1) {
            version = version.substring(index + 4, version.length() - 1);
        } else if (version.endsWith("SNAPSHOT")) {
            index = version.indexOf('-');
            version = version.substring(0, index);
        }

        int lastDot = version.lastIndexOf('.');
        if (version.indexOf('.') != lastDot) version = version.substring(0, lastDot);

        return version;
    }

}
