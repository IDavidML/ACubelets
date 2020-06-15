package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
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

import java.sql.SQLException;
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

        GUILayout guiLayout = main.getLayoutHandler().getLayout("crafting");

        Inventory gui = Bukkit.createInventory(null, inventorySize, guiLayout.getMessage("Title"));

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        gui.setItem((inventorySize - 10) + guiLayout.getSlot("Back"), back);

        List<String> lorePoints = new ArrayList<>();
        for (String line : guiLayout.getMessageList("Items.Points.Lore")) {
            lorePoints.add(line.replaceAll("%points_available%", ""+main.getPlayerDataHandler().getData(p).getLootPoints()));
        }
        ItemStack points = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Points.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Points.Name"))
                .setLore(lorePoints)
                .toItemStack();
        gui.setItem((inventorySize - 10) + guiLayout.getSlot("Points"), points);

        for(CraftParent craft : main.getCubeletCraftingHandler().getCrafts()) {
            CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(craft.getCubeletType());

            List<String> lore = new ArrayList<>();
            for (String line : guiLayout.getMessageList("Ingredients.Lore")) {
                if(line.contains("%ingredients%")) {
                    for(CraftIngredient ingredient : craft.getIngrediens()) {

                        String status;
                        if(main.getCubeletCraftingHandler().haveIngredient(p, ingredient))
                            status = guiLayout.getMessage("Ingredients.Status.Available");
                        else
                            status = guiLayout.getMessage("Ingredients.Status.NotAvailable");

                        if(ingredient.getCraftType() == CraftType.CUBELET)
                            lore.add(ColorUtil.translate(guiLayout.getMessage("Ingredients.Ingredient.Cubelet")
                                    .replaceAll("%name%", ColorUtil.removeColors(main.getCubeletTypesHandler().getTypeBydId(ingredient.getName()).getName()))
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                        else if(ingredient.getCraftType() == CraftType.MONEY)
                            lore.add(ColorUtil.translate(guiLayout.getMessage("Ingredients.Ingredient.Money")
                                    .replaceAll("%amount%", ""+ingredient.getAmount())
                                    .replaceAll("%status%", status)
                            ));
                        else if(ingredient.getCraftType() == CraftType.POINTS)
                            lore.add(ColorUtil.translate(guiLayout.getMessage("Ingredients.Ingredient.Points")
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
            item = NBTEditor.setItemTag(item, Boolean.toString(main.getCubeletCraftingHandler().haveIngredients(p, craft)), "haveIngredients");

            if(craft.getIngrediens().size() > 0) gui.setItem(craft.getSlot(), item);
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
            GUILayout guiLayout = main.getLayoutHandler().getLayout("crafting");

            if(e.getClick() != ClickType.DOUBLE_CLICK) {
                if (slot >= 0 && slot <= (inventorySize - 10)) {
                    String haveIngredients = NBTEditor.getString(e.getCurrentItem(), "haveIngredients");
                    if (haveIngredients.equalsIgnoreCase("false")) {
                        p.sendMessage(main.getLayoutHandler().getLayout("crafting").getMessage("NoAfford"));
                        return;
                    }

                    String cubeletType = NBTEditor.getString(e.getCurrentItem(), "cubeletType");
                    main.getCraftingConfirmationGUI().open(p, cubeletType);
                } else if (slot == ((inventorySize - 10) + guiLayout.getSlot("Back"))) {
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