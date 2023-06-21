package me.davidml16.acubelets.objects.loothistory;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.utils.ItemStack64;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;

public class RewardHistory {

    private UUID _UUID;
    private String name;

    private ItemStack itemStack;

    public RewardHistory(UUID _UUID, String name, String itemBase64) {

        this._UUID = _UUID;
        this.name = name;

        try {
            this.itemStack = ItemStack64.itemStackFromBase64(itemBase64);
        } catch (IOException e) {
            this.itemStack = XMaterial.BARREL.parseItem();
        }

    }

    public RewardHistory(UUID _UUID, String name, ItemStack itemStack) {
        this._UUID = _UUID;
        this.name = name;
        this.itemStack = itemStack;
    }

    public UUID getUUID() {
        return _UUID;
    }

    public void setUUID(UUID _UUID) {
        this._UUID = _UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

}
