package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.LootHistoryMenu;
import me.davidml16.acubelets.menus.options.OptionsMainMenu;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecuteLootHistory {

    private Main main;
    public ExecuteLootHistory(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate("&cThe commands only can be use by players!"));
            return true;
        }

        LootHistoryMenu lootHistoryMenu = new LootHistoryMenu(main, (Player) sender);
        lootHistoryMenu.setAttribute(Menu.AttrType.OPENED_EXTERNALLY_ATTR, new Boolean(true));
        lootHistoryMenu.open();

        return true;

    }

}
