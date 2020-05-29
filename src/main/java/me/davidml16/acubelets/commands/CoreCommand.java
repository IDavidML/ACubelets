package me.davidml16.acubelets.commands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.commands.commands.*;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommand implements CommandExecutor {

    private final Main main;

    private final ExecuteGive executeGive;
    private final ExecuteRemove executeRemove;
    private final ExecuteBox executeBox;
    private final ExecuteType executeType;
    private final ExecuteReload executeReload;
    private final ExecuteSetup executeSetup;
    private final ExecuteInfo executeInfo;

    public CoreCommand(Main main) {
        this.main = main;
        this.executeGive = new ExecuteGive(main);
        this.executeRemove = new ExecuteRemove(main);
        this.executeBox = new ExecuteBox(main);
        this.executeType = new ExecuteType(main);
        this.executeReload = new ExecuteReload(main);
        this.executeSetup = new ExecuteSetup(main);
        this.executeInfo = new ExecuteInfo(main);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) {
            if(sender instanceof Player) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.Balance")
                        .replaceAll("%cubelets_available%", ""+main.getPlayerDataHandler().getData((Player) sender).getCubelets().size()));
                return true;
            } else {
                return sendCommandHelp(sender, label);
            }
        }

        switch (args[0]) {
            case "help":
                return sendCommandHelp(sender, label);
            case "machine":
                return executeBox.executeCommand(sender, label, args);
            case "give":
                return executeGive.executeCommand(sender, label, args);
            case "remove":
                return executeRemove.executeCommand(sender, label, args);
            case "type":
                return executeType.executeCommand(sender, label, args);
            case "setup":
                return executeSetup.executeCommand(sender, label, args);
            case "reload":
                return executeReload.executeCommand(sender, label, args);
            case "info":
                return executeInfo.executeCommand(sender, label, args);
        }

        sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cInvalid argument, use /" + label + " help to see available commands"));
        return false;
    }

    private boolean sendCommandHelp(CommandSender sender, String label) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) return false;
        }

        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " [give/remove] [player] [typeID] [amount]"));
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " info [player]"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " machine [add/remove/edit]"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " type"));
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " setup [typeID]"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/" + label + " reload"));
        sender.sendMessage("");
        return true;
    }

}