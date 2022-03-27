package me.davidml16.acubelets.menus.admin.options;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.admin.crafting.EditCraftingCraftsMenu;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OptionsMainMenu extends Menu {

    public OptionsMainMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Inventory gui = createInventory(45, "Options | Main");
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

        int available = getMain().getCubeletCraftingHandler().getCrafts().size();
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

        fillPage(edge);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        if (slot == 19) {

            new OptionsAnimationsMenu(getMain(), player).open();

        } else if (slot == 21) {

            new EditCraftingCraftsMenu(getMain(), player).open();

        } else if (slot == 40) {

            player.closeInventory();

        }

    }

    @Override
    public void OnMenuClosed() { }

}