package me.davidml16.acubelets.data;

import me.davidml16.acubelets.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CubeletType {

    private Main main;

    private String id;
    private String name;

    private ItemStack icon;
    private List<String> lore;

    private Map<String, List<Reward>> rewards;
    private Map<String, Rarity> rarities;

    public CubeletType(Main main, String id, String name) {
        this.main = main;
        this.id = id;
        this.name = name;
        this.rewards = new HashMap<>();
        this.rarities = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<Reward>> getRewards() { return rewards; }

    public List<Reward> getAllRewards() {
        List<Reward> rewards = new ArrayList<>();
        for(String rarity : getRewards().keySet()) {
            rewards.addAll(getRewards().get(rarity));
        }
        return rewards;
    }

    public void setRewards(Map<String, List<Reward>> rewards) { this.rewards = rewards; }

    public Map<String, Rarity> getRarities() { return rarities; }

    public void setRarities(Map<String, Rarity> rarities) { this.rarities = rarities; }

    public ItemStack getIcon() { return icon; }

    public void setIcon(ItemStack icon) { this.icon = icon; }

    public List<String> getLore() { return lore; }

    public void setLore(List<String> lore) { this.lore = lore; }

    @Override
    public String toString() {
        return "CubeletType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", lore=" + lore +
                ", rewards=" + rewards +
                ", rarities=" + rarities +
                '}';
    }

    public void saveType() {
        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        config.set("type.rarities", new ArrayList<>());
        if (config.contains("type.rarities")) {
            List<Rarity> rts = new ArrayList<>(rarities.values());
            rts.sort(Collections.reverseOrder());
            for (Rarity rarity : rts) {
                config.set("type.rarities." + rarity.getId() + ".name", rarity.getName());
                config.set("type.rarities." + rarity.getId() + ".chance", rarity.getChance());
            }
        }

        config.set("type.rewards", new ArrayList<>());
        if (config.contains("type.rewards")) {
            for (Reward reward : getAllRewards()) {
                config.set("type.rewards." + reward.getId() + ".name", reward.getName());
                config.set("type.rewards." + reward.getId() + ".rarity", reward.getRarity().getId());
                config.set("type.rewards." + reward.getId() + ".command", reward.getCommand());
                config.set("type.rewards." + reward.getId() + ".icon", reward.getIcon().getType().name() + ":" + reward.getIcon().getData().getData());
            }
        }

        main.getCubeletTypesHandler().saveConfig(id);
    }

}
