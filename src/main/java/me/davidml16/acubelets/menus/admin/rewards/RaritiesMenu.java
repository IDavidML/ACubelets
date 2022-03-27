package me.davidml16.acubelets.menus.admin.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.rarities.EditRarityConversation;
import me.davidml16.acubelets.conversation.rarities.RarityConversation;
import me.davidml16.acubelets.interfaces.RarityComparator;
import me.davidml16.acubelets.menus.admin.type.TypeConfigMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.Rarity;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RaritiesMenu extends Menu {

    public RaritiesMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        List<Rarity> rarities = new ArrayList<>(getMain().getCubeletTypesHandler().getTypeBydId(id).getRarities().values());
        rarities.sort(new RarityComparator());

        if(page > 0 && rarities.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        Inventory gui = createInventory(45, "%cubelet_type% | Rarities".replaceAll("%cubelet_type%", id));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack newRarity = new ItemBuilder(XMaterial.SUNFLOWER.parseItem()).setName(Utils.translate("&aCreate new rarity")).toItemStack();
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

        if (rarities.size() > (page + 1) * 21) {
            gui.setItem(26, new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).setName(Utils.translate("&aNext page")).toItemStack());
        } else {
            gui.setItem(26, new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack());
        }

        if (rarities.size() > 21) rarities = rarities.subList(page * 21, ((page * 21) + 21) > rarities.size() ? rarities.size() : (page * 21) + 21);

        if(rarities.size() > 0) {
            for (Rarity rarity : rarities) {
                gui.addItem(new ItemBuilder(XMaterial.ITEM_FRAME.parseItem())
                        .setName(Utils.translate("&a" + rarity.getId()))
                        .setLore(
                                "",
                                Utils.translate(" &7Name: &6" + rarity.getName() + " "),
                                Utils.translate(" &7Chance: &6" + rarity.getChance() + "% "),
                                Utils.translate(" &7Duplicate Points: &6" + rarity.getDuplicatePointsRange() + " "),
                                "",
                                Utils.translate("&eLeft-Click » &aRemove rarity "),
                                Utils.translate("&eRight-Click » &aEdit rarity ")).toItemStack());
            }
        } else {
            gui.setItem(22, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cAny rarity created")).setLore(
                    "",
                    Utils.translate(" &7You dont have any "),
                    Utils.translate(" &7rarity created. "),
                    ""
            ).toItemStack());
        }

        gui.setItem(39, newRarity);
        gui.setItem(41, back);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        int slot = event.getRawSlot();
        CubeletType cubeletType = getMain().getCubeletTypesHandler().getTypeBydId(id);

        if (slot == 18 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            previousPage();

            playSound(SoundType.CLICK);

        } else if (slot == 26 && event.getCurrentItem().getType() == XMaterial.ENDER_PEARL.parseMaterial()) {

            nextPage();

            playSound(SoundType.CLICK);

        } else if (slot == 39) {

            player.closeInventory();

            new RarityConversation(getMain()).getConversation(player, cubeletType).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 41) {

            TypeConfigMenu typeConfigMenu = new TypeConfigMenu(getMain(), player);
            typeConfigMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, cubeletType.getId());
            typeConfigMenu.open();

        } else if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            if (cubeletType.getAllRewards().size() == 0) return;

            String rarityID = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            Rarity rarity = cubeletType.getRarities().get(rarityID);

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {

                cubeletType.getRarities().remove(rarityID);

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aYou removed rarity &e" + rarityID + " &afrom rarities of cubelet type &e" + cubeletType.getId()));

                playSound(SoundType.CLICK);

                reloadMenu();

            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {

                player.closeInventory();

                new EditRarityConversation(getMain()).getConversation(player, cubeletType, rarity).begin();

                playSound(SoundType.ANVIL_USE);

            }

        }

    }

    @Override
    public void OnMenuClosed() {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        getMain().getCubeletTypesHandler().getTypeBydId(id).saveType();

    }
}
