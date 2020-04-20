package me.davidml16.acubelets.commands.commands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteCreate {

    private Main main;
    public ExecuteCreate(Main main) {
        this.main = main;
    }

    public boolean executeCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if (args.length >= 1 && args.length <= 3) {
            sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cUsage: /cubelets create [type/rarity] [id] [name]"));
            return false;
        }

        String id = args[2].toLowerCase();

        StringBuilder name = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            name.append(args[i]).append(args.length - 1 > i ? " " : "");
        }

        if(args[1].equalsIgnoreCase("type")) {

            if (main.getCubeletTypesHandler().typeExist(id)) {
                sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cThis cubelet type already exists!"));
                return true;
            }

            if (!Character.isDigit(id.charAt(0))) {
                if (main.getCubeletTypesHandler().createType(id)) {
                    FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);
                    config.set("type.name", name.toString());
                    config.set("type.icon.texture", "base64:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYyMmI2YTNhMGYyNGJkZWVhYjJhNmFjZDliMWY1MmJiOTU5NGQ1ZjZiMWUyYzA1ZGRkYjIxOTQxMGM4In19fQ==");

                    List<String> lore = Arrays.asList(
                            "&5Received: &a%received% ago",
                            "",
                            "&7This is the most common type of ",
                            "&7Cubelet. Initial scans indicate ",
                            "&7that the contents of this cubelet ",
                            "&7will be probably basic. ",
                            "",
                            "&6Click to open."
                    );

                    config.set("type.icon.lore", lore);
                    config.set("type.rewards", new ArrayList<>());
                    main.getCubeletTypesHandler().saveConfig(id);

                    main.getTypeConfigGUI().loadGUI(id);

                    sender.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully created cubelet type &e" + id + " &awith the name &e" + name));
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
