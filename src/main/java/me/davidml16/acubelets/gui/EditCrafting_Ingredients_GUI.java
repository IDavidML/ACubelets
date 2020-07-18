package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.*;
import me.davidml16.acubelets.enums.CraftType;
import me.davidml16.acubelets.interfaces.Reward;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.NBTEditor;
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

import java.io.File;
import java.util.*;

public class EditCrafting_Ingredients_GUI implements Listener {

    class InventorySession {

        private CraftParent craftParent;
        private int page;

        public InventorySession(CraftParent craftParent, int page) {
            this.craftParent = craftParent;
            this.page = page;
        }

        public CraftParent getCraftParent() { return craftParent; }

        public int getPage() { return page; }

    }

    private HashMap<UUID, InventorySession> opened;
    private List<Integer> borders;

    private Main main;

    public EditCrafting_Ingredients_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, InventorySession>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, InventorySession> getOpened() {
        return opened;
    }

    public void reloadGUI(CraftParent craftParent) {
        for(UUID uuid : opened.keySet()) {
            if(opened.get(uuid).getCraftParent().equals(craftParent)) {
                Player p = Bukkit.getPlayer(uuid);
                openPage(p, craftParent, opened.get(uuid).getPage());
            }
        }
    }

    private void openPage(Player p, CraftParent craftParent, int page) {
        List<CraftIngredient> ingredients = craftParent.getIngrediens();

        if(page > 0 && ingredients.size() < (page * 21) + 1) {
            openPage(p, craftParent, page - 1);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 36, "Crafting editor » Ingredients");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newReward = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(ColorUtil.translate("&aCreate new ingredient")).toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(ColorUtil.translate("&aBack")).toItemStack();

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
            gui.setItem(27, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(ColorUtil.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(27, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (ingredients.size() > (page + 1) * 14) {
            gui.setItem(35, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(ColorUtil.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(35, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (ingredients.size() > 14) ingredients = ingredients.subList(page * 14, ((page * 14) + 14) > ingredients.size() ? ingredients.size() : (page * 14) + 14);

        if(ingredients.size() > 0) {
            for (CraftIngredient craftIngredient : ingredients) {

                List<String> lore;
                if(craftIngredient.getCraftType() == CraftType.CUBELET) {
                    lore = Arrays.asList("",
                            ColorUtil.translate(" &7Parent: &6" + craftIngredient.getParentType() + " "),
                            ColorUtil.translate(" &7Type: &6" + craftIngredient.getCraftType().toString() + " "),
                            ColorUtil.translate(" &7Name: &6" + craftIngredient.getName() + " "),
                            ColorUtil.translate(" &7Amount: &6" + craftIngredient.getAmount() + " "),
                            "",
                            ColorUtil.translate("&eLeft-Click » &aRemove ingredient "),
                            ColorUtil.translate("&eRight-Click » &aEdit ingredient "));
                } else {
                    lore = Arrays.asList("",
                            ColorUtil.translate(" &7Parent: &6" + craftIngredient.getParentType() + " "),
                            ColorUtil.translate(" &7Type: &6" + craftIngredient.getCraftType().toString() + " "),
                            ColorUtil.translate(" &7Amount: &6" + craftIngredient.getAmount() + " "),
                            "",
                            ColorUtil.translate("&eLeft-Click » &aRemove ingredient "),
                            ColorUtil.translate("&eRight-Click » &aEdit ingredient "));
                }


                ItemStack ingredient = new ItemBuilder(getIconByIngredient(craftIngredient))
                        .setName(ColorUtil.translate("&a" + craftIngredient.getName() + " x" + craftIngredient.getAmount()))
                        .setLore(lore)
                        .toItemStack();
                ingredient = NBTEditor.set(ingredient, craftIngredient.getUuid().toString(), "ingredientUUID");
                gui.addItem(ingredient);
            }
        } else {
            ItemStack noCreated = new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cAny ingredients created")).setLore(
                    "",
                    ColorUtil.translate(" &7You dont have any "),
                    ColorUtil.translate(" &7ingredients created. "),
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

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new InventorySession(craftParent, page)), 1L);

    }

    public void open(Player p, CraftParent craftParent) {
        p.updateInventory();
        openPage(p, craftParent, 0);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new InventorySession(craftParent, 0)), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            CraftParent craftParent = opened.get(p.getUniqueId()).getCraftParent();

            if (slot == 27 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, craftParent, opened.get(p.getUniqueId()).getPage() - 1);
            } else if (slot == 35 && e.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                openPage(p, craftParent, opened.get(p.getUniqueId()).getPage() + 1);
            } else if (slot == 30) {
                p.closeInventory();
                new CraftParentMenu(main).getConversation(p).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 100, 3);
            } else if (slot == 32) {
                main.getEditCraftingCraftsGUI().open(p);
            } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25)) {
                if (e.getCurrentItem().getType() == Material.AIR) return;

                if (main.getCubeletCraftingHandler().getCrafts().size() == 0) return;

                String ingredientUUID = NBTEditor.getString(e.getCurrentItem(), "ingredientUUID");
                Optional<CraftIngredient> craftIngredient = craftParent.getIngrediens().stream().filter(ing -> ing.getUuid().toString().equalsIgnoreCase(ingredientUUID)).findFirst();

                if (craftIngredient.isPresent()) {
                    if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {

                        craftParent.getIngrediens().remove(craftIngredient.get());
                        main.getCubeletCraftingHandler().saveCrafting();
                        reloadGUI(craftParent);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);

                    } else if (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {

                        p.closeInventory();
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);

                    }
                }

            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private ItemStack getIconByIngredient(CraftIngredient craftIngredient) {
        if(craftIngredient.getCraftType() == CraftType.CUBELET)
            return main.getCubeletTypesHandler().getTypeBydId(craftIngredient.getName()).getIcon();
        else if(craftIngredient.getCraftType() == CraftType.POINTS)
            return XMaterial.POPPY.parseItem();
        else if(craftIngredient.getCraftType() == CraftType.MONEY)
            return XMaterial.GOLD_INGOT.parseItem();
        else
            return XMaterial.BARRIER.parseItem();
    }

}