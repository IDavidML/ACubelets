package me.davidml16.acubelets.menus.admin.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.rewards.*;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EditRewardItemsMenu extends Menu {

    public EditRewardItemsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);

        List<ItemObject> itemObjects = reward.getItems();

        if(page > 0 && itemObjects.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(45, "%reward% | Items".replaceAll("%reward%", reward.getId()));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        fillBorders(edge);

        for (int i = 10; i <= 16; i++)
            gui.setItem(i, null);
        for (int i = 19; i <= 25; i++)
            gui.setItem(i, null);
        for (int i = 28; i <= 34; i++)
            gui.setItem(i, null);

        if (page > 0) {
            gui.setItem(18, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aPrevious page")).toItemStack());
        } else {
            gui.setItem(18, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (itemObjects.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        gui.setItem(40, back);

        if (itemObjects.size() > 21) itemObjects = itemObjects.subList(page * 21, ((page * 21) + 21) > itemObjects.size() ? itemObjects.size() : (page * 21) + 21);

        if(itemObjects.size() > 0) {

            for (ItemObject itemObject : itemObjects) {

                gui.addItem(new ItemBuilder(itemObject.getItemStack().clone())
                        .setName(Utils.translate("&a" + itemObject.getId()))
                        .setLore(
                                "",
                                Utils.translate("&eLeft-Click » &aRemove item ")
                        ).toItemStack());

            }

        } else {

            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny items added")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7items added. "),
                    "",
                    Utils.translate(" &7Click items of your. "),
                    Utils.translate(" &7inventory to add it. "),
                    ""
            ).toItemStack());

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        int slot = event.getRawSlot();

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);
        CubeletType cubeletType = reward.getParentCubelet();

        Player player = getOwner();

        if (slot == 18 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

        } else if (slot == 26 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

        } else if (slot == 40) {

            RewardsMenu rewardsMenu = new RewardsMenu(getMain(), player);
            rewardsMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
            rewardsMenu.open();

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (reward.getItems().size() == 0) return;

            String itemID = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            ItemObject itemObject = reward.getItem(itemID);

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {

                reward.getItems().remove(itemObject);
                reward.recreateItems();

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aYou removed item &e" + itemObject.getId() + " &afrom items of reward &e" + reward.getId()));

                reloadMenu();

            }

        } else if (slot >= 45 && slot <= 80) {

            ItemStack itemInHand = event.getCurrentItem();

            ItemObject newItemObject = new ItemObject("item-" + reward.getItems().size(), itemInHand);
            reward.getItems().add(newItemObject);

            player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                    + " &aYou added item &e" + newItemObject.getId() + " &ato items of reward &e" + reward.getId()));

            reloadMenu();

        }

    }

    @Override
    public void OnMenuClosed() {

        Reward reward = (Reward) getAttribute(AttrType.REWARD_ATTR);

        reward.getParentCubelet().saveType();

    }

}
