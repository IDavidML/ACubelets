package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Matcher;

public class ExecuteInfo {

    private Main main;
    public ExecuteInfo(Main main) {
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
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " info [player]"));
            sender.sendMessage("");
            return true;
        }

        String player = args[1];

        try {

            main.getDatabaseHandler().hasName(player, name -> {

                if(name == null) {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));

                } else {

                    if(Bukkit.getPlayer(player) == null) {

                        try {

                            main.getDatabaseHandler().getPlayerUUID(player, result -> {

                                UUID uuid = UUID.fromString(result);

                                main.getDatabaseHandler().getPlayerLootPoints(uuid, points -> {
                                    sender.sendMessage("");
                                    String msg = main.getLanguageHandler().getMessage("Commands.Points.Info");
                                    msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                                    msg = msg.replaceAll("%amount%", Matcher.quoteReplacement(Long.toString(points)));
                                    sender.sendMessage(Utils.translate(msg));
                                    sender.sendMessage("");
                                });

                            });

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } else {

                        Player target = Bukkit.getPlayer(player);
                        long points = main.getPlayerDataHandler().getData(target).getLootPoints();

                        sender.sendMessage("");
                        String msg = main.getLanguageHandler().getMessage("Commands.Points.Info");
                        msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                        msg = msg.replaceAll("%amount%", Matcher.quoteReplacement(Long.toString(points)));
                        sender.sendMessage(Utils.translate(msg));
                        sender.sendMessage("");

                    }

                }

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;

    }

}
