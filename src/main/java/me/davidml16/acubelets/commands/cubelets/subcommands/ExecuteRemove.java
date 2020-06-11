package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.ColorUtil;
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
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [typeID] [amount]"));
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
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                        " &aRemoved &e1x " + name + " &afrom &e" + player));
                return true;
            } else {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                        " &6" + player + " &cdoes not have &61x " + ColorUtil.removeColors(name) + "&c."));
                return false;
            }
        } else if(args.length == 4) {
            int amount = Integer.parseInt(args[3]);
            if(amount > 0) {
                if(actualBalance >= amount) {
                    CubeletsAPI.removeCubelet(player, id, amount);
                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                            " &aRemoved &e" + amount + "x " + main.getCubeletTypesHandler().getTypeBydId(id).getName() + " &afrom &e" + player));
                } else {
                    if(actualBalance > 0)
                        sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                            " &6" + player + " &cdoes not have &6" + amount + "x " + ColorUtil.removeColors(name) + "&c. Currently balance: &6" + actualBalance));
                    else if(actualBalance == 0)
                        sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                                " &6" + player + " &cdoes not have &6" + amount + "x " + ColorUtil.removeColors(name) + "&c."));
                    return false;
                }
                return true;
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to remove need to be more than 0!"));
                return false;
            }
        }
        return false;
    }

}
