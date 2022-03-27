package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.rewards.RewardsPreviewMenu;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecutePreview {

    private Main main;
    public ExecutePreview(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " preview [typeID]"));
            sender.sendMessage("");
            return true;
        }

        String id = args[1].toLowerCase();
        if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis " + label + " type doesn't exists!"));
            return true;
        }

        RewardsPreviewMenu rewardsPreviewMenu = new RewardsPreviewMenu(Main.get(), (Player) sender);
        rewardsPreviewMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, id);
        rewardsPreviewMenu.setAttribute(Menu.AttrType.OPENED_EXTERNALLY_ATTR, new Boolean(true));
        rewardsPreviewMenu.open();

        return true;
    }

}
