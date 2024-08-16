package me.davidml16.acubelets.menus.admin.type;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.admin.AnimationsMenu;
import me.davidml16.acubelets.menus.admin.rewards.RaritiesMenu;
import me.davidml16.acubelets.menus.admin.rewards.RewardsMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TypeConfigMenu extends Menu {

    public TypeConfigMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        Inventory gui = createInventory(45, "%cubelet_type% | Configuration".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(id);
        gui.setItem(4, new ItemBuilder(type.getIcon().clone()).setName(Utils.translate(type.getName())).toItemStack());

        gui.setItem(19, new ItemBuilder(XMaterial.ARMOR_STAND.parseItem())
                .setName(Utils.translate("&aAnimations"))
                .setLore(
                        "",
                        Utils.translate(" &7Open animations gui to "),
                        Utils.translate(" &7chance cubelet animation. "),
                        Utils.translate(" &7to begin rarity setup. "),
                        "",
                        Utils.translate("&eClick to config animations! ")
                ).toItemStack());

        gui.setItem(21, new ItemBuilder(XMaterial.ITEM_FRAME.parseItem())
                .setName(Utils.translate("&aRarities"))
                .setLore(
                        "",
                        Utils.translate(" &7Open rarities gui and "),
                        Utils.translate(" &7click on new rarity "),
                        Utils.translate(" &7to begin rarity setup. "),
                        "",
                        Utils.translate(" &7Click the rarity item "),
                        Utils.translate(" &7in the GUI to remove it. "),
                        "",
                        Utils.translate("&eClick to config rarities! ")
                ).toItemStack());

        gui.setItem(23, new ItemBuilder(XMaterial.GOLD_NUGGET.parseItem())
                .setName(Utils.translate("&aRewards"))
                .setLore(
                        "",
                        Utils.translate(" &7Open rewards gui and "),
                        Utils.translate(" &7click on new reward "),
                        Utils.translate(" &7to begin reward setup. "),
                        "",
                        Utils.translate(" &7Click the rewards item "),
                        Utils.translate(" &7in the GUI to remove it. "),
                        "",
                        Utils.translate("&eClick to config rewards! ")
                ).toItemStack());

        gui.setItem(25, new ItemBuilder(XMaterial.CHEST.parseItem()).setName(Utils.translate("&aSettings"))
                .setLore(
                        "",
                        Utils.translate(" &7Click on the chest "),
                        Utils.translate(" &7to change some settings "),
                        "",
                        Utils.translate(" &7- Cubelet type name "),
                        Utils.translate(" &7- Cubelet type icon "),
                        Utils.translate(" &7- Cubelet type key "),
                        "",
                        Utils.translate("&eClick to open settings menu! ")
                )
                .toItemStack());

        gui.setItem(40, new ItemBuilder(XMaterial.BARRIER.parseItem())
                .setName(Utils.translate("&cReload configuration "))
                .setLore(
                        "",
                        Utils.translate(" &7Reload configuration to "),
                        Utils.translate(" &7update last changes made. "),
                        "",
                        Utils.translate("&eClick reload cubelets! ")
                )
                .toItemStack());

        fillPage(edge);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;

        int slot = event.getRawSlot();

        Player player = getOwner();
        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(id);

        if (slot == 19) {

            AnimationsMenu animationsMenu = new AnimationsMenu(getMain(), player);
            animationsMenu.setAttribute(AttrType.CUBELET_TYPE_ATTR, type);
            animationsMenu.open();

        } else if (slot == 21) {

            RaritiesMenu raritiesMenu = new RaritiesMenu(getMain(), player);
            raritiesMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, id);
            raritiesMenu.open();

        } else if (slot == 23) {

            RewardsMenu rewardsMenu = new RewardsMenu(getMain(), player);
            rewardsMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, id);
            rewardsMenu.open();

        } else if (slot == 25) {

            TypeSettingsMenu typeSettingsMenu = new TypeSettingsMenu(getMain(), player);
            typeSettingsMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, id);
            typeSettingsMenu.open();

        } else if (slot == 40) {

            getMain().getPluginHandler().reloadAll();

            if(!getMain().getLanguageHandler().isEmptyMessage("Commands.Reload"))
                player.sendMessage(getMain().getLanguageHandler().getMessage("Commands.Reload"));

            playSound(SoundType.ANVIL_USE);

        }

    }

    @Override
    public void OnMenuClosed() { }

}