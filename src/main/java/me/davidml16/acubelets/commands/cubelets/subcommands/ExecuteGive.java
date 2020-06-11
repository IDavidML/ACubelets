package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.utils.ColorUtil;
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

        if (args.length == 1 || args.length == 2) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " give [player] [typeID] [amount]"));
            return false;
        }

        String player = args[1];

        String id = args[2];
        if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
            sender.sendMessage(ColorUtil.translate(
                    main.getLanguageHandler().getPrefix() + " &cThis " + label + " type doesn't exists!"));
            return false;
        }

        try {
            if(!main.getDatabaseHandler().hasName(player)) {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(args.length == 3) {
            CubeletsAPI.giveCubelet(player, id, 1);
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                    " &aGived &e1x " + main.getCubeletTypesHandler().getTypeBydId(id).getName() + " &ato &e" + player));
        } else if(args.length == 4) {
            int amount = Integer.parseInt(args[3]);
            if(amount > 0) {
                CubeletsAPI.giveCubelet(player, id, amount);
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                        " &aGived &e" + amount + "x " + main.getCubeletTypesHandler().getTypeBydId(id).getName() + " &ato &e" + player));
                return true;
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                return false;
            }
        }
        return false;
    }

}
