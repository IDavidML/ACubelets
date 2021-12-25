package me.davidml16.acubelets.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {

    public static String CUBELET_TYPE_ATTR = "CUBELET_TYPE_ATTR";
    public static String CUBELET_DISPLAYED_LIST_ATTR = "CUBELET_DISPLAYED_LIST_ATTR";
    public static String CUBELET_DISPLAYED_ITEMS_ATTR = "CUBELET_DISPLAYED_ITEMS_ATTR";
    public static String CUBELET_BOX_ATTR = "CUBELET_BOX_ATTR";
    public static String CUSTOM_ID_ATTR = "CUSTOM_ID_ATTR";
    public static String CRAFT_PARENT_ATTR = "CRAFT_PARENT_ATTR";
    public static String GIFT_GUISESSION_ATTR = "GIFT_GUISESSION_ATTR";
    public static String GIFT_AMOUNTGUISESSION_ATTR = "GIFT_AMOUNTGUISESSION_ATTR";
    public static String ANIMATION_SETTINGS_ATTR = "ANIMATION_SETTINGS_ATTR";
    public static String REWARD_ATTR = "REWARD_ATTR";
    public static String OPENED_EXTERNALLY_ATTR = "OPENED_EXTERNALLY_ATTR";

    private Player owner;

    private Inventory inventory;
    private int page;

    private Map<String, Object> attributes;

    public Menu(Player owner) {
        this.owner = owner;
        this.attributes = new HashMap<>();
    }

    public void open() {

        openPage(0);

    }

    public void openPage(int page) {

        this.page = page;

        OnPageOpened(page);

    }

    public abstract void OnPageOpened(int page);

    public abstract void OnMenuClick(InventoryClickEvent event);

    public abstract void OnMenuClosed();

}
