package me.davidml16.acubelets.handlers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.CubeletBox;
import me.davidml16.acubelets.data.Rarity;
import me.davidml16.acubelets.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CubeletRarityHandler {

    private HashMap<String, Rarity> rarities;
    private File file;
    private YamlConfiguration config;

    private Main main;

    public CubeletRarityHandler(Main main) {
        this.main = main;
        this.rarities = new HashMap<String, Rarity>();
    }

    public HashMap<String, Rarity> getRarities() { return rarities; }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public Rarity getRarityById(String id) {
        return rarities.get(id);
    }

    public void createRarity(String id, String name, int chance) {
        rarities.put(id, new Rarity(id, name, chance));
        config.set("rarities", new ArrayList<>());

        for(String ids : rarities.keySet()) {
            Rarity rarity = getRarityById(ids);
            config.set("rarities." + id + ".name", rarity.getName());
            config.set("rarities." + id + ".chance", rarity.getChance());
        }

        saveConfig();
    }

    public void removeBox(String id) {
        if(rarities.containsKey(id)) {
            rarities.remove(id);
            config.set("rarities", new ArrayList<>());

            for(String ids : rarities.keySet()) {
                Rarity rarity = getRarityById(ids);
                config.set("rarities." + id + ".name", rarity.getName());
                config.set("rarities." + id + ".chance", rarity.getChance());
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

    public void loadRarities() {

        rarities.clear();

        File file = new File(main.getDataFolder(), "rarities.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try {
                file.createNewFile();
                config.set("rarities", new ArrayList<>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.file = file;
        this.config = config;

        if(!config.contains("rarities")) {
            config.set("rarities", new ArrayList<>());
        }

        saveConfig();

        Main.log.sendMessage(ColorUtil.translate(""));
        Main.log.sendMessage(ColorUtil.translate("  &eLoading rarities:"));

        if(config.contains("rarities")) {
            if(config.getConfigurationSection("rarities") != null) {
                for (String id : config.getConfigurationSection("locations").getKeys(false)) {
                    String name = config.getString("rarities." + id + ".name");
                    int chance = config.getInt("rarities." + id + ".chance");
                    rarities.put(id, new Rarity(id, name, chance));
                }
            }
        }

        if(rarities.size() == 0)
            Main.log.sendMessage(ColorUtil.translate("    &cNo cubelet rarities has been loaded!"));
        else
            Main.log.sendMessage(ColorUtil.translate("    &b" + rarities.size() + " &aCubelet rarities loaded!"));

    }

}
