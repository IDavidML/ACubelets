package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.gifts.GiftMenu;
import me.davidml16.acubelets.objects.GiftGuiSession;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class ExecuteGift {

    private Main main;
    public ExecuteGift(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if(!main.isGiftCubelets()) return true;

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " gift [player]"));
            sender.sendMessage("");
            return true;
        }

        String player = args[1];

        if(sender.getName().equalsIgnoreCase(player)) {
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getMessage("Commands.Cubelets.Gift.Yourself")));
            return false;
        }

        try {

            main.getDatabaseHandler().hasName(player, name -> {

                if(name == null) {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));

                } else {

                    try {

                        main.getDatabaseHandler().getPlayerUUID(player, result -> {

                            UUID uuid = UUID.fromString(result);

                            GiftGuiSession giftGuiSession = new GiftGuiSession(((Player) sender).getUniqueId(), uuid, name, true);

                            GiftMenu giftMenu = new GiftMenu(main, (Player) sender);
                            giftMenu.setAttribute(Menu.AttrType.GIFT_GUISESSION_ATTR, giftGuiSession);
                            giftMenu.open();

                        });

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

}
