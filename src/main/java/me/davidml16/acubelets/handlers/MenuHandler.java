package me.davidml16.acubelets.handlers;

import com.cryptomorin.xseries.XItemStack;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MenuHandler {

    private Main main;

    private Map<Player, Menu> openedMenus;

    private ClickType rewardPreviewClickType;

    public MenuHandler(Main main) {

        this.main = main;
        this.openedMenus = new HashMap<>();
        this.rewardPreviewClickType = ClickType.SHIFT_LEFT;
    }

    public Map<Player, Menu> getOpenedMenus() {
        return openedMenus;
    }

    public void setOpenedMenus(Map<Player, Menu> openedMenus) {
        this.openedMenus = openedMenus;
    }

    public void addOpenedMenu(Player player, Menu menu) {
        this.openedMenus.put(player, menu);
    }

    public boolean hasOpenedMenu(Player player) {
        return openedMenus.containsKey(player);
    }

    public boolean hasOpenedMenu(Player player, Class<? extends Menu> menuClass) {

        Menu menu = getOpenedMenu(player);

        if(menu == null) return false;

        return menu.getClass().equals(menuClass);

    }

    public Menu getOpenedMenu(Player player) {
        return hasOpenedMenu(player) ? openedMenus.get(player) : null;
    }

    public ClickType getRewardPreviewClickType() {
        return rewardPreviewClickType;
    }

    public void setClickType(String clickType) {
        if(Arrays.asList("LEFT", "RIGHT", "MIDDLE", "SHIFT_LEFT", "SHIFT_RIGHT").contains(clickType))
            this.rewardPreviewClickType = ClickType.valueOf(clickType.toUpperCase());
        else
            this.rewardPreviewClickType = ClickType.SHIFT_LEFT;
    }

    public void handleMenuClick(Player player, InventoryClickEvent event) {

        Menu menu = getOpenedMenu(player);

        if(menu == null)
            return;

        menu.OnMenuClick(event);
    }

    public void handleMenuClose(Player player) {

        Menu menu = getOpenedMenu(player);

        if(menu == null)
            return;

        menu.OnMenuClosed();

        openedMenus.remove(player);
    }

    public void reloadAllMenus(Player player, Class<? extends Menu> menuClass) {

        Menu menu = getOpenedMenu(player);

        if(menu == null) return;

        if(!menu.getClass().equals(menuClass)) return;

        menu.reloadMyMenu();
    }

    public void reloadAllMenus(Class<? extends Menu> menuClass) {

        for (Iterator<Menu> it = openedMenus.values().iterator(); it.hasNext(); ) {
            Menu menu = it.next();

            if(!menu.getClass().equals(menuClass)) continue;

            menu.reloadMyMenu();
        }

    }

    public void reloadAllMenus() {

        for (Iterator<Menu> it = openedMenus.values().iterator(); it.hasNext(); ) {
            Menu menu = it.next();
            menu.reloadMyMenu();
        }

    }

}
