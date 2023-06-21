package me.davidml16.acubelets.menus.admin.crafting;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.crafting.CraftParentConversation;
import me.davidml16.acubelets.conversation.crafting.EditCraftParentConversation;
import me.davidml16.acubelets.menus.admin.options.OptionsMainMenu;
import me.davidml16.acubelets.objects.CraftParent;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EditCraftingCraftsMenu extends Menu {

    public EditCraftingCraftsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        List<CraftParent> craftParents = getMain().getCubeletCraftingHandler().getCrafts();

        if(page > 0 && craftParents.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(45, "Crafting editor | Crafts");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newReward = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new craft")).toItemStack();

        int rows = getMain().getCubeletCraftingHandler().getInventoryRows();
        ItemStack editRows = new ItemBuilder(XMaterial.CHEST.parseItem()).setName(Utils.translate("&aModify gui rows"))
                .setLore(
                        "",
                        Utils.translate(" &7Actual rows: &6" + rows + " " + (rows == 6 ? "&c(Max reached)" : rows == 2 ? "&c(Min reached)" : "")),
                        "",
                        Utils.translate("&eLeft-Click » &aAdd one row "),
                        Utils.translate("&eMiddle-Click » &aReset to 4 rows "),
                        Utils.translate("&eRight-Click » &aRemove one row"))
                .toItemStack();

        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack")).toItemStack();

        fillBorders(edge);

        gui.setItem(38, newReward);
        gui.setItem(40, back);
        gui.setItem(42, editRows);

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);
        for (int i = 28; i <= 34; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(36, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(36, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (craftParents.size() > (page + 1) * 14) {
            gui.setItem(44, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(44, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (craftParents.size() > 21) craftParents = craftParents.subList(page * 21, Math.min(((page * 21) + 21), craftParents.size()));

        if(craftParents.size() > 0) {

            for (CraftParent craftParent : craftParents) {

                gui.addItem(new ItemBuilder(getMain().getCubeletTypesHandler().getTypeBydId(craftParent.getCubeletType()).getIcon())
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
            for (int i = 28; i <= 34; i++)
                gui.setItem(i, noCreated);

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        if (slot == 36 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

            playSound(SoundType.CLICK);

        } else if (slot == 44 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

            playSound(SoundType.CLICK);

        } else if (slot == 38) {

            player.closeInventory();

            new CraftParentConversation(getMain()).getConversation(player).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 40) {

            new OptionsMainMenu(getMain(), player).open();

        } else if (slot == 42) {

            if (event.getClick() == ClickType.LEFT) {

                if (getMain().getCubeletCraftingHandler().getInventoryRows() < 6) {

                    getMain().getCubeletCraftingHandler().setInventorySize(getMain().getCubeletCraftingHandler().getInventoryRows() + 1);

                    playSound(SoundType.CLICK);

                    reloadMenu();

                }

            } else if (event.getClick() == ClickType.MIDDLE) {

                getMain().getCubeletCraftingHandler().setInventorySize(4);

                playSound(SoundType.CHICKEN_EGG_POP);

                reloadMenu();

            } else if (event.getClick() == ClickType.RIGHT) {

                if (getMain().getCubeletCraftingHandler().getInventoryRows() > 2) {

                    getMain().getCubeletCraftingHandler().setInventorySize(getMain().getCubeletCraftingHandler().getInventoryRows() - 1);

                    playSound(SoundType.CLICK);

                    reloadMenu();

                }

            }

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            if (getMain().getCubeletCraftingHandler().getCrafts().size() == 0) return;

            String craftID = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            CraftParent craftParent = getMain().getCubeletCraftingHandler().getCraftById(craftID);

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {

                EditCraftingIngredientsMenu editCraftingIngredientsMenu = new EditCraftingIngredientsMenu(getMain(), player);
                editCraftingIngredientsMenu.setAttribute(AttrType.CRAFT_PARENT_ATTR, craftParent);
                editCraftingIngredientsMenu.open();

            } else if(event.getClick() == ClickType.MIDDLE) {

                getMain().getCubeletCraftingHandler().getCrafts().remove(craftParent);
                getMain().getCubeletCraftingHandler().saveCrafting();

                playSound(SoundType.CLICK);

                reloadMenu();

            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {

                player.closeInventory();

                new EditCraftParentConversation(getMain()).getConversation(player, craftParent).begin();

                playSound(SoundType.ANVIL_USE);

            }

        }

    }

    @Override
    public void OnMenuClosed() { }

}
