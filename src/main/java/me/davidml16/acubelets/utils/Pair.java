package me.davidml16.acubelets.utils;

public class Pair {

    private String parkour;
    private int page;

    public Pair(String parkour, int page) {
        this.parkour = parkour;
        this.page = page;
    }

    public String getParkour() {
        return parkour;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "parkour='" + parkour + '\'' +
                ", page=" + page +
                '}';
    }
}
