package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.admin.EditMachineMenu;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ExecuteBox {

    private Main main;
    public ExecuteBox(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.translate("&cThe commands only can be use by players!"));
            return true;
        }

        if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
            sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
            return false;
        }

        if (args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /" + label + " machine [create/remove/edit]"));
            sender.sendMessage("");
            return false;
        }

        if (args[1].equalsIgnoreCase("create")) {

            Block block = ((Player)sender).getTargetBlock(null, 5);

            if(block.getType().isBlock() && block.getType() != Material.AIR) {

                if (!main.getCubeletBoxHandler().getMachines().containsKey(block.getLocation())) {

                    ArmorStand armorStand = block.getLocation().getWorld().spawn(block.getLocation().clone().add(0.5, 1.5, 0.5), ArmorStand.class);
                    armorStand.setVisible(false);
                    armorStand.setGravity(true);
                    armorStand.setSmall(false);
                    armorStand.setRemoveWhenFarAway(false);

                    Bukkit.getScheduler().runTaskLater(main, () -> armorStand.setVelocity(new Vector(0, -10, 0)), 1);

                    Sounds.playSound(((Player)sender), ((Player)sender).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully " + label + " new cubelet machine on" +
                            " &aX: &e" + block.getLocation().getBlockX() +
                            ", &aY: &e" + block.getLocation().getBlockY() +
                            ", &aZ: &e" + block.getLocation().getBlockZ()));

                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        double blockHeight = armorStand.getLocation().getY() - block.getLocation().getBlockY();
                        main.getCubeletBoxHandler().createMachine(block.getLocation(), blockHeight);
                        armorStand.remove();
                    }, 10);

                    return true;

                } else {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis " + label + " machine location already exists!"));

                    return false;

                }

            } else {

                sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cA " + label + " machine needs to be a block!"));

                return false;

            }
        }

        if (args[1].equalsIgnoreCase("remove")) {

            Block block = ((Player) sender).getTargetBlock(null, 5);

            if(block.getType().isBlock() && block.getType() != Material.AIR) {

                if (main.getCubeletBoxHandler().getMachines().containsKey(block.getLocation())) {

                    main.getCubeletBoxHandler().removeMachine(block.getLocation());

                    Sounds.playSound(((Player)sender), ((Player)sender).getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully removed " + label + " machine of" +
                            " &aX: &e" + block.getLocation().getBlockX() +
                            ", &aY: &e" + block.getLocation().getBlockY() +
                            ", &aZ: &e" + block.getLocation().getBlockZ()));

                    return true;

                } else {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis " + label + " machine location no exists!"));

                    return false;

                }

            } else {

                sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cA " + label + " machine needs to be a block!"));

                return false;

            }

        }

        if (args[1].equalsIgnoreCase("edit")) {

            Block block = ((Player) sender).getTargetBlock(null, 5);

            if(block.getType().isBlock() && block.getType() != Material.AIR) {

                if (main.getCubeletBoxHandler().getMachines().containsKey(block.getLocation())) {

                    EditMachineMenu editMachineMenu = new EditMachineMenu(main, (Player) sender);
                    editMachineMenu.setAttribute(Menu.AttrType.CUBELET_BOX_ATTR, main.getCubeletBoxHandler().getMachineByLocation(block.getLocation()));
                    editMachineMenu.open();

                    return true;

                } else {

                    sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cThis " + label + " machine location no exists!"));

                    return false;

                }

            } else {

                sender.sendMessage(Utils.translate(main.getLanguageHandler().getPrefix() + " &cA " + label + " machine needs to be a block!"));

                return false;

            }

        }

        return true;
    }

}
