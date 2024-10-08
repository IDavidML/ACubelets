package me.davidml16.acubelets.commands.cubelets;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.commands.cubelets.subcommands.*;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CoreCommand extends Command {

    private final Main main = Main.get();

    private final ExecuteGive executeGive = new ExecuteGive(main);
    private final ExecuteRemove executeRemove = new ExecuteRemove(main);
    private final ExecuteBox executeBox = new ExecuteBox(main);
    private final ExecuteType executeType = new ExecuteType(main);
    private final ExecuteReload executeReload = new ExecuteReload(main);
    private final ExecuteSetup executeSetup = new ExecuteSetup(main);
    private final ExecuteOptions executeOptions = new ExecuteOptions(main);
    private final ExecuteInfo executeInfo = new ExecuteInfo(main);
    private final ExecuteClear executeClear = new ExecuteClear(main);
    private final ExecuteGift executeGift = new ExecuteGift(main);
    private final ExecuteGiveKey executeGiveKey = new ExecuteGiveKey(main);
    private final ExecutePreview executePreview = new ExecutePreview(main);
    private final ExecuteLootHistory executeLootHistory = new ExecuteLootHistory(main);

    public CoreCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if(args.length == 0) {
            if(sender instanceof Player) {
                if(!main.getLanguageHandler().isEmptyMessage("Commands.Balance.Cubelets"))
                    sender.sendMessage(main.getLanguageHandler().getMessage("Commands.Balance.Cubelets").replaceAll("%cubelets_available%", String.valueOf(main.getPlayerDataHandler().getData((Player) sender).getCubelets().size())));
                return true;
            } else {
                return sendCommandHelp(sender, label);
            }
        }

        if(sender instanceof Player) {
            if(main.getConversationHandler().haveConversation((Player) sender)) return true;
        }

        switch (args[0]) {
            case "help":
                return sendCommandHelp(sender, label);
            case "machine":
                return executeBox.executeCommand(sender, label, args);
            case "give":
                return executeGive.executeCommand(sender, label, args);
            case "givekey":
                return executeGiveKey.executeCommand(sender, label, args);
            case "remove":
                return executeRemove.executeCommand(sender, label, args);
            case "type":
                return executeType.executeCommand(sender, label, args);
            case "setup":
                return executeSetup.executeCommand(sender, label, args);
            case "options":
                return executeOptions.executeCommand(sender, label, args);
            case "reload":
                return executeReload.executeCommand(sender, label, args);
            case "info":
                return executeInfo.executeCommand(sender, label, args);
            case "clear":
                return executeClear.executeCommand(sender, label, args);
            case "gift":
                return executeGift.executeCommand(sender, label, args);
            case "preview":
                return executePreview.executeCommand(sender, label, args);
            case "history":
                return executeLootHistory.executeCommand(sender, label, args);
        }

        sender.sendMessage("");
        sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cInvalid argument, use /" + label + " help to see available commands"));
        sender.sendMessage("");
        return false;
    }

    private boolean sendCommandHelp(CommandSender sender, String label) {

        if(sender instanceof Player) {

            if(main.isSetting("GiftCubeletsCommand")) {
                sender.sendMessage("");
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " gift [player]"));
            } else {
                sender.sendMessage("");
            }

            if(main.isSetting("Rewards.Preview.Enabled")) {
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " preview [typeID]"));
            }

            sender.sendMessage(Utils.translate("&7 - &a/" + label + " history"));

            if (main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage("");
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " [give/remove] [player] [typeID] [amount]"));
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " [info/clear] [player]"));

                if(main.isSetting("UseKeys"))
                    sender.sendMessage(Utils.translate("&7 - &a/" + label + " givekey [player] [typeID] [amount]"));

                sender.sendMessage("");
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " machine [create/remove/edit]"));
                sender.sendMessage("");
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " type"));
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " setup [typeID]"));
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " options"));
                sender.sendMessage("");
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " reload"));
                sender.sendMessage("");
            } else {
                sender.sendMessage("");
            }

        } else {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate("&7 - &a/" + label + " [give/remove] [player] [typeID] [amount]"));
            sender.sendMessage(Utils.translate("&7 - &a/" + label + " [info/clear] [player]"));

            if(main.isSetting("UseKeys"))
                sender.sendMessage(Utils.translate("&7 - &a/" + label + " givekey [player] [typeID] [amount]"));

            sender.sendMessage("");
            sender.sendMessage(Utils.translate("&7 - &a/" + label + " reload"));
            sender.sendMessage("");
        }

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
            if(main.isSetting("GiftCubeletsCommand"))
                list.add("gift");
            if(main.isSetting("Rewards.Preview.Enabled"))
                list.add("preview");
            list.add("history");
            if (main.playerHasPermission(p, "acubelets.admin")) {
                list.add("help");
                list.add("give");

                if(main.isSetting("UseKeys"))
                    list.add("givekey");

                list.add("info");
                list.add("clear");
                list.add("remove");
                list.add("machine");
                list.add("type");
                list.add("setup");
                list.add("options");
                list.add("reload");
            }
        }

        if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("givekey")) {
            if (args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    for (Player target : main.getServer().getOnlinePlayers()) {
                        list.add(target.getName());
                    }
                    list.add("*");
                }
            } else if (args.length == 3) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    list.addAll(main.getCubeletTypesHandler().getTypes().keySet());
                }
            } else if (args.length == 4) {
                list.add("1");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    for (Player target : main.getServer().getOnlinePlayers()) {
                        list.add(target.getName());
                    }
                }
            } else if (args.length == 3) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    list.addAll(main.getCubeletTypesHandler().getTypes().keySet());
                }
            }
        } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("clear")) {
            if (args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    for (Player target : main.getServer().getOnlinePlayers()) {
                        list.add(target.getName());
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("setup")) {
            if(args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    for (File file : Objects.requireNonNull(new File(main.getDataFolder(), "types").listFiles())) {
                        list.add(file.getName().replace(".yml", ""));
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("machine")) {
            if(args.length == 2) {
                if (main.playerHasPermission(p, "acubelets.admin")) {
                    list.add("create");
                    list.add("remove");
                    list.add("edit");
                }
            }
        } else if (args[0].equalsIgnoreCase("type")) {
            if (main.playerHasPermission(p, "acubelets.admin")) {
                if(args.length == 2) {
                    list.add("create");
                    list.add("remove");
                    list.add("template");
                    list.add("list");
                } else if(args.length == 3) {
                    if(args[1].equalsIgnoreCase("remove")) {
                        for (String type : main.getCubeletTypesHandler().getTypes().keySet()) {
                            list.add(type.toLowerCase());
                        }
                    } else if(args[1].equalsIgnoreCase("template")) {
                        list.addAll(main.getTemplates());
                        list.add("*");
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("gift")) {
            if (args.length == 2) {
                for (Player target : main.getServer().getOnlinePlayers()) {
                    if(!target.getName().equalsIgnoreCase(p.getName()))
                        list.add(target.getName());
                }
            }
        } else if (args[0].equalsIgnoreCase("preview")) {
            if(args.length == 2) {
                list.addAll( main.getCubeletTypesHandler().getTypes().keySet());
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