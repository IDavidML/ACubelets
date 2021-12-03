package me.davidml16.acubelets.gui.options;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class OptionsMain_GUI implements Listener {

    private ArrayList<UUID> opened;

    private Main main;

    public OptionsMain_GUI(Main main) {
        this.main = main;
        this.opened = new ArrayList<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public ArrayList<UUID> getOpened() {
        return opened;
    }

    public void open(Player player) {

        Inventory gui = Bukkit.createInventory(null, 45, "Options | Main");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        gui.setItem(4, new ItemBuilder(XMaterial.NAME_TAG.parseItem())
                .setName(Utils.translate("&aOptions menu"))
                .setLore(
                        "",
                        Utils.translate(" &7Here you can change "),
                        Utils.translate(" &7animation settings. "),
                        Utils.translate(" &7and crafting. "),
                        ""
                ).toItemStack());

        gui.setItem(19, new ItemBuilder(XMaterial.ARMOR_STAND.parseItem())
                .setName(Utils.translate("&aAnimations"))
                .setLore(
                        "",
                        Utils.translate(" &7Open animations gui to "),
                        Utils.translate(" &7change animations settings. "),
                        "",
                        Utils.translate("&eClick to config animations! ")
                ).toItemStack());

        int available = main.getCubeletCraftingHandler().getCrafts().size();
        gui.setItem(21, new ItemBuilder(XMaterial.ITEM_FRAME.parseItem())
                .setName(Utils.translate("&aCrafting"))
                .setLore(
                        "",
                        Utils.translate(" &7Open crafting gui to "),
                        Utils.translate(" &7crafting settings. "),
                        "",
                        Utils.translate(" &7Crafts created: " + (available > 0 ? "&6" : "&c") + available),
                        "",
                        Utils.translate("&eClick to config crafting! ")
                ).toItemStack());

        gui.setItem(23, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem())
                .setName(Utils.translate("&4&lComing soon..."))
                .toItemStack());

        gui.setItem(25, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem())
                .setName(Utils.translate("&4&lComing soon..."))
                .toItemStack());

        gui.setItem(40, new ItemBuilder(XMaterial.BOOK.parseItem()).setName(Utils.translate("&aClose")).toItemStack());

        for (int i = 0; i < 45; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        player.openInventory(gui);

        Sounds.playSound(player, player.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(player.getUniqueId()), 1L);

    }

    public void reloadGUI() {

        for(UUID uuid : opened) {

            Player player = Bukkit.getPlayer(uuid);

            open(player);

        }

    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.contains(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            int slot = e.getRawSlot();

            if (slot == 19) {
                main.getOptionsAnimationsGUI().open(p);
            } else if (slot == 21) {
                main.getEditCraftingCraftsGUI().open(p);
            } else if (slot == 40) {
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}