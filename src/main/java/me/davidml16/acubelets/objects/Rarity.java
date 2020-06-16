package me.davidml16.acubelets.objects;

public class Rarity implements Comparable<Rarity> {

    private String id;
    private String name;
    private double chance;
    private String duplicatePointsRange;

    public Rarity(String id, String name, double chance, String duplicatePointsRange) {
        this.id = id;
        this.name = name;
        this.chance = chance;
        this.duplicatePointsRange = duplicatePointsRange;
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

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public String getDuplicatePointsRange() { return duplicatePointsRange; }

    public void setDuplicatePointsRange(String duplicatePointsRange) { this.duplicatePointsRange = duplicatePointsRange; }

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
        return (int) (chance - otherRarity.getChance());
    }
}
