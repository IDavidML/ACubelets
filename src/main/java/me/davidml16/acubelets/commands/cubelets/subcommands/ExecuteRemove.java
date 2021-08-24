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

            main.getDatabaseHandler().hasName(player, exists -> {

                if(!exists) {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));

                } else {

                    if(Bukkit.getPlayer(player) != null) {

                        Profile profile = main.getPlayerDataHandler().getData(Bukkit.getPlayer(player));
                        long actualBalance = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(id)).count();

                        execute(args, player, sender, id, actualBalance, amount);

                    } else {

                        try {

                            main.getDatabaseHandler().getPlayerUUID(player, result -> {

                                UUID uuid = UUID.fromString(result);

                                try {

                                    main.getDatabaseHandler().getCubeletBalance(uuid, id, balance -> {

                                        execute(args, player, sender, id, balance, amount);

                                    });

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            });

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }

                }

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

    private boolean execute(String[] args, String player, CommandSender sender, String id, long actualBalance, int amount) {

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
