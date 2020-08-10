package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.PointsAPI;
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

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [amount]"));
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

        if(Bukkit.getPlayer(player) != null) {
            Profile profile = main.getPlayerDataHandler().getData(Bukkit.getPlayer(player));
            long actualBalance = profile.getLootPoints();

            if(args.length == 2) {
                sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [amount]"));
                return false;
            } else if(args.length == 3) {
                int amount = Integer.parseInt(args[2]);
                if(amount > 0) {
                    if(actualBalance >= amount) {
                        PointsAPI.remove(player, amount);

                        String msg = main.getLanguageHandler().getMessage("Commands.Points.Remove.Removed");
                        msg = msg.replaceAll("%amount%", Integer.toString(amount));
                        msg = msg.replaceAll("%player%", player);
                        sender.sendMessage(Utils.translate(msg));

                        return true;
                    } else {

                        String msg = main.getLanguageHandler().getMessage("Commands.Points.Remove.NoBalance");
                        msg = msg.replaceAll("%amount%", Integer.toString(amount));
                        msg = msg.replaceAll("%balance%", Long.toString(actualBalance));
                        msg = msg.replaceAll("%player%", player);
                        sender.sendMessage(Utils.translate(msg));

                        return true;
                    }
                } else {
                    sender.sendMessage(Utils.translate(
                            main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                    return false;
                }
            }
        } else {
            try {
                UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
                main.getDatabaseHandler().getPlayerLootPoints(uuid, actualBalance -> {
                    if(args.length == 2) {
                        sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " remove [player] [amount]"));
                    } else if(args.length == 3) {
                        int amount = Integer.parseInt(args[2]);
                        if(amount > 0) {
                            if(actualBalance >= amount) {
                                PointsAPI.remove(player, amount);

                                String msg = main.getLanguageHandler().getMessage("Commands.Points.Remove.Removed");
                                msg = msg.replaceAll("%amount%", Integer.toString(amount));
                                msg = msg.replaceAll("%player%", player);
                                sender.sendMessage(Utils.translate(msg));

                            } else {

                                String msg = main.getLanguageHandler().getMessage("Commands.Points.Remove.NoBalance");
                                msg = msg.replaceAll("%amount%", Integer.toString(amount));
                                msg = msg.replaceAll("%balance%", Long.toString(actualBalance));
                                msg = msg.replaceAll("%player%", player);
                                sender.sendMessage(Utils.translate(msg));

                            }
                        } else {
                            sender.sendMessage(Utils.translate(
                                    main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                        }
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return false;
    }

}
