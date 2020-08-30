package me.davidml16.acubelets.objects.rewards;

import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemReward extends Reward {

    private List<Item> items;

    public ItemReward(String id, String name, Rarity rarity, List<Item> items, ItemStack icon, CubeletType parentCubelet) {
        super(id, name, rarity, icon, parentCubelet);
        this.items = items;
    }

    public List<Item> getItems() { return items; }

    public void setItems(List<Item> items) { this.items = items; }

    public Item getItem(String id) {
        for(Item item : getItems())
            if(item.getId().equalsIgnoreCase(id))
                return item;
        return null;
    }

    @Override
    public String toString() {
        return "ItemReward{" +
                "items=" + items +
                '}';
    }

}
