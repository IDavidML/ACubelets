package me.davidml16.acubelets.commands.commands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteType {

    private Main main;
    public ExecuteType(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if(args.length == 1) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets type create [id] [name]"));
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets type remove [id]"));
            return false;
        }

        if(args[1].equalsIgnoreCase("create")) {

             if (args.length <= 3) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets type create [id] [name]"));
                return false;
            }

            String id = args[2].toLowerCase();

            StringBuilder name = new StringBuilder();
            for(int i = 3; i < args.length; i++) {
                name.append(args[i]).append(args.length - 1 > i ? " " : "");
            }

            if (main.getCubeletTypesHandler().typeExist(id)) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cThis cubelet type already exists!"));
                return true;
            }

            if (!Character.isDigit(id.charAt(0))) {
                if (main.getCubeletTypesHandler().createType(id)) {
                    FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);
                    config.set("type.name", name.toString());
                    config.set("type.animation", "animation1");
                    config.set("type.icon.texture", "base64:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYyMmI2YTNhMGYyNGJkZWVhYjJhNmFjZDliMWY1MmJiOTU5NGQ1ZjZiMWUyYzA1ZGRkYjIxOTQxMGM4In19fQ==");

                    List<String> description = Arrays.asList("&7This is the most common type of", "&7Cubelet. Initial scans indicate", "&7that the contents of this cubelet", "&7will be probably basic.");
                    List<String> lore = Arrays.asList("&5Received: &a%received% ago", "", "%description%", "", "&6Click to open.");

                    config.set("type.description", description);
                    config.set("type.lore.opening", lore);

                    config.set("type.rarities", new ArrayList<>());
                    config.set("type.rewards", new ArrayList<>());
                    main.getCubeletTypesHandler().saveConfig(id);

                    main.getPluginHandler().reloadAll();

                    main.getTypeConfigGUI().loadGUI(id);
                    main.getRaritiesGUI().loadGUI(id);
                    main.getRewardsGUI().loadGUI(id);
                    main.getAnimationsGUI().loadGUI(id);

                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully created cubelet type &e" + id + " &awith the name &e" + name));
                    return true;
                }
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cThe cubelet type cannot start with a number, use for example 'normal'."));
                return false;
            }
        } else if(args[1].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets type remove [id]"));
                return false;
            }

            String id = args[2].toLowerCase();

            if (!main.getCubeletTypesHandler().typeExist(id)) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cThis cubelet type no exists!"));
                return true;
            }

            if (!Character.isDigit(id.charAt(0))) {
                if (main.getCubeletTypesHandler().removeType(id)) {
                    main.getTypeConfigGUI().getGuis().remove(id);
                    main.getRaritiesGUI().getGuis().remove(id);
                    main.getRewardsGUI().getGuis().remove(id);
                    main.getAnimationsGUI().getGuis().remove(id);
                    main.getCubeletTypesHandler().getTypes().remove(id);

                    try {
                        main.getDatabaseHandler().removeCubelet(id);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                        main.getPlayerDataHandler().getData(p).getCubelets().removeIf(cubelet -> {
                            return cubelet.getType().equalsIgnoreCase(id);
                        });
                        if(main.getCubeletsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
                        if(main.getTypeConfigGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
                        if(main.getRewardsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
                        if(main.getRaritiesGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
                        if(main.getAnimationsGUI().getOpened().containsKey(p.getUniqueId())) p.closeInventory();
                    }

                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &aSuccesfully removed cubelet type &e" + id + "&a!"));
                    return true;
                }
            } else {
                sender.sendMessage(ColorUtil.translate(
                        main.getLanguageHandler().getPrefix() + " &cThe cubelet type cannot start with a number, use for example 'normal'."));
                return false;
            }
        }
        return false;
    }

}
