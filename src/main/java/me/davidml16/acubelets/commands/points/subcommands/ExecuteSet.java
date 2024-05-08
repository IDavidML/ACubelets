package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.PointsAPI;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ExecuteSet {

    private Main main;
    public ExecuteSet(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                if(!main.getLanguageHandler().isEmptyMessage("Commands.NoPerms"))
                    sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " set [player] [amount]"));
            sender.sendMessage("");
            return false;
        }

        String player = args[1];

        try {

            main.getDatabaseHandler().hasName(player, name -> {

                if(name == null) {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));

                } else {

                    if(args.length == 2) {

                        sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " set [player] [amount]"));

                    } else if(args.length == 3) {

                        int amount = Integer.parseInt(args[2]);

                        if(amount >= 0) {
                            PointsAPI.set(player, amount);

                            String msg = main.getLanguageHandler().getMessage("Commands.Points.Set");
                            msg = msg.replaceAll("%amount%", Integer.toString(amount));
                            msg = msg.replaceAll("%player%", name);
                            sender.sendMessage(Utils.translate(msg));

                        } else {

                            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cAmount to set need to be more or equals to 0!"));

                        }
                    }

                }

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

}
