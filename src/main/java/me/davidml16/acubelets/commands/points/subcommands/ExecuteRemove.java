package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.PointsAPI;
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

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [amount]"));
            sender.sendMessage("");
            return false;
        }

        String player = args[1];

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
            actualBalance = profile.getLootPoints();
        } else {
            try {
                UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
                actualBalance = main.getDatabaseHandler().getPlayerLootPoints(uuid);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(args.length == 2) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [amount]"));
            return false;
        } else if(args.length == 3) {
            int amount = Integer.parseInt(args[2]);
            if(amount > 0) {
                if(actualBalance >= amount) {
                    PointsAPI.remove(player, amount);
                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                            " &aRemoved &e" + amount + "x Points &afrom &e" + player));
                    return true;
                } else {
                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                            " &6" + player + " &cdoes not have &6" + amount + "x Points&c. Currently balance: &6" + actualBalance));
                    return true;
                }
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                return false;
            }
        }
        return false;
    }

}
