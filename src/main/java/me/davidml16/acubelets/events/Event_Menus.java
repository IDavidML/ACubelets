package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Event_Menus implements Listener {

    private Main main;
    public Event_Menus(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        main.getMenuHandler().handleMenuClick(player, event);

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();

        main.getMenuHandler().handleMenuClose(player);

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {

        Player player = (Player) event.getPlayer();

        main.getMenuHandler().handleMenuDrop(player, event);

    }

}
