package me.davidml16.acubelets.objects;

import org.bukkit.inventory.ItemStack;

public class DisplayedCubelet {
    private Cubelet cubelet;
    private ItemStack item;
    private int slot;

    public DisplayedCubelet(Cubelet cubelet, ItemStack item) {
        this(cubelet, item, -1);
    }

    public DisplayedCubelet(Cubelet cubelet, ItemStack item, int slot) {
        this.cubelet = cubelet;
        this.item = item;
        this.slot = slot;
    }

    public Cubelet getCubelet() {
        return cubelet;
    }

    public void setCubelet(Cubelet cubelet) {
        this.cubelet = cubelet;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "DisplayedCubelet{" +
                "cubelet=" + cubelet +
                ", item=" + item +
                ", slot=" + slot +
                '}';
    }
}
