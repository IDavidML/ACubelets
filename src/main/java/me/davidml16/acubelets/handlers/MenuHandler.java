package me.davidml16.acubelets.handlers;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MenuHandler {

    private final Main main;

    private final Map<UUID, Menu> openedMenus = new ConcurrentHashMap<>();

    private ClickType rewardPreviewClickType = ClickType.SHIFT_LEFT;

    public MenuHandler(Main main) {
        this.main = main;
    }

    // ── State management ───────────────────────────────────────────────────

    public void addOpenedMenu(Player player, Menu menu) {
        openedMenus.put(player.getUniqueId(), menu);
    }

    public void forceRemoveMenu(Player player) {
        openedMenus.remove(player.getUniqueId());
    }

    public boolean hasOpenedMenu(Player player) {
        return openedMenus.containsKey(player.getUniqueId());
    }

    public boolean hasOpenedMenu(Player player, Class<? extends Menu> menuClass) {
        Menu menu = getOpenedMenu(player);
        return menu != null && menu.getClass().equals(menuClass);
    }

    public Menu getOpenedMenu(Player player) {
        return openedMenus.get(player.getUniqueId());
    }

    public Map<UUID, Menu> getOpenedMenus() {
        return Collections.unmodifiableMap(openedMenus);
    }

    public ClickType getRewardPreviewClickType() {
        return rewardPreviewClickType;
    }

    public void setClickType(String clickType) {
        Set<String> valid = Set.of("LEFT", "RIGHT", "MIDDLE", "SHIFT_LEFT", "SHIFT_RIGHT");
        rewardPreviewClickType = valid.contains(clickType.toUpperCase())
                ? ClickType.valueOf(clickType.toUpperCase())
                : ClickType.SHIFT_LEFT;
    }

    public void handleMenuClick(Player player, InventoryClickEvent event) {
        Menu menu = getOpenedMenu(player);
        if (menu == null) return;

        event.setCancelled(true);

        ClickType click = event.getClick();

        if (event.getRawSlot() == -999) {
            player.updateInventory();
            return;
        }

        if (click == ClickType.NUMBER_KEY || click == ClickType.DOUBLE_CLICK) {
            scheduleInventoryCleanup(player);
            return;
        }

        int topSize = player.getOpenInventory().getTopInventory().getSize();
        if (event.getRawSlot() >= topSize) {
            player.updateInventory();
            return;
        }

        menu.OnMenuClick(event);

        if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)
            scheduleInventoryCleanup(player);
        else
            player.updateInventory();
    }

    public void handleMenuClose(Player player) {
        Menu menu = getOpenedMenu(player);
        if (menu == null) return;

        menu.OnMenuClosed();
        cleanupAcubeletsFromPlayer(player);
        openedMenus.remove(player.getUniqueId());
    }

    public void handleMenuDrop(Player player, PlayerDropItemEvent event) {
        if (hasOpenedMenu(player)) event.setCancelled(true);
    }

    public void reloadAllMenus(Player player, Class<? extends Menu> menuClass) {
        Menu menu = getOpenedMenu(player);
        if (menu != null && menu.getClass().equals(menuClass)) menu.reloadMyMenu();
    }

    public void reloadAllMenus(Class<? extends Menu> menuClass) {
        openedMenus.values().stream()
                .filter(m -> m.getClass().equals(menuClass))
                .forEach(Menu::reloadMyMenu);
    }

    public void reloadAllMenus() {
        openedMenus.values().forEach(Menu::reloadMyMenu);
    }

    private void scheduleInventoryCleanup(Player player) {
        main.getServer().getScheduler().runTaskLater(main, () -> {
            cleanupAcubeletsFromPlayer(player);
            player.updateInventory();
        }, 1L);
    }

    private void cleanupAcubeletsFromPlayer(Player player) {
        ItemStack cursor = player.getItemOnCursor();
        if (isGhostItem(cursor)) {
            player.setItemOnCursor(new ItemStack(Material.AIR));
        }

        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if (isGhostItem(contents[i])) player.getInventory().setItem(i, null);
        }
    }

    private boolean isGhostItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        return NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "action")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "cubeletUUID")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "typeID")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "cubeletType")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "rewardID")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "animation")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "status")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "randomUUID")
                || NBTEditor.contains(item, NBTEditor.CUSTOM_DATA, "haveIngredients");
    }
}