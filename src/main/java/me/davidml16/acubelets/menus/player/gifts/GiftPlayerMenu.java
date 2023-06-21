package me.davidml16.acubelets.menus.player.gifts;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.GiftGuiSession;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GiftPlayerMenu extends Menu {

    public GiftPlayerMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        List<Player> players = new ArrayList<>(getMain().getServer().getOnlinePlayers());

        if(page > 0 && players.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("giftplayer");

        Inventory gui = createInventory(45, guiLayout.getMessage("Title"));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        fillTopSide(edge, 4);

        if (page > 0) {

            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");

            gui.setItem((45 - 10) + guiLayout.getSlot("PreviousPage"), item);

        }

        if (players.size() > (page + 1) * 14) {

            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");

            gui.setItem((45 - 10) + guiLayout.getSlot("NextPage"), item);

        }

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        back = NBTEditor.set(back, "back", "action");
        gui.setItem((45 - 10) + guiLayout.getSlot("Back"), back);

        if (players.size() > 14) players = players.subList(page * 14, Math.min(((page * 14) + 14), players.size()));

        if(players.size() > 0) {

            for (Player loop : players) {

                if(!loop.getUniqueId().equals(player.getUniqueId())) {

                    ItemStack item = new ItemBuilder(SkullCreator.itemFromUuid(loop.getUniqueId()))
                            .setName(Utils.translate(guiLayout.getMessage("Items.Player.Name").replace("%player%",loop.getName())))
                            .setLore(guiLayout.getMessageList("Items.Player.Lore"))
                            .toItemStack();
                    item = NBTEditor.set(item, "player", "action");
                    item = NBTEditor.set(item, loop.getName(), "name");
                    item = NBTEditor.set(item, loop.getUniqueId().toString(), "uuid");

                    gui.addItem(item);

                }

            }

        }

        fillTopSide(null, 4);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        String action = NBTEditor.getString(event.getCurrentItem(), "action");

        if(event.getClick() == ClickType.DOUBLE_CLICK) return;

        if(action == null) return;

        Player player = getOwner();

        switch (action) {

            case "player":

                String name = NBTEditor.getString(event.getCurrentItem(), "name");
                UUID uuid = UUID.fromString(NBTEditor.getString(event.getCurrentItem(), "uuid"));

                GiftGuiSession giftGuiSession = new GiftGuiSession(player.getUniqueId(), uuid, name, false);

                GiftMenu giftMenu = new GiftMenu(getMain(), player);
                giftMenu.setAttribute(AttrType.GIFT_GUISESSION_ATTR, giftGuiSession);
                giftMenu.open();

                break;

            case "previous":
                previousPage();
                break;

            case "next":
                nextPage();
                break;

            case "back":
                new CubeletsMenu(getMain(), player).open();
                break;

        }

    }

    @Override
    public void OnMenuClosed() { }

}