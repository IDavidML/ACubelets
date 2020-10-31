package me.davidml16.acubelets.objects.rewards;

import me.davidml16.acubelets.objects.CommandObject;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandReward extends Reward {

    private List<CommandObject> commands;

    public CommandReward(String id, String name, Rarity rarity, List<CommandObject> commands, ItemStack icon, CubeletType parentCubelet) {
        super(id, name, rarity, icon, parentCubelet);
        this.commands = commands;
    }

    public List<CommandObject> getCommands() {
        return commands;
    }

    public void setCommands(List<CommandObject> commands) {
        this.commands = commands;
    }

    public CommandObject getCommand(String id) {
        for(CommandObject command : getCommands())
            if(command.getId().equalsIgnoreCase(id))
                return command;
        return null;
    }

    public void recreateCommands() {
        int i = 0;
        for(CommandObject commandObject : getCommands()) {
            commandObject.setId("command-" + i);
            i++;
        }
    }

    @Override
    public String toString() {
        return "CommandReward{" +
                super.toString() +
                ", commands=" + commands +
                '}';
    }

}
