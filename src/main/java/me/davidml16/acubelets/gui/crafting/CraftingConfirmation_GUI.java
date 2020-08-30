package me.davidml16.acubelets.gui.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.objects.CraftIngredient;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class CraftingConfirmation_GUI implements Listener {

    private HashMap<UUID, String> opened;

    private Main main;

    public CraftingConfirmation_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, String>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, String> getOpened() {
        return opened;
    }

    public void open(Player p, String id) {
        p.updateInventory();

        GUILayout guiLayout = main.getLayoutHandler().getLayout("craftingconfirmation");

        Inventory gui = Bukkit.createInventory(null, InventoryType.HOPPER, guiLayout.getMessage("Title"));
        CraftParent craftParent = main.getCubeletCraftingHandler().getCraftById(id);

        List<String> lore = new ArrayList<>();
        for (String line : guiLayout.getMessageList("Items.Craft.Lore")) {
            if(line.contains("%ingredients%")) {
                for(CraftIngredient ingredient : craftParent.getIngrediens()) {
                    if(ingredient.getCraftType() == CraftType.CUBELET)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Cubelet")
                                .replaceAll("%name%", Utils.removeColors(main.getCubeletTypesHandler().getTypeBydId(ingredient.getName()).getName()))
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));
                    else if(ingredient.getCraftType() == CraftType.MONEY)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Money")
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));
                    else if(ingredient.getCraftType() == CraftType.POINTS)
                        lore.add(Utils.translate(guiLayout.getMessage("Ingredients.Ingredient.Points")
                                .replaceAll("%amount%", ""+ingredient.getAmount())
                        ));
                }
            } else {
                lore.add(Utils.translate(line));
            }
        }

        ItemStack craft = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Craft.Material")).get().parseItem())
                .setName(Utils.translate(guiLayout.getMessage("Items.Craft.Name")))
                .setLore(lore)
                .toItemStack();

        ItemStack cancel = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Cancel.Material")).get().parseItem())
                .setName(Utils.translate(guiLayout.getMessage("Items.Cancel.Name")))
                .setLore(guiLayout.getMessageList("Items.Cancel.Lore"))
                .toItemStack();

        gui.setItem(1, craft);
        gui.setItem(3, cancel);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), id), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if(slot == 1) {
                String cubeletType = opened.get(p.getUniqueId());
                CraftParent craft = main.getCubeletCraftingHandler().getCraftById(cubeletType);

                if(!main.getCubeletCraftingHandler().haveIngredients(p, craft)) {
                    p.sendMessage(main.getLayoutHandler().getLayout("crafting").getMessage("NoAfford"));
                    return;
                }

                main.getCubeletCraftingHandler().removeIngredients(p, craft);
                try {
                    main.getCubeletTypesHandler().giveCubelet(p.getUniqueId(), craft.getCubeletType(), 1);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                main.getCubeletsGUI().open(p);
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);
            } else if(slot == 3) {
                main.getCraftingGUI().open(p);
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}