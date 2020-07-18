package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EditCrafting_GUI implements Listener {

    private Set<UUID> opened;

    private Main main;

    public EditCrafting_GUI(Main main) {
        this.main = main;
        this.opened = new HashSet<UUID>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public Set<UUID> getOpened() {
        return opened;
    }

    public void reloadGUI() {
        Inventory gui = Bukkit.createInventory(null, 36, "Crafting editor » Main");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        if(main.getCubeletCraftingHandler().getInventoryRows() < 6) {
            gui.setItem(11, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aAdd one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    ColorUtil.translate(" &7New rows: &6" + (main.getCubeletCraftingHandler().getInventoryRows() + 1)),
                    "",
                    ColorUtil.translate("&eClick to add one row")
            ).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aAdd one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    "",
                    ColorUtil.translate("&cReached maximum number of rows")
            ).toItemStack());
        }

        gui.setItem(12, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(ColorUtil.translate("&aReset rows to default")).setLore(
                "",
                ColorUtil.translate(" &7New rows: &64"),
                "",
                ColorUtil.translate("&eClick to reset rows to default")
        ).toItemStack());

        if(main.getCubeletCraftingHandler().getInventoryRows() > 2) {
            gui.setItem(13, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aRemove one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    ColorUtil.translate(" &7New rows: &6" + (main.getCubeletCraftingHandler().getInventoryRows() - 1)),
                    "",
                    ColorUtil.translate("&eClick to remove one row")
            ).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aRemove one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    "",
                    ColorUtil.translate("&cReached minimum number of rows")
            ).toItemStack());
        }

        int available = main.getCubeletCraftingHandler().getCrafts().size();
        gui.setItem(15, new ItemBuilder(XMaterial.CRAFTING_TABLE.parseItem()).setName(ColorUtil.translate("&aConfigure crafts")).setLore(
                "",
                ColorUtil.translate(" &7Crafts created: " + (available > 0 ? "&6" : "&c") + available),
                "",
                ColorUtil.translate("&eClick to setup crafts")
        ).toItemStack());

        for (int i = 0; i < 36; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("opencubelet");
        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Close.Name"))
                .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                .toItemStack();
        gui.setItem(31, back);

        for(UUID uuid : opened) {
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).getOpenInventory().getTopInventory().setContents(gui.getContents());
        }
    }

    public void open(Player p) {
        p.updateInventory();

        Inventory gui = Bukkit.createInventory(null, 36, "Crafting editor » Main");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        if(main.getCubeletCraftingHandler().getInventoryRows() < 6) {
            gui.setItem(11, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aAdd one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    ColorUtil.translate(" &7New rows: &6" + (main.getCubeletCraftingHandler().getInventoryRows() + 1)),
                    "",
                    ColorUtil.translate("&eClick to add one row")
            ).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aAdd one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    "",
                    ColorUtil.translate("&cReached maximum number of rows")
            ).toItemStack());
        }

        gui.setItem(12, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(ColorUtil.translate("&aReset rows to default")).setLore(
                "",
                ColorUtil.translate(" &7New rows: &64"),
                "",
                ColorUtil.translate("&eClick to reset rows to default")
        ).toItemStack());

        if(main.getCubeletCraftingHandler().getInventoryRows() > 2) {
            gui.setItem(13, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aRemove one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    ColorUtil.translate(" &7New rows: &6" + (main.getCubeletCraftingHandler().getInventoryRows() - 1)),
                    "",
                    ColorUtil.translate("&eClick to remove one row")
            ).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aRemove one row")).setLore(
                    "",
                    ColorUtil.translate(" &7Actual rows: &6" + main.getCubeletCraftingHandler().getInventoryRows()),
                    "",
                    ColorUtil.translate("&cReached minimum number of rows")
            ).toItemStack());
        }

        int available = main.getCubeletCraftingHandler().getCrafts().size();
        gui.setItem(15, new ItemBuilder(XMaterial.CRAFTING_TABLE.parseItem()).setName(ColorUtil.translate("&aConfigure crafts")).setLore(
                "",
                ColorUtil.translate(" &7Crafts created: " + (available > 0 ? "&6" : "&c") + available),
                "",
                ColorUtil.translate("&eClick to setup crafts")
        ).toItemStack());

        for (int i = 0; i < 36; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("opencubelet");
        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Close.Name"))
                .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                .toItemStack();
        gui.setItem(31, back);

        p.openInventory(gui);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(p.getUniqueId()), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.contains(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if(e.getClick() != ClickType.DOUBLE_CLICK) {
                if (slot == 11) {
                    if (main.getCubeletCraftingHandler().getInventoryRows() < 6) {
                        main.getCubeletCraftingHandler().setInventorySize(main.getCubeletCraftingHandler().getInventoryRows() + 1);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                        reloadGUI();
                    }
                } else if (slot == 12) {
                    main.getCubeletCraftingHandler().setInventorySize(4);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 100, 3);
                    reloadGUI();
                } else if (slot == 13) {
                    if (main.getCubeletCraftingHandler().getInventoryRows() > 2) {
                        main.getCubeletCraftingHandler().setInventorySize(main.getCubeletCraftingHandler().getInventoryRows() - 1);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                        reloadGUI();
                    }
                } else if (slot == 15) {
                    main.getEditCraftingCraftsGUI().open(p);
                } else if (slot == 31) {
                    p.closeInventory();
                }
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(opened.contains(p.getUniqueId())) {
            opened.remove(p.getUniqueId());
            main.getCubeletCraftingHandler().saveCrafting();
        }
    }

}