package me.davidml16.acubelets.menus.player.gifts;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.GiftGuiSession;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiftCubeletMenu extends Menu {

    public GiftCubeletMenu(Main main, Player player) {
        super(main, player);
        setSize(6);
    }

    @Override
    public void OnPageOpened(int page) {

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("giftamount");

        Player player = getOwner();
        GiftGuiSession session = (GiftGuiSession) getAttribute(AttrType.GIFT_GUISESSION_ATTR);

        if(session.getAvailable() == 0) {

            GiftMenu giftMenu = new GiftMenu(getMain(), player);
            giftMenu.setAttribute(AttrType.GIFT_GUISESSION_ATTR, session);
            giftMenu.open();

            return;

        }

        if(session.getCubeletAmount() > session.getAvailable()) {

            session.setCubeletAmount(1);

            reloadMyMenu();

            return;

        }

        Inventory gui = createInventory(getSize(), guiLayout.getMessage("Title")
                        .replaceAll("%cubelet_type%", Utils.removeColors(session.getCubeletType().getName()))
                        .replaceAll("%amount%", String.valueOf(session.getCubeletAmount())));

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        back = NBTEditor.set(back, "back", "action");
        gui.setItem((getSize() - 10) + guiLayout.getSlot("Back"), back);

        List<String> cubeletLore = new ArrayList<>();

        if(Bukkit.getPlayer(session.getTargetName()) != null) {

            for (String line : guiLayout.getMessageList("Items.Cubelet.Lore")) {

                cubeletLore.add(Utils.translate(line
                        .replaceAll("%gift_amount%", String.valueOf(session.getCubeletAmount())))
                        .replaceAll("%receiver%", session.getTargetName())
                );

            }

        } else {

            for (String line : guiLayout.getMessageList("Items.Cubelet.Lore")) {

                cubeletLore.add(Utils.translate(line
                        .replaceAll("%gift_amount%", String.valueOf(session.getCubeletAmount())))
                        .replaceAll("%receiver%", session.getTargetName())
                );

            }

        }

        ItemStack cubelet = new ItemBuilder(session.getCubeletType().getIcon().clone())
                .setName(Utils.translate(guiLayout.getMessage("Items.Cubelet.Name").replace("%cubelet_name%", session.getCubeletType().getName())))
                .setLore(cubeletLore)
                .toItemStack();
        cubelet = NBTEditor.set(cubelet, "send", "action");
        gui.setItem(getCenterSlot(), cubelet);

        ItemStack add = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFkMGY4MmEyYTRjZGQ4NWY3OWY0ZDlkOTc5OGY5YzNhNWJjY2JlOWM3ZjJlMjdjNWZjODM2NjUxYThmM2Y0NSJ9fX0="))
                .setName(guiLayout.getMessage("Items.Add.Name"))
                .toItemStack();
        add = NBTEditor.set(add, "add", "action");
        gui.setItem(getCenterSlot() + 2, add);

        ItemStack substract = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRmNWMyZjg5M2JkM2Y4OWNhNDA3MDNkZWQzZTQyZGQwZmJkYmE2ZjY3NjhjODc4OWFmZGZmMWZhNzhiZjYifX19"))
                .setName(guiLayout.getMessage("Items.Substract.Name"))
                .toItemStack();
        substract = NBTEditor.set(substract, "substract", "action");
        gui.setItem(getCenterSlot() - 2, substract);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = getOwner();
        String action = NBTEditor.getString(event.getCurrentItem(), "action");

        if(event.getClick() == ClickType.DOUBLE_CLICK) return;

        if(action == null) return;

        GiftGuiSession giftGuiSession = (GiftGuiSession) getAttribute(AttrType.GIFT_GUISESSION_ATTR);

        switch (action) {

            case "send":

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getMessage("Commands.Cubelets.Gift.Gifted")
                        .replaceAll("%amount%", String.valueOf(giftGuiSession.getCubeletAmount()))
                        .replaceAll("%cubelet%", Utils.removeColors(giftGuiSession.getCubeletType().getName()))
                        .replaceAll("%player%", giftGuiSession.getTargetName())
                ));

                Player target = Bukkit.getPlayer(giftGuiSession.getTarget());

                if(target != null) {

                    target.sendMessage(Utils.translate(getMain().getLanguageHandler().getMessage("Commands.Cubelets.Gift.Received")
                            .replaceAll("%amount%", String.valueOf(giftGuiSession.getCubeletAmount()))
                            .replaceAll("%cubelet%", Utils.removeColors(giftGuiSession.getCubeletType().getName()))
                            .replaceAll("%player%", player.getName())
                    ));

                }

                playSound(SoundType.NOTE_PLING);

                getMain().getTransactionHandler().transferCubelets(player.getUniqueId(),
                        giftGuiSession.getTarget(),
                        giftGuiSession.getCubeletType(),
                        giftGuiSession.getCubeletAmount());

                player.closeInventory();

                break;

            case "add":

                if(giftGuiSession.getCubeletAmount() < giftGuiSession.getAvailable()) {

                    giftGuiSession.setCubeletAmount(giftGuiSession.getCubeletAmount() + 1);

                    reloadMyMenu();

                    playSound(SoundType.CLICK);

                }

                break;

            case "substract":

                if(giftGuiSession.getCubeletAmount() > 1) {

                    giftGuiSession.setCubeletAmount(giftGuiSession.getCubeletAmount() - 1);

                    reloadMyMenu();

                    playSound(SoundType.CLICK);

                }

                break;

            case "back":

                GiftMenu giftMenu = new GiftMenu(getMain(), player);
                giftMenu.setAttribute(AttrType.GIFT_GUISESSION_ATTR, giftGuiSession);

                break;

        }

    }

    @Override
    public void OnMenuClosed() { }

}
