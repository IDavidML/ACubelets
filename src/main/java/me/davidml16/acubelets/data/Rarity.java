package me.davidml16.acubelets.data;

public class Rarity implements Comparable<Rarity> {

    private String id;
    private String name;
    private int chance;

    public Rarity(String id, String name, int chance) {
        this.id = id;
        this.name = name;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    @Override
    public String toString() {
        return "Rarity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", chance='" + chance + '\'' +
                '}';
    }

    @Override
    public int compareTo(Rarity otherRarity) {
        return chance - otherRarity.getChance();
    }
}
