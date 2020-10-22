package me.davidml16.acubelets.objects;

public class Pair {

    private String id;
    private int page;

    public Pair(String id, int page) {
        this.id = id;
        this.page = page;
    }

    public String getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "cubeletType='" + id + '\'' +
                ", page=" + page +
                '}';
    }
}
