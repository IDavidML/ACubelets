package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
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

                                sender.sendMessage("");

                                main.getDatabaseHandler().getCubelets(uuid).thenAccept(list -> {

                                    if (list.size() == 0) {

                                        String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.NotHave");
                                        msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                                        sender.sendMessage(Utils.translate(msg));

                                    } else {

                                        String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.Have.Header");
                                        msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                                        sender.sendMessage(Utils.translate(msg));

                                        for (CubeletType type : main.getCubeletTypesHandler().getTypes().values()) {
                                            long amount = list.stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type.getId())).count();
                                            if (amount > 0) {
                                                String msgList = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.Have.List");
                                                msgList = msgList.replaceAll("%player%", Matcher.quoteReplacement(name));
                                                msgList = msgList.replaceAll("%cubelet%", Matcher.quoteReplacement(type.getName()));
                                                msgList = msgList.replaceAll("%amount%", Matcher.quoteReplacement(name));
                                                sender.sendMessage(Utils.translate(msgList));
                                            }
                                        }

                                        sender.sendMessage("");

                                    }

                                });

                            });

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } else {

                        Player target = Bukkit.getPlayer(player);

                        sender.sendMessage("");

                        List<Cubelet> list = main.getPlayerDataHandler().getData(target).getCubelets();

                        if (list.size() == 0) {

                            String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.NotHave");
                            msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                            sender.sendMessage(Utils.translate(msg));

                        } else {

                            String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.Have.Header");
                            msg = msg.replaceAll("%player%", Matcher.quoteReplacement(name));
                            sender.sendMessage(Utils.translate(msg));

                            for (CubeletType type : main.getCubeletTypesHandler().getTypes().values()) {
                                long amount = list.stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type.getId())).count();
                                if (amount > 0) {
                                    String msgList = main.getLanguageHandler().getMessage("Commands.Cubelets.Info.Have.List");
                                    msgList = msgList.replaceAll("%player%", Matcher.quoteReplacement(name));
                                    msgList = msgList.replaceAll("%cubelet%", Matcher.quoteReplacement(type.getName()));
                                    msgList = msgList.replaceAll("%amount%", Matcher.quoteReplacement(name));
                                    sender.sendMessage(Utils.translate(msgList));
                                }
                            }

                            sender.sendMessage("");

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
