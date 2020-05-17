package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.api.CubeletReceivedEvent;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.TimeAPI.TimeAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

        Main.log.sendMessage(ColorUtil.translate(""));
        Main.log.sendMessage(ColorUtil.translate("  &eLoading types:"));
        File[] allFiles = new File(main.getDataFolder(), "types").listFiles();
        for (File file : allFiles) {
            String id = file.getName().toLowerCase().replace(".yml", "");

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


                    if (!config.contains("type.rarities")) {
                        config.set("type.rarities", new ArrayList<>());
                    }

                    if (!config.contains("type.rewards")) {
                        config.set("type.rewards", new ArrayList<>());
                    }

                    saveConfig(id);

                    String name = config.getString("type.name");

                    CubeletType cubeletType = new CubeletType(main, id, name);
                    types.put(id, cubeletType);

                    cubeletType.setAnimation(config.getString("type.animation").toLowerCase());

                    String[] icon = ((String) config.get("type.icon.texture")).split(":");
                    switch(icon[0].toLowerCase()) {
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

                    long convertedTime;
                    if(Objects.requireNonNull(config.getString("type.expiration")).equalsIgnoreCase(""))
                        convertedTime = 0;
                    else
                        convertedTime = new TimeAPI(config.getString("type.expiration")).getMilliseconds();
                    cubeletType.setExpireTime(convertedTime);

                    Main.log.sendMessage(ColorUtil.translate("    &a'" + id + "' &7- &aCubelet type loaded!"));
                } else {
                    Main.log.sendMessage(ColorUtil.translate("    &c'" + id + "' not loaded because cubelet type data is not correct!"));
                }
            } else {
                Main.log.sendMessage(ColorUtil.translate("    &c'" + id + "' not loaded because cubelet type id starts with a number!"));
            }
        }

        if(types.size() == 0)
            Main.log.sendMessage(ColorUtil.translate("    &cNo cubelet type has been loaded!"));

    }

    private boolean validTypeData(FileConfiguration config) {
        return config.contains("type.name");
    }

    public boolean typeExist(String id) {
        return typesFiles.containsKey(id);
    }

    public void giveCubelet(String player, String type, int amount) throws SQLException {
        if (main.getCubeletTypesHandler().getTypes().containsKey(type)) {
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
            UUID uuid = UUID.fromString(main.getDatabaseHandler().getPlayerUUID(player));
            if (amount > 0) {
                for (int i = 1; i <= amount; i++) {
                    try {
                        Cubelet cubelet = new Cubelet(cubeletType);
                        main.getDatabaseHandler().addCubelet(uuid, cubelet.getUuid(), cubelet.getType(), cubelet.getReceived(), cubelet.getExpire());
                        if(Bukkit.getPlayer(uuid) != null) {
                            Player target = Bukkit.getPlayer(uuid);
                            main.getPlayerDataHandler().getData(Objects.requireNonNull(target)).getCubelets().add(cubelet);

                            if(main.getCubeletsGUI().getOpened().containsKey(uuid)) {
                                main.getCubeletsGUI().reloadPage(target);
                            }

                            Bukkit.getPluginManager().callEvent(new CubeletReceivedEvent(target, getTypeBydId(type), amount));
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }
    }

}
