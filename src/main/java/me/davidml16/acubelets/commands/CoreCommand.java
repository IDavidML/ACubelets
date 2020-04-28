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
    private final ExecuteBox executeBox;
    private final ExecuteType executeType;
    private final ExecuteReload executeReload;
    private final ExecuteSetup executeSetup;

    public CoreCommand(Main main) {
        this.main = main;
        this.executeGive = new ExecuteGive(main);
        this.executeBox = new ExecuteBox(main);
        this.executeType = new ExecuteType(main);
        this.executeReload = new ExecuteReload(main);
        this.executeSetup = new ExecuteSetup(main);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 0) {
            if(sender instanceof Player) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.Balance")
                        .replaceAll("%cubelets_available%", ""+main.getPlayerDataHandler().getData((Player) sender).getCubelets().size()));
                return true;
            } else {
                return sendCommandHelp(sender);
            }
        }

        switch (args[0]) {
            case "help":
                return sendCommandHelp(sender);
            case "machine":
                return executeBox.executeCommand(sender, args);
            case "give":
                return executeGive.executeCommand(sender, args);
            case "type":
                return executeType.executeCommand(sender, args);
            case "setup":
                return executeSetup.executeCommand(sender, args);
            case "reload":
                return executeReload.executeCommand(sender, args);
        }

        sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cInvalid argument, use /cubelets help to see available commands"));
        return false;
    }

    private boolean sendCommandHelp(CommandSender sender) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) return false;
        }

        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/cubelets give [player] [typeID] [amount]"));
        sender.sendMessage(ColorUtil.translate("&7 - &a/cubelets machine [add/remove]"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/cubelets type"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/cubelets setup [typeID]"));
        sender.sendMessage("");
        sender.sendMessage(ColorUtil.translate("&7 - &a/cubelets reload"));
        sender.sendMessage("");
        return true;
    }

}