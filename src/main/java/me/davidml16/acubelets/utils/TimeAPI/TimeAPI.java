package me.davidml16.acubelets.utils.TimeAPI;

import java.util.concurrent.TimeUnit;

public class TimeAPI {
    private static final long DAYS_IN_WEEK = 7;
    private static final long DAYS_IN_MONTH = 30;
    private static final long DAYS_IN_YEAR = 365;

    private String ogTime;
    private long millis;

    public TimeAPI(String time) {
        this.ogTime = time;
        reparse(time);
    }

    public TimeAPI(long millis) {
        this.millis = millis;
    }

    public void reparse(String time) {
        long temp = 0;

        TimeScanner scanner = new TimeScanner(time
                .replace(" ", "")
                .replace("and", "")
                .replace(",", "")
                .toLowerCase());

        long next;
        while(scanner.hasNext()) {
            next = scanner.nextLong();
            switch(scanner.nextString()) {
                case "ms":
                case "millis":
                case "milliseconds":
                    temp += next;
                    break;
                case "s":
                case "sec":
                case "secs":
                case "second":
                case "seconds":
                    temp += TimeUnit.SECONDS.toMillis(next);
                    break;
                case "m":
                case "min":
                case "mins":
                case "minute":
                case "minutes":
                    temp += TimeUnit.MINUTES.toMillis(next);
                    break;
                case "h":
                case "hr":
                case "hrs":
                case "hour":
                case "hours":
                    temp += TimeUnit.HOURS.toMillis(next);
                    break;
                case "d":
                case "dy":
                case "dys":
                case "day":
                case "days":
                    temp += TimeUnit.DAYS.toMillis(next);
                    break;
                case "w":
                case "week":
                case "weeks":
                    temp += TimeUnit.DAYS.toMillis(next * DAYS_IN_WEEK);
                    break;
                case "mo":
                case "mon":
                case "mnth":
                case "month":
                case "months":
                    temp += TimeUnit.DAYS.toMillis(next * DAYS_IN_MONTH);
                    break;
                case "y":
                case "yr":
                case "yrs":
                case "year":
                case "years":
                    temp += TimeUnit.DAYS.toMillis(next * DAYS_IN_YEAR);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        this.millis = temp;
    }

    public String getOgTime() {
        return ogTime;
    }

    public long getNanoseconds() {
        return TimeUnit.MILLISECONDS.toNanos(millis);
    }

    public long getMicroseconds() {
        return TimeUnit.MILLISECONDS.toMicros(millis);
    }

    public long getMilliseconds() {
        return millis;
    }

    public long getSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public long getMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(millis);
    }

    public long getHours() {
        return TimeUnit.MILLISECONDS.toHours(millis);
    }

    public long getDays() {
        return TimeUnit.MILLISECONDS.toDays(millis);
    }

    public long getWeeks() {
        return getDays() / DAYS_IN_WEEK;
    }

    public long getMonths() {
        return getDays() / DAYS_IN_MONTH;
    }

    public long getYears() {
        return getDays() / DAYS_IN_YEAR;
    }

    public long getTicks() {
        return millis / 20000;
    }

}
