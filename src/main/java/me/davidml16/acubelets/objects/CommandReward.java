package me.davidml16.acubelets.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandReward extends Reward {

    private List<String> commands;

    public CommandReward(String id, String name, Rarity rarity, List<String> commands, ItemStack icon) {
        super(id, name, rarity, icon);
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return "CommandReward{" +
                super.toString() +
                ", commands=" + commands +
                '}';
    }
}
