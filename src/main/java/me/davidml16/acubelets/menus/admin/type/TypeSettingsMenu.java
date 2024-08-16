package me.davidml16.acubelets.menus.admin.type;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RenameConversation;
import me.davidml16.acubelets.conversation.TypeIconConversation;
import me.davidml16.acubelets.conversation.TypeKeyConversation;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TypeSettingsMenu extends Menu {

    public TypeSettingsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        Inventory gui = createInventory(45, "%cubelet_type% | Settings".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(id);
        List<String> lore = new ArrayList<>();
        for(String line : type.getLoreAvailable()) {
            lore.add(Utils.translate(line));
        }
        gui.setItem(13, new ItemBuilder(XMaterial.NAME_TAG.parseItem()).setName(Utils.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(22, new ItemBuilder(type.getIcon()).setName(Utils.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        Utils.translate(" &7You can change the "),
                        Utils.translate(" &7icon clicking this item "),
                        Utils.translate(" &7and opening icon setup "),
                        "",
                        Utils.translate("&eClick change skull texture! ")
                )
                .toItemStack());

        gui.setItem(20, new ItemBuilder(XMaterial.ANVIL.parseItem()).setName(Utils.translate("&aCubelet type name"))
                .setLore(
                        "",
                        Utils.translate(" &7Click on the anvil "),
                        Utils.translate(" &7to start rename menu "),
                        "",
                        Utils.translate(" &7Choose 1 to rename cubelet type "),
                        Utils.translate(" &7Choose 2 to save and exit menu. "),
                        "",
                        Utils.translate("&eClick to rename cubelet! ")
                )
                .toItemStack());

        gui.setItem(24, new ItemBuilder(XMaterial.TRIPWIRE_HOOK.parseItem())
                .addGlow()
                .setName(Utils.translate("&aCubelet key"))
                .setLore(
                        "",
                        Utils.translate(" &7Open cubelets by "),
                        Utils.translate(" &7holding a key in hand, "),
                        Utils.translate(" &7a clicking a machine. "),
                        "",
                        Utils.translate("&eClick to change key item! ")
                ).toItemStack());

        gui.setItem(40, back);

        fillPage(edge);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();

        int slot = event.getRawSlot();
        String id = (String) getAttribute(AttrType.CUSTOM_ID_ATTR);

        CubeletType type = getMain().getCubeletTypesHandler().getTypeBydId(id);

        if (slot == 20) {

            player.closeInventory();

            new RenameConversation(getMain()).getConversation(player, type).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 22) {

            player.closeInventory();

            new TypeIconConversation(getMain()).getConversation(player, type).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 24) {

            player.closeInventory();

            new TypeKeyConversation(getMain()).getConversation(player, type).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 40) {

            TypeConfigMenu typeConfigMenu = new TypeConfigMenu(getMain(), player);
            typeConfigMenu.setAttribute(AttrType.CUSTOM_ID_ATTR, id);
            typeConfigMenu.open();

        }

    }

    @Override
    public void OnMenuClosed() { }

}