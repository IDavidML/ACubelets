package me.davidml16.acubelets.objects.rewards;

import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.inventory.ItemStack;

public class Reward {

    private String id;
    private String name;
    private Rarity rarity;
    private ItemStack icon;
    private CubeletType parentCubelet;

    public Reward(String id, String name, Rarity rarity, ItemStack icon, CubeletType parentCubelet) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.icon = icon;
        this.parentCubelet = parentCubelet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Rarity getRarity() { return rarity; }

    public void setRarity(Rarity rarity) { this.rarity = rarity; }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public CubeletType getParentCubelet() { return parentCubelet; }

    public void setParentCubelet(CubeletType parentCubelet) { this.parentCubelet = parentCubelet; }

    @Override
    public String toString() {
        return "Reward{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rarity=" + rarity +
                ", icon=" + icon +
                '}';
    }

}
