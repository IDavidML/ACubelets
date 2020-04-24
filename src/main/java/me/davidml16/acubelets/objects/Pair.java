package me.davidml16.acubelets.objects;

public class Pair {

    private String cubeletType;
    private int page;

    public Pair(String cubeletType, int page) {
        this.cubeletType = cubeletType;
        this.page = page;
    }

    public String getCubeletType() {
        return cubeletType;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "cubeletType='" + cubeletType + '\'' +
                ", page=" + page +
                '}';
    }
}
