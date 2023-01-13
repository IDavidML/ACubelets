package me.davidml16.acubelets.objects.rewards;

import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Rarity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reward {

    private String id;
    private String name;
    private Rarity rarity;
    private ItemStack icon;
    private CubeletType parentCubelet;

    private List<CommandObject> commands;
    private List<PermissionObject> permissions;
    private List<ItemObject> items;

    private UUID rewardUUID;

    public Reward(String id, String name, Rarity rarity, ItemStack icon, CubeletType parentCubelet) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.icon = icon;
        this.parentCubelet = parentCubelet;
        this.rewardUUID = UUID.randomUUID();
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

    public UUID getRewardUUID() {
        return rewardUUID;
    }

    public void setRewardUUID(UUID rewardUUID) {
        this.rewardUUID = rewardUUID;
    }

    public List<CommandObject> getCommands() {
        if(commands == null)
            commands = new ArrayList<>();
        return commands;
    }

    public void setCommands(List<CommandObject> commands) {
        this.commands = commands;
    }

    public CommandObject getCommand(String id) {
        return commands.stream().filter(commandObject -> commandObject.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public void recreateCommands() {
        int i = 0;
        for(CommandObject commandObject : getCommands()) {
            commandObject.setId("command-" + i);
            i++;
        }
    }


    public List<PermissionObject> getPermissions() {
        if(permissions == null)
            permissions = new ArrayList<>();
        return permissions;
    }

    public void setPermissions(List<PermissionObject> permissions) {
        this.permissions = permissions;
    }

    public PermissionObject getPermission(String id) {
        return permissions.stream().filter(permissionObject -> permissionObject.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public void recreatePermissions() {
        int i = 0;
        for(PermissionObject permissionObject : getPermissions()) {
            permissionObject.setId("permission-" + i);
            i++;
        }
    }


    public List<ItemObject> getItems() {
        if(items == null)
            items = new ArrayList<>();
        return items;
    }

    public void setItems(List<ItemObject> items) { this.items = items; }

    public ItemObject getItem(String id) {
        return items.stream().filter(itemObject -> itemObject.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public void recreateItems() {
        int i = 0;
        for(ItemObject itemObject : getItems()) {
            itemObject.setId("item-" + i);
            i++;
        }
    }



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
