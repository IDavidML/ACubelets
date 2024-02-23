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

    public enum AttrType {
        CUBELET_TYPE_ATTR, CUBELET_DISPLAYED_LIST_ATTR, CUBELET_DISPLAYED_ITEMS_ATTR, CUBELET_BOX_ATTR, CUSTOM_ID_ATTR, CRAFT_PARENT_ATTR,
        GIFT_GUISESSION_ATTR, ANIMATION_SETTINGS_ATTR, REWARD_ATTR, OPENED_EXTERNALLY_ATTR, CUBELET_DISPLAYED_CUBELETS_ATTR
    }

    public enum SoundType { CLICK, CHICKEN_EGG_POP, ANVIL_USE, NOTE_PLING };

    private Main main;
    private Player owner;

    private Inventory inventory;
    private int size;
    private int sizeRows;
    private int pageSize;
    private int centerSlot;
    private int page;

    private List<Integer> pageSlots;

    private Map<AttrType, Object> attributes;

    private boolean removeFromOppened;

    public Menu(Main main, Player owner) {
        this.main = main;
        this.owner = owner;
        this.attributes = new HashMap<>();
        this.removeFromOppened = true;
    }

    public void open() {

        owner.updateInventory();

        openPage(0);

    }

    public void openPage(int page) {

        this.page = page;

        OnPageOpened(page);

    }

    public void openInventory() {

        owner.openInventory(inventory);

        Bukkit.getScheduler().runTaskLater(main, () -> {
            main.getMenuHandler().addOpenedMenu(owner, this);
        }, 1L);

    }

    public Inventory createInventory(int size, String title) {

        this.inventory = Bukkit.createInventory(null, size, title);

        return inventory;

    }

    public Inventory createInventory(InventoryType inventoryType, String title) {

        this.inventory = Bukkit.createInventory(null, inventoryType, title);

        return inventory;

    }

    public abstract void OnPageOpened(int page);

    public abstract void OnMenuClick(InventoryClickEvent event);

    public abstract void OnMenuClosed();

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.sizeRows = size;
        this.size = size * 9;
        this.pageSize = (size - 3) * 7;
        this.centerSlot = 22;

        this.pageSlots = new ArrayList<>();

        for (int row = 2; row <= getSizeRows() - 2; row++) {
            for (int column = 0; column < 7; column++) {
                int slot = (((row * 9) - 1) - 7) + column;
                pageSlots.add(slot);
            }
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSizeRows() {
        return sizeRows;
    }

    public int getCenterSlot() {
        return centerSlot;
    }

    public int getNextAvailableSlot() {
        for(int slot : pageSlots) {
            if(inventory.getItem(slot) == null) return slot;
        }

        return pageSlots.get(0);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Map<AttrType, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<AttrType, Object> attributes) {
        this.attributes = attributes;
    }

    public Object getAttribute(AttrType attrType) {
        return this.attributes.getOrDefault(attrType, null);
    }

    public void setAttribute(AttrType attrType, Object value) {
        this.attributes.put(attrType, value);
    }

    public boolean isRemoveFromOppened() {
        return removeFromOppened;
    }

    public void setRemoveFromOppened(boolean removeFromOppened) {
        this.removeFromOppened = removeFromOppened;
    }

    public void reloadMenu() {

        for(Menu menu : getMain().getMenuHandler().getOpenedMenus().values()) {

            if(!menu.getClass().equals(this.getClass())) continue;

            menu.openPage(menu.getPage());

        }

    }

    public void reloadMyMenu() {

        openPage(page);

    }

    public void fillBorders(ItemStack item) {

        int size = inventory.getSize();
        int rows = (size + 1) / 9;

        // Fill top
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }

        // Fill bottom
        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, item);
        }

        // Fill sides
        for (int i = 2; i <= rows - 1; i++) {
            int[] slots = new int[]{i * 9 - 1, (i - 1) * 9};
            inventory.setItem(slots[0], item);
            inventory.setItem(slots[1], item);
        }

    }

    public void fillTopSide(ItemStack item, int rows) {

        // Fill top
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, item);
        }

        // Fill sides
        for (int i = 2; i <= rows; i++) {
            int[] slots = new int[]{i * 9 - 1, (i - 1) * 9};
            inventory.setItem(slots[0], item);
            inventory.setItem(slots[1], item);
        }

    }

    public void fillPage(ItemStack item) {

        int size = inventory.getSize();

        for (int i = 0; i < size; i++) {

            if(inventory.getItem(i) != null)
                continue;

            inventory.setItem(i, item);

        }

    }

    public void previousPage() {

        if(page == 0) return;

        page--;

        openPage(page);

    }

    public void nextPage() {

        page++;

        openPage(page);

    }

    public void playSound(SoundType soundType) {

        switch (soundType) {

            case CLICK:
                Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.CLICK, 10, 2);
                break;

            case CHICKEN_EGG_POP:
                Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 10, 3);
                break;

            case ANVIL_USE:
                Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
                break;

            case NOTE_PLING:
                Sounds.playSound(owner, owner.getLocation(), Sounds.MySound.NOTE_PLING, 10, 3);
                break;

        }

    }

}
