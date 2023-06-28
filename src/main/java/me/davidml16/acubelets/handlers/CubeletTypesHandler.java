package me.davidml16.acubelets.handlers;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.ItemStack64;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.TimeAPI.TimeAPI;
import me.davidml16.acubelets.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CubeletTypesHandler {

    private HashMap<String, CubeletType> types;
    private HashMap<String, File> typesFiles;
    private HashMap<String, YamlConfiguration> typesConfigs;

    private Main main;

    public CubeletTypesHandler(Main main) {
        this.main = main;
        this.types = new HashMap<String, CubeletType>();
        this.typesFiles = new HashMap<String, File>();
        this.typesConfigs = new HashMap<String, YamlConfiguration>();
    }

    public HashMap<String, CubeletType> getTypes() {
        return types;
    }

    public HashMap<String, File> getTypesFiles() {
        return typesFiles;
    }

    public HashMap<String, YamlConfiguration> getTypesConfigs() {
        return typesConfigs;
    }

    public CubeletType getTypeBydId(String id) {
        for (CubeletType type : types.values()) {
            if (type.getId().equalsIgnoreCase(id))
                return type;
        }
        return null;
    }

    public boolean createType(String id) {
        File file = new File(main.getDataFolder(), "types/" + id + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
                typesFiles.put(id, file);
                typesConfigs.put(id, YamlConfiguration.loadConfiguration(file));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeType(String id) {
        if(typesFiles.containsKey(id) && typesConfigs.containsKey(id)) {
            typesFiles.get(id).delete();
            typesFiles.remove(id);
            typesConfigs.remove(id);
            return true;
        }
        return false;
    }

    public void saveConfig(String id) {
        try {
            File file = typesFiles.get(id);
            if(file.exists()) {
                typesConfigs.get(id).save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig(String id) {
        return typesConfigs.get(id);
    }

    public void loadTypes() {

        typesConfigs.clear();
        typesFiles.clear();
        types.clear();

        File directory = new File(main.getDataFolder(), "types");
        if(!directory.exists()) {
            directory.mkdir();
        }

        File[] allFiles = new File(main.getDataFolder(), "types").listFiles();
        for (File file : allFiles) {
            String id = file.getName().toLowerCase().replace(".yml", "");

            if(id.equalsIgnoreCase("random")) continue;

            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            typesFiles.put(id, file);
            typesConfigs.put(id, config);

            if(!Character.isDigit(id.charAt(0))) {
                if (validTypeData(config)) {

                    if (!config.contains("type.animation")) {
                        config.set("type.animation", "animation1");
                    }

                    if (!config.contains("type.expiration")) {
                        config.set("type.expiration", "2w");
                    }

                    if (!config.contains("type.description")) {
                        List<String> lore = Arrays.asList(
                                "&7This is the most common type of ",
                                "&7Cubelet. Initial scans indicate ",
                                "&7that the contents of this cubelet ",
                                "&7will be probably basic. "
                        );
                        config.set("type.description", lore);
                    }

                    if (!config.contains("type.lore.opening.available")) {
                        List<String> loreAvailable = Arrays.asList(
                                "&5Received: &a%received% ago",
                                "&5Expires: &aIn %expires%",
                                "",
                                "%description%",
                                "",
                                "&6Click to open."
                        );
                        config.set("type.lore.opening.available", loreAvailable);
                    }

                    if (!config.contains("type.lore.opening.expired")) {
                        List<String> loreExpired = Arrays.asList(
                                "&5Received: &a%received% ago",
                                "&5Expires: &cExpired",
                                "",
                                "%description%",
                                ""
                        );
                        config.set("type.lore.opening.expired", loreExpired);
                    }

                    if (!config.contains("type.icon")) {
                        config.set("type.icon.texture", "base64:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYyMmI2YTNhMGYyNGJkZWVhYjJhNmFjZDliMWY1MmJiOTU5NGQ1ZjZiMWUyYzA1ZGRkYjIxOTQxMGM4In19fQ==");
                    }

                    if (!config.contains("type.key")) {

                        ItemStack key = new ItemBuilder(XMaterial.TRIPWIRE_HOOK.parseMaterial(), 1)
                                .addGlow()
                                .setName(Utils.translate(config.getString("type.name") + "'s key"))
                                .setLore(Arrays.asList(
                                        Utils.translate("&8&oGo to a Cubelets Machine to"),
                                        Utils.translate("&8&oopen this key."),
                                        "",
                                        Utils.translate("&a&lLeft-Click: &6Preview rewards"),
                                        Utils.translate("&a&lRight-Click: &6Open cubelet key")
                                ))
                                .toItemStack();

                        if(!main.isSerializeBase64()) {
                            XItemStack.serialize(key, Utils.getConfigurationSection(config, "type.key"));
                        } else {
                            config.set("type.key", ItemStack64.itemStackToBase64(key));
                        }

                    }

                    if (!config.contains("type.rarities")) {
                        config.set("type.rarities", new ArrayList<>());
                    }

                    if (!config.contains("type.rewards")) {
                        config.set("type.rewards", new ArrayList<>());
                    }

                    if(!config.getString("type.animation").contains("animation")) {
                        config.set("type.animation", AnimationHandler.DEFAULT_ANIMATION);
                    }

                    saveConfig(id);

                    String name = config.getString("type.name");

                    CubeletType cubeletType = new CubeletType(main, id, name);
                    types.put(id, cubeletType);

                    cubeletType.setAnimation(config.getString("type.animation").toLowerCase());

                    if (!(config.get("type.icon.texture") instanceof MemorySection)) {
                        String[] icon = ((String) config.get("type.icon.texture")).split(":");
                        switch (icon[0].toLowerCase()) {
                            case "base64":
                                cubeletType.setIcon(SkullCreator.itemFromBase64(icon[1]));
                                break;
                            case "uuid":
                                cubeletType.setIcon(SkullCreator.itemFromUuid(UUID.fromString(icon[1])));
                                break;
                            case "name":
                                cubeletType.setIcon(SkullCreator.itemFromName(icon[1]));
                                break;
                        }
                    } else {
                        try {
                            cubeletType.setIcon(new ItemBuilder(ItemStack64.itemStackFromBase64(config.getString("type.icon.texture"))).setLore("").toItemStack());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    List<String> loreAvailable = new ArrayList<>();
                    for(String line : config.getStringList("type.lore.opening.available")) {
                        if(line.contains("%description%")) {
                            loreAvailable.addAll(config.getStringList("type.description"));
                        } else {
                            loreAvailable.add(line);
                        }
                    }
                    cubeletType.setLoreAvailable(loreAvailable);

                    List<String> loreExpired = new ArrayList<>();
                    for(String line : config.getStringList("type.lore.opening.expired")) {
                        if(line.contains("%description%")) {
                            loreExpired.addAll(config.getStringList("type.description"));
                        } else {
                            loreExpired.add(line);
                        }
                    }
                    cubeletType.setLoreExpired(loreExpired);

                    cubeletType.setDescription(config.getStringList("type.description"));

                    long convertedTime;
                    if(Objects.requireNonNull(config.getString("type.expiration")).equalsIgnoreCase(""))
                        convertedTime = 0;
                    else
                        convertedTime = new TimeAPI(config.getString("type.expiration")).getMilliseconds();
                    cubeletType.setExpireTime(convertedTime);

                    ItemStack key = null;
                    if (config.get("type.key") instanceof MemorySection) {
                        key = XItemStack.deserialize(Utils.getConfigurationSection(config, "type.key"));
                    } else {
                        try {
                            key = ItemStack64.itemStackFromBase64(config.getString("type.key"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    cubeletType.setKey(key);

                }
            }
        }



    }

    private boolean validTypeData(FileConfiguration config) {
        return config.contains("type.name");
    }

    public boolean typeExist(String id) {
        return typesFiles.containsKey(id);
    }

    public void printLog() {

        Main.log.sendMessage(Utils.translate(""));
        Main.log.sendMessage(Utils.translate("  &eLoading cubelets:"));

        int longestWord = longestID();

        for(CubeletType cubeletType : types.values()) {

            String log = "    &a'" + cubeletType.getId() + "' " + getStringSpaces(cubeletType.getId(), longestWord) + "&7► ";

            int rarities = cubeletType.getRarities().size();
            log += (rarities > 0 ? "&a" : "&c") + rarities + " rarities&7, ";

            int rewards = cubeletType.getAllRewards().size();
            log += (rewards > 0 ? "&a" : "&c") + rewards + " rewards";

            Main.log.sendMessage(Utils.translate(log));

        }

        if(types.size() == 0) Main.log.sendMessage(Utils.translate("    &cNo cubelets has been loaded!"));
        Main.log.sendMessage(Utils.translate(""));

    }

    public int longestID() {
        int longest = 0;

        for(String cubeletID : types.keySet()) {
            if(cubeletID.length() > longest) longest = cubeletID.length();
        }

        return longest;
    }

    public String getStringSpaces(String word, int longest) {
        String str = "";

        if(word.length() >= longest) return str;

        str += StringUtils.repeat(" ", longest - word.length());

        return str;
    }

}
