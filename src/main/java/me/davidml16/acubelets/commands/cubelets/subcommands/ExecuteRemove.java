package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class ExecuteRemove {

    private Main main;
    public ExecuteRemove(Main main) {
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
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [typeID] [amount]"));
            sender.sendMessage("");
            return false;
        }

        String player = args[1];

        String id = args[2];
        if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
            sender.sendMessage(Utils.translate(
                    main.getLanguageHandler().getPrefix() + " &cThis " + label + " type doesn't exists!"));
            return false;
        }

        int amount = 1;

        try {
            if(!main.getDatabaseHandler().hasName(player)) {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        long actualBalance = 0;
        if(Bukkit.getPlayer(player) != null) {
            Profile profile = main.getPlayerDataHandler().getData(Bukkit.getPlayer(player));
            actualBalance = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(id)).count();
        } else {
            try {
                UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
                actualBalance = main.getDatabaseHandler().getCubeletBalance(uuid, id);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        String name = main.getCubeletTypesHandler().getTypeBydId(id).getName();

        if(args.length == 3) {
            if(actualBalance >= 1) {
                CubeletsAPI.removeCubelet(player, id, 1);

                String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Remove.Removed");
                msg = msg.replaceAll("%amount%", Integer.toString(amount));
                msg = msg.replaceAll("%cubelet%",  main.getCubeletTypesHandler().getTypeBydId(id).getName());
                msg = msg.replaceAll("%player%", player);
                sender.sendMessage(Utils.translate(msg));

                return true;
            } else {

                String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Remove.NoBalance");
                msg = msg.replaceAll("%amount%", Integer.toString(amount));
                msg = msg.replaceAll("%cubelet%", Utils.removeColors(name));
                msg = msg.replaceAll("%balance%", Long.toString(actualBalance));
                msg = msg.replaceAll("%player%", player);
                sender.sendMessage(Utils.translate(msg));

                return false;
            }
        } else if(args.length == 4) {
            amount = Integer.parseInt(args[3]);
            if(amount > 0) {
                if(actualBalance >= amount) {
                    CubeletsAPI.removeCubelet(player, id, amount);

                    String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Remove.Removed");
                    msg = msg.replaceAll("%amount%", Integer.toString(amount));
                    msg = msg.replaceAll("%cubelet%",  main.getCubeletTypesHandler().getTypeBydId(id).getName());
                    msg = msg.replaceAll("%player%", player);
                    sender.sendMessage(Utils.translate(msg));

                } else {

                    String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Remove.NoBalance");
                    msg = msg.replaceAll("%amount%", Integer.toString(amount));
                    msg = msg.replaceAll("%cubelet%", Utils.removeColors(name));
                    msg = msg.replaceAll("%balance%", Long.toString(actualBalance));
                    msg = msg.replaceAll("%player%", player);
                    sender.sendMessage(Utils.translate(msg));

                    return false;
                }
                return true;
            } else {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to remove need to be more than 0!"));
                return false;
            }
        }
        return true;
    }

}
