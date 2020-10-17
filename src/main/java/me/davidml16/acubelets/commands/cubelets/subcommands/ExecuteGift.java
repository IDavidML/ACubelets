package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class ExecuteGift {

    private Main main;
    public ExecuteGift(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if(!main.isGiftCubelets()) return true;

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " gift [player]"));
            sender.sendMessage("");
            return true;
        }

        String player = args[1];

        if(sender.getName().equalsIgnoreCase(player)) {
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getMessage("Commands.Cubelets.Gift.Yourself")));
            return false;
        }

        try {
            if(!main.getDatabaseHandler().hasName(player)) {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
            main.getGiftGUI().open(((Player) sender), uuid, player, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

}
