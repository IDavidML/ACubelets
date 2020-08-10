package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.PointsAPI;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ExecuteGive {

    private Main main;
    public ExecuteGive(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " give [player] [amount]"));
            sender.sendMessage("");
            return false;
        }

        String player = args[1];

        try {
            if(!main.getDatabaseHandler().hasName(player)) {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(args.length == 2) {
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " give [player] [amount]"));
            return false;
        } else if(args.length == 3) {
            int amount = Integer.parseInt(args[2]);
            if(amount > 0) {
                PointsAPI.give(player, amount);

                String msg = main.getLanguageHandler().getMessage("Commands.Points.Give");
                msg = msg.replaceAll("%amount%", Integer.toString(amount));
                msg = msg.replaceAll("%player%", player);
                sender.sendMessage(Utils.translate(msg));

                return true;
            } else {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                return false;
            }
        }
        return false;
    }

}
