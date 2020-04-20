package me.davidml16.acubelets.data;

public class Reward {

    private String id;
    private String rarity;
    private String command;
    private int chance;

    public Reward(String id, String rarity, String command, int chance) {
        this.id = id;
        this.rarity = rarity;
        this.command = command;
        this.chance = chance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id='" + id + '\'' +
                ", rarity='" + rarity + '\'' +
                ", command='" + command + '\'' +
                ", chance=" + chance +
                '}';
    }

}
