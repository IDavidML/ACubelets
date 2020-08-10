package me.davidml16.acubelets.commands.points.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
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
            if(!main.getDatabaseHandler().hasName(player)) {
                sender.sendMessage(Utils.translate(
                        main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(Bukkit.getPlayer(player) == null) {
            try {
                UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
                main.getDatabaseHandler().getPlayerLootPoints(uuid, points -> {
                    sender.sendMessage("");
                    sender.sendMessage(Utils.translate(" &6&l" + player + " &ahas &6" + points + " &aPoints."));
                    sender.sendMessage("");
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Player target = Bukkit.getPlayer(player);
            long points = main.getPlayerDataHandler().getData(target).getLootPoints();

            sender.sendMessage("");
            sender.sendMessage(Utils.translate(" &6&l" + player + " &ahas &6" + points + " &aPoints."));
            sender.sendMessage("");
        }

        return true;
    }

}
