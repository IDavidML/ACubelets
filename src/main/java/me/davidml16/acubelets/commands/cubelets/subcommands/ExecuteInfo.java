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

                                        sender.sendMessage(Utils.translate(" &6&l" + name + " &cnot have any cubelets:"));

                                    } else {

                                        sender.sendMessage(Utils.translate(" &6&l" + name + " &ahas the following cubelets:"));

                                        for (CubeletType type : main.getCubeletTypesHandler().getTypes().values()) {
                                            long amount = list.stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type.getId())).count();
                                            if (amount > 0)
                                                sender.sendMessage(Utils.translate("  &7- " + type.getName() + " &7x" + amount));
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

                            sender.sendMessage(Utils.translate(" &6&l" + name + " &cnot have any cubelets:"));

                        } else {

                            sender.sendMessage(Utils.translate(" &6&l" + name + " &ahas the following cubelets:"));

                            for (CubeletType type : main.getCubeletTypesHandler().getTypes().values()) {
                                long amount = list.stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type.getId())).count();
                                if (amount > 0)
                                    sender.sendMessage(Utils.translate("  &7- " + type.getName() + " &7x" + amount));
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
