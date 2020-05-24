package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CubeletCraftingHandler {

    private File file;
    private YamlConfiguration config;

    private Main main;

    public CubeletCraftingHandler(Main main) {
        this.main = main;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCrafting() {
        File file = new File(main.getDataFolder(), "cubelet-crafting.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                config.set("cubelet-crafting.settings.rows", 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        this.config = config;

        saveConfig();
    }

    public int getInventorySize() {
        int size = config.getInt("cubelet-crafting.settings.rows");
        if(size >= 2 && size <= 6)
            return size * 9;
        return 4 * 9;
    }

}
