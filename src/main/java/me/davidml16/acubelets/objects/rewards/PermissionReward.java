package me.davidml16.acubelets.objects.rewards;

import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.inventory.ItemStack;

public class PermissionReward extends Reward {

    private String permission;

    public PermissionReward(String id, String name, Rarity rarity, String permission, ItemStack icon, CubeletType parentCubelet) {
        super(id, name, rarity, icon, parentCubelet);
        this.permission = permission;
    }

    public String getPermission() { return permission; }

    public void setPermission(String permission) { this.permission = permission; }

    @Override
    public String toString() {
        return "PermissionReward{" +
                super.toString() +
                ", permission='" + permission + '\'' +
                '}';
    }
}
