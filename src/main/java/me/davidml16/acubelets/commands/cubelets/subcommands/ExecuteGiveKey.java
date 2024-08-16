package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ExecuteGiveKey {

    private Main main;
    public ExecuteGiveKey(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if(!main.isSetting("UseKeys")) return true;

        if (args.length == 1 || args.length == 2) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " givekey [player] [typeID] [amount]"));
            sender.sendMessage("");
            return false;
        }

        String player = args[1];

        String id = args[2];
        if (!main.getCubeletTypesHandler().getTypes().containsKey(id)) {
            sender.sendMessage(Utils.translate(
                    main.getLanguageHandler().getPrefix() + " &cThis " + label + " type doesn't exists!"));
            return false;
        }

        int amount = 1;
        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(id);

        if(player.equalsIgnoreCase("*") || player.equalsIgnoreCase("all")) {
            if(args.length == 3) {
                for(Player iterator : Bukkit.getOnlinePlayers()) {
                    giveKey(sender, iterator, cubeletType, amount);
                }
                return true;
            } else if(args.length == 4) {
                amount = Integer.parseInt(args[3]);
                if(amount > 0) {
                    for(Player iterator : Bukkit.getOnlinePlayers()) {
                        giveKey(sender, iterator, cubeletType, amount);
                    }
                    return true;
                } else {
                    sender.sendMessage(Utils.translate(
                            main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                    return false;
                }
            }
        } else {

            Player target = Bukkit.getPlayer(player);
            if(target == null) {
                sender.sendMessage(main.getLanguageHandler().getPrefix() + " &cPlayer " + player + " is not online!");
                return false;
            }

            if(args.length == 3) {
                giveKey(sender, target, cubeletType, amount);
                return true;
            } else if(args.length == 4) {
                amount = Integer.parseInt(args[3]);
                if(amount > 0) {
                    giveKey(sender, target, cubeletType, amount);
                    return true;
                } else {
                    sender.sendMessage(Utils.translate(
                            main.getLanguageHandler().getPrefix() + " &cAmount to give need to be more than 0!"));
                    return false;
                }
            }
        }


        return true;
    }

    private void giveKey(CommandSender sender, Player target, CubeletType cubeletType, int amount) {

        ItemStack key = cubeletType.getKeyNBT();
        for(int i = 0; i < amount; i++) {
            if(target.getInventory().firstEmpty() >= 0)
                target.getInventory().addItem(key);
            else
                target.getLocation().getWorld().dropItemNaturally(target.getLocation(), key);
        }

        String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.GiveKey.Give");
        msg = msg.replaceAll("%amount%", Integer.toString(amount));
        msg = msg.replaceAll("%cubelet%",  cubeletType.getName());
        msg = msg.replaceAll("%player%", target.getName());
        sender.sendMessage(Utils.translate(msg));

        String msgReceived = main.getLanguageHandler().getMessage("Commands.Cubelets.GiveKey.Received");
        msgReceived = msgReceived.replaceAll("%amount%", Integer.toString(amount));
        msgReceived = msgReceived.replaceAll("%cubelet%",  cubeletType.getName());
        target.sendMessage(Utils.translate(msgReceived));

    }

}
