package me.davidml16.acubelets.gui.crafting;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.crafting.CraftParentMenu;
import me.davidml16.acubelets.conversation.crafting.EditCraftParentMenu;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class EditCrafting_Crafts_GUI implements Listener {

    private HashMap<UUID, Integer> opened;
    private List<Integer> borders;

    private Main main;

    public EditCrafting_Crafts_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, Integer>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    public void reloadGUI() {
        for(UUID uuid : opened.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            openPage(p, opened.get(uuid));
        }
    }

    private void openPage(Player p, int page) {
        List<CraftParent> craftParents = main.getCubeletCraftingHandler().getCrafts();

        if(page > 0 && craftParents.size() < (page * 14) + 1) {
            openPage(p, page - 1);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 36, "Crafting editor » Crafts");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newReward = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new craft")).toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack")).toItemStack();

        for (Integer i : borders) {
            gui.setItem(i, edge);
        }

        gui.setItem(30, newReward);
        gui.setItem(32, back);

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(27, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(27, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (craftParents.size() > (page + 1) * 14) {
            gui.setItem(35, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(35, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (craftParents.size() > 14) craftParents = craftParents.subList(page * 14, ((page * 14) + 14) > craftParents.size() ? craftParents.size() : (page * 14) + 14);

        if(craftParents.size() > 0) {
            for (CraftParent craftParent : craftParents) {
                gui.addItem(new ItemBuilder(main.getCubeletTypesHandler().getTypeBydId(craftParent.getCubeletType()).getIcon())
                        .setName(Utils.translate("&a" + craftParent.getCubeletType()))
                        .setLore(
                                "",
                                Utils.translate(" &7Cubelet: &6" + craftParent.getCubeletType() + " "),
                                Utils.translate(" &7Slot: &6" + craftParent.getSlot() + " "),
                                Utils.translate(" &7Ingredients: " + (craftParent.getIngrediens().size() > 0 ? "&6" : "&c") + craftParent.getIngrediens().size()),
                                "",
                                Utils.translate("&eLeft-Click » &aEdit ingredients "),
                                Utils.translate("&eMiddle-Click » &aRemove craft "),
                                Utils.translate("&eRight-Click » &aEdit craft ")
                        ).toItemStack());
            }
        } else {
            ItemStack noCreated = new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny craft created")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7craft created. "),
                    ""
            ).toItemStack();

            for (int i = 10; i <= 16; i++)
                gui.setItem(i, noCreated);
            for (int i = 19; i <= 25; i++)
                gui.setItem(i, noCreated);
        }

        if (!opened.containsKey(p.getUniqueId())) {
            p.openInventory(gui);
        } else {
            p.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), page), 1L);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), 0), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (slot == 27 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, opened.get(p.getUniqueId()) - 1);
            } else if (slot == 35 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, opened.get(p.getUniqueId()) + 1);
            } else if (slot == 30) {
                p.closeInventory();
                new CraftParentMenu(main).getConversation(p).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 100, 3);
            } else if (slot == 32) {
                main.getEditCraftingGUI().open(p);
            } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25)) {
                if (e.getCurrentItem().getType() == Material.AIR) return;

                if (main.getCubeletCraftingHandler().getCrafts().size() == 0) return;

                String craftID = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                CraftParent craftParent = main.getCubeletCraftingHandler().getCraftById(craftID);

                if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {

                    main.getEditCraftingIngredientsGUI().open(p, craftParent);

                } else if(e.getClick() == ClickType.MIDDLE) {

                    main.getCubeletCraftingHandler().getCrafts().remove(craftParent);
                    main.getCubeletCraftingHandler().saveCrafting();
                    reloadGUI();
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);


                } else if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {

                    p.closeInventory();
                    new EditCraftParentMenu(main).getConversation(p, craftParent).begin();
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);

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
