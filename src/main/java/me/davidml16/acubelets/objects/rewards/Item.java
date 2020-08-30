package me.davidml16.acubelets.objects.rewards;

import org.bukkit.inventory.ItemStack;

public class Item {

    private String id;
    private ItemStack itemStack;

    public Item(String id, ItemStack itemStack) {
        this.id = id;
        this.itemStack = itemStack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", itemStack=" + itemStack +
                '}';
    }

}
