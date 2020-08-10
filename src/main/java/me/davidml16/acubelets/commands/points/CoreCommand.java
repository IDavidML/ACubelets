package me.davidml16.acubelets.commands.points;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.commands.points.subcommands.*;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoreCommand extends Command {

    private final Main main = Main.get();

    private final ExecuteGive executeGive = new ExecuteGive(main);
    private final ExecuteRemove executeRemove = new ExecuteRemove(main);
    private final ExecuteSet executeSet = new ExecuteSet(main);
    private final ExecuteInfo executeInfo = new ExecuteInfo(main);

    public CoreCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(args.length == 0) {
            if(sender instanceof Player) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.Balance.Points")
                        .replaceAll("%points_available%", ""+main.getPlayerDataHandler().getData((Player) sender).getLootPoints()));
                return true;
            } else {
                return sendCommandHelp(sender, label);
            }
        }

        switch (args[0]) {
            case "help":
                return sendCommandHelp(sender, label);
            case "give":
                return executeGive.executeCommand(sender, label, args);
            case "remove":
                return executeRemove.executeCommand(sender, label, args);
            case "set":
                return executeSet.executeCommand(sender, label, args);
            case "info":
                return executeInfo.executeCommand(sender, label, args);
        }

        sender.sendMessage("");
        sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cInvalid argument, use /" + label + " help to see available commands"));
        sender.sendMessage("");
        return false;
    }

    private boolean sendCommandHelp(CommandSender sender, String label) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) return false;
        }

        sender.sendMessage("");
        sender.sendMessage(Utils.translate("&7 - &a/" + label + " give [player] [amount]"));
        sender.sendMessage(Utils.translate("&7 - &a/" + label + " remove [player] [amount]"));
        sender.sendMessage(Utils.translate("&7 - &a/" + label + " set [player] [amount]"));
        sender.sendMessage(Utils.translate("&7 - &a/" + label + " info [player]"));
        sender.sendMessage("");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        Player p = (Player) sender;

        List<String> list = new ArrayList<String>();
        List<String> auto = new ArrayList<String>();

        if (args.length == 1) {
            if (main.playerHasPermission(p, "acubelets.admin")) {
                list.add("give");
                list.add("remove");
                list.add("set");
                list.add("info");
            }
        }

        if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("remove")
                || args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    for (Player target : main.getServer().getOnlinePlayers()) {
                        list.add(target.getName());
                    }
                }
            }
        }

        for (String s : list) {
            if (s.startsWith(args[args.length - 1])) {
                auto.add(s);
            }
        }

        return auto.isEmpty() ? list : auto;
    }

}