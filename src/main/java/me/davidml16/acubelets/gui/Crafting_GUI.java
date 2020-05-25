package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

        for(CraftParent craft : main.getCubeletCraftingHandler().getCrafts()) {
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(craft.getCubeletType());
            boolean playerHaveIngredients = true;

            List<String> lore = new ArrayList<>();
            for (String line : main.getCubeletCraftingHandler().getLore()) {
                if(line.contains("%ingredients%")) {
                    for(CraftIngredient ingredient : craft.getIngrediens()) {

                        String status;
                        if(main.getCubeletCraftingHandler().haveIngredient(p, ingredient))
                            status = main.getCubeletCraftingHandler().getStatusAvailable();
                        else {
                            status = main.getCubeletCraftingHandler().getStatusNoAvailable();
                            playerHaveIngredients = false;
                        }

                        if(ingredient.getCraftType() == CraftType.CUBELET)
                            lore.add(ColorUtil.translate(main.getCubeletCraftingHandler().getIngredientCubeletFormat()
                                    .replaceAll("%name%", ColorUtil.removeColors(main.getCubeletTypesHandler().getTypeBydId(ingredient.getName()).getName()))
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                    }
                } else if(line.contains("%description%")) {
                    for(String desc_line : cubeletType.getDescription())
                        lore.add(ColorUtil.translate(desc_line));
                } else {
                    lore.add(ColorUtil.translate(line));
                }
            }

            ItemStack item = new ItemBuilder(cubeletType.getIcon()).setName(ColorUtil.translate(cubeletType.getName())).setLore(lore).toItemStack();
            item = NBTEditor.setItemTag(item, craft.getCubeletType(), "cubeletType");
            item = NBTEditor.setItemTag(item, Boolean.toString(playerHaveIngredients), "haveIngredients");

            gui.setItem(craft.getSlot(), item);
        }

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(p.getUniqueId()), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (opened.contains(p.getUniqueId())) {
            e.setCancelled(true);

            int inventorySize = main.getCubeletCraftingHandler().getInventorySize();
            int slot = e.getRawSlot();

            if(e.getClick() != ClickType.DOUBLE_CLICK) {
                if (slot >= 0 && slot <= (inventorySize - 10)) {
                    String haveIngredients = NBTEditor.getString(e.getCurrentItem(), "haveIngredients");
                    if (haveIngredients.equalsIgnoreCase("false")) {
                        p.sendMessage(main.getLanguageHandler().getMessage("GUI.Crafting.NoAfford"));
                        return;
                    }
                } else if (slot == (inventorySize - 5)) {
                    main.getCubeletsGUI().open(p);
                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}