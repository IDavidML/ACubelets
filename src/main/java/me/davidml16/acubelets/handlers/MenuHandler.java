package me.davidml16.acubelets.handlers;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.gui.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;

public class MenuHandler {

    private Main main;

    private Map<Player, Menu> openedMenus;

    public MenuHandler(Main main) {
        this.main = main;
        this.openedMenus = new HashMap<>();
    }

    public Map<Player, Menu> getOpenedMenus() {
        return openedMenus;
    }

    public void setOpenedMenus(Map<Player, Menu> openedMenus) {
        this.openedMenus = openedMenus;
    }

    public boolean hasOpenedMenu(Player player) {
        return openedMenus.containsKey(player);
    }

    public Menu getOpenedMenu(Player player) {
        return hasOpenedMenu(player) ? openedMenus.get(player) : null;
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

    }

}
