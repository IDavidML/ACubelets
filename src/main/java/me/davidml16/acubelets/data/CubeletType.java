package me.davidml16.acubelets.data;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeletType {

    private String id;
    private String name;

    private ItemStack icon;
    private List<String> lore;

    private Map<String, List<Reward>> rewards;
    private Map<String, Rarity> rarities;

    public CubeletType(String id, String name) {
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

}
