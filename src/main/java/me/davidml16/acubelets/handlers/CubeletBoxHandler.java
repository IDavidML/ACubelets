package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
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

public class CubeletBoxHandler {

    private HashMap<Location, CubeletBox> boxes;
    private File file;
    private YamlConfiguration config;

    private Main main;

    public CubeletBoxHandler(Main main) {
        this.main = main;
        this.boxes = new HashMap<Location, CubeletBox>();
    }

    public HashMap<Location, CubeletBox> getBoxes() {
        return boxes;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public CubeletBox getBoxByLocation(Location loc) {
        return boxes.get(loc);
    }

    public void createBox(Location loc) {
        CubeletBox box = new CubeletBox(loc);
        boxes.put(loc, box);
        main.getHologramHandler().loadHolograms(box);

        int i = 1;
        for(Location location : boxes.keySet()) {
            config.set("locations." + String.valueOf(i) + ".world", location.getWorld().getName());
            config.set("locations." + String.valueOf(i) + ".x", location.getBlockX());
            config.set("locations." + String.valueOf(i) + ".y", location.getBlockY());
            config.set("locations." + String.valueOf(i) + ".z", location.getBlockZ());
            i++;
        }

        saveConfig();
    }

    public void removeBox(Location loc) {
        if(boxes.containsKey(loc)) {

            CubeletBox box = getBoxByLocation(loc);
            for(Hologram holo : box.getHolograms().values()) {
                holo.delete();
            }
            box.getHolograms().clear();

            boxes.remove(loc);

            config.set("locations", new ArrayList<>());

            int i = 1;
            for(Location location : boxes.keySet()) {
                config.set("locations." + String.valueOf(i) + ".world", location.getWorld().getName());
                config.set("locations." + String.valueOf(i) + ".x", location.getBlockX());
                config.set("locations." + String.valueOf(i) + ".y", location.getBlockY());
                config.set("locations." + String.valueOf(i) + ".z", location.getBlockZ());
                i++;
            }

            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBoxes() {

        boxes.clear();

        File file = new File(main.getDataFolder(), "locations.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                config.set("locations", new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        this.config = config;

        if(!config.contains("locations")) {
            config.set("locations", new ArrayList<>());
        }

        saveConfig();

        Main.log.sendMessage(ColorUtil.translate(""));
        Main.log.sendMessage(ColorUtil.translate("  &eLoading boxes:"));

        if(config.contains("locations")) {
            if(config.getConfigurationSection("locations") != null) {
                for (int i = 1; i <= config.getConfigurationSection("locations").getKeys(false).size(); i++) {
                    String world = config.getString("locations." + String.valueOf(i) + ".world");
                    int x = config.getInt("locations." + String.valueOf(i) + ".x");
                    int y = config.getInt("locations." + String.valueOf(i) + ".y");
                    int z = config.getInt("locations." + String.valueOf(i) + ".z");
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                    boxes.put(loc, new CubeletBox(loc));
                }
            }
        }

        if(boxes.size() == 0)
            Main.log.sendMessage(ColorUtil.translate("    &cNo cubelet boxes has been loaded!"));
        else
            Main.log.sendMessage(ColorUtil.translate("    &b" + boxes.size() + " &aCubelet Boxes loaded!"));

    }

}
