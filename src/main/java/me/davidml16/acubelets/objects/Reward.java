package me.davidml16.acubelets.objects;

public class Reward {

    private String id;
    private String name;
    private Rarity rarity;
    private String command;
    private CustomIcon icon;

    public Reward(String id, String name, Rarity rarity, String command, CustomIcon icon) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.command = command;
        this.icon = icon;
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

    public CustomIcon getIcon() {
        return icon;
    }

    public void setIcon(CustomIcon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rarity=" + rarity +
                ", command='" + command + '\'' +
                ", icon=" + icon +
                '}';
    }

}
