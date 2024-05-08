package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.menus.player.LootHistoryMenu;
import me.davidml16.acubelets.menus.player.crafting.CraftingMenu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class ExecuteClear {

    private Main main;
    public ExecuteClear(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                if(!main.getLanguageHandler().isEmptyMessage("Commands.NoPerms"))
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

            main.getDatabaseHandler().hasName(player, name -> {

                if(name == null) {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis player not exists in the database!"));

                } else {

                    if(Bukkit.getPlayer(player) == null) {

                        try {

                            main.getDatabaseHandler().getPlayerUUID(player, result -> {

                                UUID uuid = UUID.fromString(result);

                                main.getDatabaseHandler().removeCubelets(uuid);
                                main.getDatabaseHandler().removeLootHistory(uuid);

                                String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Clear");
                                msg = msg.replaceAll("%player%", name);
                                sender.sendMessage(Utils.translate(msg));

                            });

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } else {

                        Player target = Bukkit.getPlayer(player);

                        main.getPlayerDataHandler().getData(target).getCubelets().clear();
                        main.getPlayerDataHandler().getData(target).getLootHistory().clear();

                        String msg = main.getLanguageHandler().getMessage("Commands.Cubelets.Clear");
                        msg = msg.replaceAll("%player%", name);
                        sender.sendMessage(Utils.translate(msg));

                        Bukkit.getScheduler().runTaskLater(main, () -> {

                            main.getMenuHandler().reloadAllMenus(target, CubeletsMenu.class);
                            main.getMenuHandler().reloadAllMenus(target, CraftingMenu.class);
                            main.getMenuHandler().reloadAllMenus(target, LootHistoryMenu.class);

                            main.getHologramImplementation().reloadHolograms(target);

                            main.getDatabaseHandler().removeCubelets(target.getUniqueId());
                            main.getDatabaseHandler().removeLootHistory(target.getUniqueId());

                        }, 1L);

                    }

                }

            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return true;
    }

}
