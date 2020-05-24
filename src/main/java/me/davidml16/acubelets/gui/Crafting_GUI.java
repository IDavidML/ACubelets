package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Crafting_GUI implements Listener {

    private Set<UUID> opened;

    private Main main;

    public Crafting_GUI(Main main) {
        this.main = main;
        this.opened = new HashSet<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public Set<UUID> getOpened() {
        return opened;
    }

    public void open(Player p) {
        p.updateInventory();

        int inventorySize = main.getCubeletCraftingHandler().getInventorySize();

        Inventory gui = Bukkit.createInventory(null, inventorySize, main.getLanguageHandler().getMessage("GUI.Crafting.Title"));

        ItemStack back = new ItemBuilder(XMaterial.BOOK.parseItem())
                .setName(main.getLanguageHandler().getMessage("GUI.Crafting.Items.Back.Name"))
                .setLore(main.getLanguageHandler().getMessageList("GUI.Crafting.Items.Back.Lore"))
                .toItemStack();
        gui.setItem(inventorySize - 5, back);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(p.getUniqueId()), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.contains(p.getUniqueId())) {
            e.setCancelled(true);

            int inventorySize = main.getCubeletCraftingHandler().getInventorySize();
            int slot = e.getRawSlot();

            if (slot == (inventorySize - 5)) {
                main.getCubeletsGUI().open(p);
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}