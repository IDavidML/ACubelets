package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecuteSetup {

    private Main main;
    public ExecuteSetup(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
            sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
            return false;
        }

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " setup [typeID]"));
            sender.sendMessage("");
            return true;
        }

        String id = args[1].toLowerCase();
        if(!id.equalsIgnoreCase("crafting")) {
            if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cThis " + label + " type doesn't exists!"));
                return true;
            }

            main.getTypeConfigGUI().open((Player) sender, id);
        } else
            main.getEditCraftingGUI().open((Player) sender);

        return true;
    }

}
