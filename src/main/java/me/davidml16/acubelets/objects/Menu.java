package me.davidml16.acubelets.objects;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Menu {

    private final Main main;
    private final Player owner;
    private final Map<AttrType, Object> attributes = new HashMap<>();
    private Inventory inventory;
    private int size, sizeRows, pageSize, centerSlot, page;
    private List<Integer> pageSlots;

    public Menu(Main main, Player owner) {
        this.main = main;
        this.owner = owner;
    }

    public final void open() {
        owner.updateInventory();
        openPage(0);
    }

    public final void openPage(int page) {
        this.page = page;
        OnPageOpened(page);
    }

    public final void openInventory() {

        main.getMenuHandler().forceRemoveMenu(owner);
        owner.openInventory(inventory);
        main.getMenuHandler().addOpenedMenu(owner, this);
    }

    public Inventory createInventory(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
        return inventory;
    }

    public Inventory createInventory(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(null, type, title);
        return inventory;
    }

    public abstract void OnPageOpened(int page);

    public abstract void OnMenuClick(InventoryClickEvent event);

    public abstract void OnMenuClosed();

    public String translateTitleVariables(String message, int itemsCount) {
        int totalPages = itemsCount / getPageSize();
        totalPages = totalPages > 0 ? totalPages + 1 : 1;
        if (itemsCount > 0 && itemsCount % getPageSize() == 0) totalPages--;
        return message
                .replace("%current_page%", String.valueOf(this.page + 1))
                .replace("%total_pages%", String.valueOf(totalPages));
    }

    public void previousPage() {
        if (page > 0) openPage(--page);
    }

    public void nextPage() {
        openPage(++page);
    }

    public int getNextAvailableSlot() {
        for (int slot : pageSlots)
            if (inventory.getItem(slot) == null) return slot;
        return pageSlots.get(0);
    }

    public void reloadMenu() {
        main.getMenuHandler().reloadAllMenus(this.getClass());
    }

    public void reloadMyMenu() {
        openPage(page);
    }

    public void fillBorders(ItemStack item) {
        int rows = inventory.getSize() / 9;
        for (int i = 0; i < 9; i++) inventory.setItem(i, item);
        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) inventory.setItem(i, item);
        for (int r = 2; r <= rows - 1; r++) {
            inventory.setItem(r * 9 - 1, item);
            inventory.setItem((r - 1) * 9, item);
        }
    }

    public void fillTopSide(ItemStack item, int rows) {
        for (int i = 0; i < 9; i++) inventory.setItem(i, item);
        for (int r = 2; r <= rows; r++) {
            inventory.setItem(r * 9 - 1, item);
            inventory.setItem((r - 1) * 9, item);
        }
    }

    public void fillPage(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null) inventory.setItem(i, item);
    }

    public void playSound(SoundType type) {
        switch (type) {
            case CLICK -> Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.CLICK, 10, 2);
            case CHICKEN_EGG_POP -> Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 10, 3);
            case ANVIL_USE -> Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
            case NOTE_PLING -> Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.NOTE_PLING, 10, 3);
        }
    }

    public Main getMain() {
        return main;
    }

    public Player getOwner() {
        return owner;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int rows) {
        this.sizeRows = rows;
        this.size = rows * 9;
        this.pageSize = (rows - 3) * 7;
        this.centerSlot = 22;
        this.pageSlots = new ArrayList<>();
        for (int r = 2; r <= rows - 2; r++)
            for (int c = 0; c < 7; c++)
                pageSlots.add((r * 9 - 1) - 7 + c);
    }

    public int getSizeRows() {
        return sizeRows;
    }

    public int getCenterSlot() {
        return centerSlot;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Object getAttribute(AttrType type) {
        return attributes.getOrDefault(type, null);
    }

    public void setAttribute(AttrType type, Object value) {
        attributes.put(type, value);
    }

    public Map<AttrType, Object> getAttributes() {
        return attributes;
    }

    public enum AttrType {
        CUBELET_TYPE_ATTR, CUBELET_DISPLAYED_LIST_ATTR, CUBELET_DISPLAYED_ITEMS_ATTR,
        CUBELET_BOX_ATTR, CUSTOM_ID_ATTR, CRAFT_PARENT_ATTR, GIFT_GUISESSION_ATTR,
        ANIMATION_SETTINGS_ATTR, REWARD_ATTR, OPENED_EXTERNALLY_ATTR,
        CUBELET_DISPLAYED_CUBELETS_ATTR
    }

    public enum SoundType {CLICK, CHICKEN_EGG_POP, ANVIL_USE, NOTE_PLING}
}