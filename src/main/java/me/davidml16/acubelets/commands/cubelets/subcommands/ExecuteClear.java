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

public class ExecuteClear {

    private Main main;
    public ExecuteClear(Main main) {
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
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " clear [player]"));
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
                main.getDatabaseHandler().removeCubelets(uuid);

                String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Clear");
                msg = msg.replaceAll("%player%", player);
                sender.sendMessage(Utils.translate(msg));

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            Player target = Bukkit.getPlayer(player);

            try {
                main.getPlayerDataHandler().getData(target).getCubelets().clear();

                String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Clear");
                msg = msg.replaceAll("%player%", player);
                sender.sendMessage(Utils.translate(msg));

                if (main.getCubeletsGUI().getOpened().containsKey(target.getUniqueId())) main.getCubeletsGUI().reloadPage(target);
                if (main.getCraftingGUI().getOpened().contains(target.getUniqueId())) main.getCraftingGUI().open(target);
                main.getHologramHandler().reloadHolograms(target);

                main.getDatabaseHandler().removeCubelets(target.getUniqueId());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        return true;
    }

}
