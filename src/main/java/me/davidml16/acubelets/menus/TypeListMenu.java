package me.davidml16.acubelets.menus;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TypeListMenu extends Menu {

    public TypeListMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        List<CubeletType> types = new ArrayList<>(getMain().getCubeletTypesHandler().getTypes().values());

        if(page > 0 && types.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(45, "Cubelet Types");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack close = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aClose")).toItemStack();

        gui.setItem(40, close);

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

        if (types.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (types.size() > 21) types = types.subList(page * 21, ((page * 21) + 21) > types.size() ? types.size() : (page * 21) + 21);

        if(types.size() > 0) {
            for (CubeletType cubeletType : types) {
                gui.addItem(new ItemBuilder(cubeletType.getIcon())
                        .setName(Utils.translate("&a" + cubeletType.getId()))
                        .setLore(
                                "",
                                Utils.translate(" &7Name: &6" + cubeletType.getName() + " "),
                                Utils.translate(" &7Rarities: &6" + cubeletType.getRarities().size() + " "),
                                Utils.translate(" &7Rewards: &6" + cubeletType.getAllRewards().size() + " "),
                                "",
                                Utils.translate(" &7Animation: &6" + cubeletType.getAnimation() + " "),
                                Utils.translate(" &7Expiration: &6" + TimeUtils.millisToLongDHMS(cubeletType.getExpireTime()) + " "),
                                "",
                                Utils.translate("&eClick to setup " + cubeletType.getId() + " type ")
                        ).toItemStack());
            }
        } else {
            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny type created")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7type created. "),
                    ""
            ).toItemStack());
        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        int slot = event.getRawSlot();

        if (slot == 18 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

            playSound(SoundType.CLICK);

        } else if (slot == 26 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

            playSound(SoundType.CLICK);

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            String id = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());

            if (getMain().getCubeletTypesHandler().getTypes().size() == 0) return;

            if(getMain().getCubeletTypesHandler().getTypes().keySet().contains(id)) {

                TypeConfigMenu typeConfigMenu = new TypeConfigMenu(getMain(), player);
                typeConfigMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, id);
                typeConfigMenu.open();

            } else {

                player.closeInventory();

            }

        } else if(slot == 40) {

            player.closeInventory();

        }

    }

    @Override
    public void OnMenuClosed() { }

}
