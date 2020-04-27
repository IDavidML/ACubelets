package me.davidml16.acubelets.commands.commands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ACMaterial;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExecuteBox {

    private Main main;
    public ExecuteBox(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ColorUtil.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
            sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
            return false;
        }

        if (args.length == 1) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets box [add/remove]"));
            return false;
        }

        if (args[1].equalsIgnoreCase("add")) {
            Block block = ((Player)sender).getTargetBlock(null, 10);

            if(block.getType() == ACMaterial.END_PORTAL_FRAME.parseMaterial()) {
                if (!main.getCubeletBoxHandler().getBoxes().containsKey(block.getLocation())) {
                    main.getCubeletBoxHandler().createBox(block.getLocation());
                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully created new cubelet box on" +
                            " &aX: &e" + block.getLocation().getBlockX() +
                            ", &aY: &e" + block.getLocation().getBlockY() +
                            ", &aZ: &e" + block.getLocation().getBlockZ()));
                    return true;
                } else {
                    sender.sendMessage(ColorUtil.translate(
                            main.getLanguageHandler().getPrefix() + " &cThis cubelet box location already exists!"));
                    return false;
                }
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cA cubelet box needs to be a ender portal frame!"));
                return false;
            }
        }

        if (args[1].equalsIgnoreCase("remove")) {
            Block block = ((Player) sender).getTargetBlock(null, 10);

            if(block.getType() == ACMaterial.END_PORTAL_FRAME.parseMaterial()) {
                if (main.getCubeletBoxHandler().getBoxes().containsKey(block.getLocation())) {
                    main.getCubeletBoxHandler().removeBox(block.getLocation());
                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully removed cubelet box of" +
                            " &aX: &e" + block.getLocation().getBlockX() +
                            ", &aY: &e" + block.getLocation().getBlockY() +
                            ", &aZ: &e" + block.getLocation().getBlockZ()));
                    return true;
                } else {
                    sender.sendMessage(ColorUtil.translate(
                            main.getLanguageHandler().getPrefix() + " &cThis cubelet box location no exists!"));
                    return false;
                }
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cA cubelet box needs to be a ender portal frame!"));
                return false;
            }
        }

        return true;
    }

}
