package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.enums.CustomIconType;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class CustomIcon {

    private ItemStack item;
    private String method;
    private String texture;

    private CustomIconType type;

    public CustomIcon(ItemStack item) {
        this.item = item;
        this.type = CustomIconType.ITEM;
    }

    public CustomIcon(String method, String texture) {
        this.method = method;
        this.texture = texture;
        this.type = CustomIconType.SKULL;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public CustomIconType getType() { return type; }

    public ItemStack getItem() {
        ItemStack icon = null;
        switch (type) {
            case ITEM:
                icon = item;
                break;
            case SKULL:
                switch(method.toLowerCase()) {
                    case "base64":
                        icon = SkullCreator.itemFromBase64(texture);
                        break;
                    case "uuid":
                        icon = SkullCreator.itemFromUuid(UUID.fromString(texture));
                        break;
                    case "name":
                        icon = SkullCreator.itemFromName(texture);
                        break;
                }
                break;
        }
        return icon;
    }

}
