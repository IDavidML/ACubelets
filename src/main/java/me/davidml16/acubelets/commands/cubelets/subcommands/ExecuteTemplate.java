package me.davidml16.acubelets.commands.cubelets.subcommands;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

public class ExecuteTemplate {

    private List<String> templates;

    private Main main;
    public ExecuteTemplate(Main main) {
        this.main = main;
        this.templates = main.getTemplates();
    }

    public boolean executeCommand(CommandSender sender, String label, String[] args) {
        if(sender instanceof Player) {
            if (!main.playerHasPermission((Player) sender, "acubelets.admin")) {
                sender.sendMessage(main.getLanguageHandler().getMessage("Commands.NoPerms"));
                return false;
            }
        }

        if(args.length == 1) {
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(" &cUsage: /" + label + " template [name]"));
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(" &aAvailable templates: &e" + templates));
            sender.sendMessage("");
            return false;
        }

        String template = args[1];

        if(!template.equalsIgnoreCase("*")) {

            if (!templates.contains(template.toLowerCase())) {
                sender.sendMessage("");
                sender.sendMessage(ColorUtil.translate(" &cThis template not exists"));
                sender.sendMessage("");
                sender.sendMessage(ColorUtil.translate(" &aAvailable templates: &e" + templates));
                sender.sendMessage("");
                return false;
            }

            sender.sendMessage("");
            donwloadTemplate(sender, template);
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(" &aPlease use /" + label + " reload, to load it"));
            sender.sendMessage("");


        } else {

            sender.sendMessage("");
            for(String temp : templates) donwloadTemplate(sender, temp);
            sender.sendMessage("");
            sender.sendMessage(ColorUtil.translate(" &aPlease use /" + label + " reload, to load it"));
            sender.sendMessage("");

        }

        return true;
    }

    public void donwloadTemplate(CommandSender sender, String template) {
        File file = new File(main.getDataFolder() + "/types/" + template + ".yml");

        if(file.exists())
            file.delete();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        Map<String, Object> msgDefaults = new LinkedHashMap<String, Object>();

        InputStreamReader input = new InputStreamReader(main.getResource("type_templates/" + template + ".yml"));
        FileConfiguration data = YamlConfiguration.loadConfiguration(input);

        for(String key : data.getKeys(true)) {
            if(!(data.get(key) instanceof MemorySection)) {
                msgDefaults.put(key, data.get(key));
            }
        }

        for (String key : msgDefaults.keySet()) {
            if (!cfg.isSet(key)) {
                cfg.set(key, msgDefaults.get(key));
            }
        }

        for(String key : cfg.getKeys(true)) {
            if(!(cfg.get(key) instanceof MemorySection)) {
                if (!data.isSet(key)) {
                    cfg.set(key, null);
                }
            }
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage(ColorUtil.translate(" &aTemplate '&6" + template + "&a' has been downloaded successfully"));

    }

}
