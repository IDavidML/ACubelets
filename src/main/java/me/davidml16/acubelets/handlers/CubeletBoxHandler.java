package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.utils.Utils;
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

    public void createBox(Location loc, double blockHeight) {
        CubeletBox box = new CubeletBox(loc, blockHeight, blockHeight, Rotation.SOUTH);
        boxes.put(loc, box);
        main.getHologramHandler().loadHolograms(box);

        config.set("locations", new ArrayList<>());

        int i = 1;
        for(CubeletBox bx : boxes.values()) {
            config.set("locations." + i + ".world", bx.getLocation().getWorld().getName());
            config.set("locations." + i + ".x", bx.getLocation().getBlockX());
            config.set("locations." + i + ".y", bx.getLocation().getBlockY());
            config.set("locations." + i + ".z", bx.getLocation().getBlockZ());
            config.set("locations." + i + ".rotation", bx.getRotation().toString());
            config.set("locations." + i + ".blockHeight", bx.getBlockHeight());
            config.set("locations." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
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
            for(CubeletBox bx : boxes.values()) {
                config.set("locations." + i + ".world", bx.getLocation().getWorld().getName());
                config.set("locations." + i + ".x", bx.getLocation().getBlockX());
                config.set("locations." + i + ".y", bx.getLocation().getBlockY());
                config.set("locations." + i + ".z", bx.getLocation().getBlockZ());
                config.set("locations." + i + ".rotation", bx.getRotation().toString());
                config.set("locations." + i + ".blockHeight", bx.getBlockHeight());
                config.set("locations." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
                i++;
            }

            saveConfig();
        }
    }

    public void saveBoxes() {
        config.set("locations", new ArrayList<>());

        int i = 1;
        for(CubeletBox bx : boxes.values()) {
            config.set("locations." + i + ".world", bx.getLocation().getWorld().getName());
            config.set("locations." + i + ".x", bx.getLocation().getBlockX());
            config.set("locations." + i + ".y", bx.getLocation().getBlockY());
            config.set("locations." + i + ".z", bx.getLocation().getBlockZ());
            config.set("locations." + i + ".rotation", bx.getRotation().toString());
            config.set("locations." + i + ".blockHeight", bx.getBlockHeight());
            config.set("locations." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
            i++;
        }

        saveConfig();
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

        Main.log.sendMessage(Utils.translate(""));
        Main.log.sendMessage(Utils.translate("  &eLoading machines:"));

        if(config.contains("locations")) {
            if(config.getConfigurationSection("locations") != null) {
                for (int i = 1; i <= config.getConfigurationSection("locations").getKeys(false).size(); i++) {
                    String world = config.getString("locations." + i + ".world");
                    int x = config.getInt("locations." + i + ".x");
                    int y = config.getInt("locations." + i + ".y");
                    int z = config.getInt("locations." + i + ".z");

                    if(Bukkit.getServer().getWorld(world) == null) continue;

                    Rotation rotation = Rotation.SOUTH;
                    if(config.contains("locations." + i + ".rotation"))
                        rotation = Rotation.valueOf(config.getString("locations." + i + ".rotation"));

                    double blockHeight = 0.875;
                    if(config.contains("locations." + i + ".blockHeight"))
                        blockHeight = config.getDouble("locations." + i + ".blockHeight");

                    double permanentBlockHeight = blockHeight;
                    if(config.contains("locations." + i + ".permanentBlockHeight"))
                        permanentBlockHeight = config.getDouble("locations." + i + ".permanentBlockHeight");

                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                    boxes.put(loc, new CubeletBox(loc, blockHeight, permanentBlockHeight, rotation));
                }
            }

            config.set("locations", new ArrayList<>());
            int i = 1;
            for(CubeletBox bx : boxes.values()) {
                if(bx.getLocation().getWorld() == null) continue;
                config.set("locations." + i + ".world", bx.getLocation().getWorld().getName());
                config.set("locations." + i + ".x", bx.getLocation().getBlockX());
                config.set("locations." + i + ".y", bx.getLocation().getBlockY());
                config.set("locations." + i + ".z", bx.getLocation().getBlockZ());
                config.set("locations." + i + ".rotation", bx.getRotation().toString());
                config.set("locations." + i + ".blockHeight", bx.getBlockHeight());
                config.set("locations." + i + ".permanentBlockHeight", bx.getPermanentBlockHeight());
                i++;
            }
            saveConfig();
        }

        if(boxes.size() == 0)
            Main.log.sendMessage(Utils.translate("    &cNo Cubelet Machines has been loaded!"));
        else
            Main.log.sendMessage(Utils.translate("    &b" + boxes.size() + " &aCubelet Machines loaded!"));

    }

}
