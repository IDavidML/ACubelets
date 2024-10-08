package me.davidml16.acubelets.utils.Regex;

import org.jetbrains.annotations.NotNull;

public class TimeoutCharSequence implements CharSequence {

    private final CharSequence chars;
    private final long timeout;
    private final long maxTime;

    public TimeoutCharSequence(@NotNull CharSequence chars, long timeout) {
        this.chars = chars;
        this.timeout = timeout;
        this.maxTime = (System.currentTimeMillis() + timeout);
    }

    @Override
    public char charAt(int index) {
        if (System.currentTimeMillis() > this.maxTime) {
            throw new MatcherTimeoutException(this.chars, this.timeout);
        }
        return this.chars.charAt(index);
    }

    @Override
    public int length() {
        return this.chars.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new TimeoutCharSequence(this.chars.subSequence(start, end), this.timeout);
    }

    @Override
    public String toString() {
        return this.chars.toString();
    }
}