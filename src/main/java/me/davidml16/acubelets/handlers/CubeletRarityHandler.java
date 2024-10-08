package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class CubeletRarityHandler {

    private Main main;

    public CubeletRarityHandler(Main main) {
        this.main = main;
    }

    public Rarity getRarityById(String type, String id) {
        return main.getCubeletTypesHandler().getTypeBydId(type).getRarities().get(id);
    }

    public boolean createRarity(String type, String id, String name, int chance, String duplicatePointsRange) {
        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
        cubeletType.getRarities().put(id, new Rarity(id, name, chance, duplicatePointsRange));
        FileConfiguration config = main.getCubeletTypesHandler().getConfig(type);
        config.set("type.rarities", new ArrayList<>());
        for(Rarity rarity : cubeletType.getRarities().values()) {
            config.set("type.rarities." + rarity.getId() + ".name", rarity.getName());
            config.set("type.rarities." + rarity.getId() + ".chance", rarity.getChance());
            config.set("type.rarities." + rarity.getId() + ".duplicatePointsRange", rarity.getDuplicatePointsRange());
        }
        main.getCubeletTypesHandler().saveConfig(type);
        return true;
    }

    public boolean removeRarity(String type, String id) {
        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
        if(cubeletType.getRarities().containsKey(id)) {
            FileConfiguration config = main.getCubeletTypesHandler().getConfig(type);
            cubeletType.getRarities().remove(id);
            config.set("type.rarities", new ArrayList<>());
            for(Rarity rarity : cubeletType.getRarities().values()) {
                config.set("type.rarities." + rarity.getId() + ".name", rarity.getName());
                config.set("type.rarities." + rarity.getId() + ".chance", rarity.getChance());
                config.set("type.rarities." + rarity.getId() + ".duplicatePointsRange", rarity.getDuplicatePointsRange());
            }
            main.getCubeletTypesHandler().saveConfig(type);
            return true;
        }
        return false;
    }

    public void loadRarities() {

        for(CubeletType cubeletType : main.getCubeletTypesHandler().getTypes().values()) {
            FileConfiguration config = main.getCubeletTypesHandler().getConfig(cubeletType.getId());

            cubeletType.getRarities().clear();

            if(!config.contains("type.rarities")) {
                config.set("type.rarities", new ArrayList<>());
            }

            main.getCubeletTypesHandler().saveConfig(cubeletType.getId());

            if(config.contains("type.rarities")) {
                if(config.getConfigurationSection("type.rarities") != null) {
                    for (String id : config.getConfigurationSection("type.rarities").getKeys(false)) {
                        String name = config.getString("type.rarities." + id + ".name");
                        double chance = config.getDouble("type.rarities." + id + ".chance");

                        String duplicatePointsRange;
                        if(config.contains("type.rarities." + id + ".duplicatePointsRange"))
                            duplicatePointsRange = config.getString("type.rarities." + id + ".duplicatePointsRange");
                        else
                            duplicatePointsRange = "50-450";

                        cubeletType.getRarities().put(id, new Rarity(id, name, chance, duplicatePointsRange));
                    }
                }
            }

        }
    }

    public boolean rarityExist(String type, String id) {
        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
        return cubeletType.getRarities().containsKey(id);
    }

}
