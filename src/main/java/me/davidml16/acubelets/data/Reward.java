package me.davidml16.acubelets.data;

public class Reward {

    private String id;
    private String name;
    private Rarity rarity;
    private String command;

    public Reward(String id, String name, Rarity rarity, String command) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Rarity getRarity() { return rarity; }

    public void setRarity(Rarity rarity) { this.rarity = rarity; }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rarity='" + rarity + '\'' +
                ", command='" + command + '\'' +
                '}';
    }

}
