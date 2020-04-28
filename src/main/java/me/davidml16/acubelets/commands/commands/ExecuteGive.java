package me.davidml16.acubelets.commands.commands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletsAPI;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ExecuteGive {

    private Main main;
    public ExecuteGive(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String[] args) {

        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if (args.length == 1 || args.length == 2) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets add [player] [typeID] [amount]"));
            return false;
        }

        String player = args[1];
        Player target = Bukkit.getPlayer(player);

        if(target == null || !target.isOnline()) {
            sender.sendMessage(ColorUtil.translate(
                    main.getLanguageHandler().getPrefix() + " &cThis player is not online!"));
            return false;
        }

        String id = args[2];
        if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
            sender.sendMessage(ColorUtil.translate(
                    main.getLanguageHandler().getPrefix() + " &cThis cubelet type doesn't exists!"));
            return false;
        }

        if(args.length == 3) {
            CubeletsAPI.giveCubelet(target, id, 1);
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                    " &aGived &e1x " + main.getCubeletTypesHandler().getTypeBydId(id).getName() + " &ato &e" + target.getName()));
        } else if(args.length == 4) {
            int amount = Integer.parseInt(args[3]);
            if(amount > 0) {
                CubeletsAPI.giveCubelet(target, id, amount);
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() +
                        " &aGived &e" + amount + "x " + main.getCubeletTypesHandler().getTypeBydId(id).getName() + " &ato &e" + target.getName()));
                return true;
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                return false;
            }
        }
        return false;
    }

}
