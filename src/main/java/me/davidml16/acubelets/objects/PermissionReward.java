package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.interfaces.Reward;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PermissionReward extends Reward {

    private String permission;

    public PermissionReward(String id, String name, Rarity rarity, String permission, ItemStack icon) {
        super(id, name, rarity, icon);
        this.permission = permission;
    }

    public String getPermission() { return permission; }

    public void setPermission(String permission) { this.permission = permission; }

}
