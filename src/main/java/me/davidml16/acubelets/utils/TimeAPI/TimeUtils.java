package me.davidml16.acubelets.utils.TimeAPI;

import me.davidml16.acubelets.Main;

public class TimeUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;

    /**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                return res.append(temp).append(" ").append(temp > 1 ? Main.get().getLanguageHandler().getMessage("Times.Days") : Main.get().getLanguageHandler().getMessage("Times.Day")).toString();
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                return res.append(temp).append(" ").append(temp > 1 ? Main.get().getLanguageHandler().getMessage("Times.Hours") : Main.get().getLanguageHandler().getMessage("Times.Hour")).toString();
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                return res.append(temp).append(" ").append(temp > 1 ? Main.get().getLanguageHandler().getMessage("Times.Minutes") : Main.get().getLanguageHandler().getMessage("Times.Minute")).toString();
            }

            temp = duration / ONE_SECOND;
            return res.append(temp).append(" ").append(temp > 1 ? Main.get().getLanguageHandler().getMessage("Times.Seconds") : Main.get().getLanguageHandler().getMessage("Times.Second")).toString();
        } else {
            return "0 " + Main.get().getLanguageHandler().getMessage("Times.Second");
        }
    }
}