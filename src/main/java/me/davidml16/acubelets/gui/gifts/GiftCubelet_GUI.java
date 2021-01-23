package me.davidml16.acubelets.gui.gifts;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.*;
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

public class GiftCubelet_GUI implements Listener {

    private HashMap<UUID, GiftAmountGuiSession> opened;

    private Main main;

    public GiftCubelet_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, GiftAmountGuiSession> getOpened() {
        return opened;
    }

    public void reloadGui(Player player) {
        if(opened.containsKey(player.getUniqueId())){
            GiftAmountGuiSession session = opened.get(player.getUniqueId());
            openPage(player, session.getTarget(), session.getTargetName(), session.getCubeletType(), session.getCubeletAmount(), session.isOpenedByCommand());
        }
    }

    private void openPage(Player player, UUID target, String targetName, CubeletType cubeletType, int amount, boolean openedByCommand) {
        GUILayout guiLayout = main.getLayoutHandler().getLayout("giftamount");

        Profile profile = main.getPlayerDataHandler().getData(player);

        GiftAmountGuiSession giftAmountGuiSession = new GiftAmountGuiSession(player.getUniqueId(), target, targetName, cubeletType, amount, openedByCommand);

        if(giftAmountGuiSession.getAvailable() == 0) {
            main.getGiftGUI().open(player, target, targetName, openedByCommand);
            return;
        }
        if(amount > giftAmountGuiSession.getAvailable()) openPage(player, target, targetName, cubeletType, 1, openedByCommand);

        Inventory gui = Bukkit.createInventory(null, 36, guiLayout.getMessage("Title")
                .replaceAll("%cubelet_type%", Utils.removeColors(cubeletType.getName()))
                .replaceAll("%amount%", String.valueOf(amount))
        );

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        back = NBTEditor.set(back, "back", "action");
        gui.setItem((36 - 10) + guiLayout.getSlot("Back"), back);

        List<String> cubeletLore = new ArrayList<>();
        if(Bukkit.getPlayer(target) != null) {
            for (String line : guiLayout.getMessageList("Items.Cubelet.Lore")) {
                cubeletLore.add(Utils.translate(line
                        .replaceAll("%gift_amount%", String.valueOf(amount)))
                        .replaceAll("%receiver%", Bukkit.getPlayer(target).getName())
                );
            }
        } else {
            for (String line : guiLayout.getMessageList("Items.Cubelet.Lore")) {
                cubeletLore.add(Utils.translate(line
                        .replaceAll("%gift_amount%", String.valueOf(amount)))
                        .replaceAll("%receiver%", Bukkit.getOfflinePlayer(target).getName())
                );
            }
        }

        ItemStack cubelet = new ItemBuilder(cubeletType.getIcon().clone())
                .setName(Utils.translate(guiLayout.getMessage("Items.Cubelet.Name").replace("%cubelet_name%", cubeletType.getName())))
                .setLore(cubeletLore)
                .toItemStack();
        cubelet = NBTEditor.set(cubelet, "send", "action");
        gui.setItem(13, cubelet);

        ItemStack add = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjFkMGY4MmEyYTRjZGQ4NWY3OWY0ZDlkOTc5OGY5YzNhNWJjY2JlOWM3ZjJlMjdjNWZjODM2NjUxYThmM2Y0NSJ9fX0="))
                .setName(guiLayout.getMessage("Items.Add.Name"))
                .toItemStack();
        add = NBTEditor.set(add, "add", "action");
        gui.setItem(15, add);

        ItemStack substract = new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRmNWMyZjg5M2JkM2Y4OWNhNDA3MDNkZWQzZTQyZGQwZmJkYmE2ZjY3NjhjODc4OWFmZGZmMWZhNzhiZjYifX19"))
                .setName(guiLayout.getMessage("Items.Substract.Name"))
                .toItemStack();
        substract = NBTEditor.set(substract, "substract", "action");
        gui.setItem(11, substract);

        player.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(player.getUniqueId(), giftAmountGuiSession), 1L);
    }

    public void open(Player p, UUID target, String targetName, CubeletType cubeletType, boolean openedByCommand) {
        p.updateInventory();
        openPage(p, target, targetName, cubeletType, 1, openedByCommand);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);

            String action = NBTEditor.getString(e.getCurrentItem(), "action");

            if(e.getClick() == ClickType.DOUBLE_CLICK) return;

            if(action == null) {
                e.setCancelled(true);
                return;
            }

            GiftAmountGuiSession giftAmountGuiSession = opened.get(p.getUniqueId());

            switch (action) {
                case "send":

                    p.sendMessage(Utils.translate(main.getLanguageHandler().getMessage("Commands.Cubelets.Gift.Gifted")
                            .replaceAll("%amount%", String.valueOf(giftAmountGuiSession.getCubeletAmount()))
                            .replaceAll("%cubelet%", Utils.removeColors(giftAmountGuiSession.getCubeletType().getName()))
                            .replaceAll("%player%", giftAmountGuiSession.getTargetName())
                    ));

                    Player target = Bukkit.getPlayer(opened.get(p.getUniqueId()).getTarget());
                    if(target != null) {
                        target.sendMessage(Utils.translate(main.getLanguageHandler().getMessage("Commands.Cubelets.Gift.Received")
                                .replaceAll("%amount%", String.valueOf(giftAmountGuiSession.getCubeletAmount()))
                                .replaceAll("%cubelet%", Utils.removeColors(giftAmountGuiSession.getCubeletType().getName()))
                                .replaceAll("%player%", giftAmountGuiSession.getTargetName())
                        ));
                    }

                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.NOTE_PLING, 10, 3);

                    main.getTransactionHandler().transferCubelets(p.getUniqueId(),
                            opened.get(p.getUniqueId()).getTarget(),
                            opened.get(p.getUniqueId()).getCubeletType(),
                            opened.get(p.getUniqueId()).getCubeletAmount());

                    p.closeInventory();

                    break;
                case "add":
                    if(giftAmountGuiSession.getCubeletAmount() < giftAmountGuiSession.getAvailable()) {
                        giftAmountGuiSession.setCubeletAmount(giftAmountGuiSession.getCubeletAmount() + 1);
                        openPage(p, giftAmountGuiSession.getTarget(), giftAmountGuiSession.getTargetName(), giftAmountGuiSession.getCubeletType(), giftAmountGuiSession.getCubeletAmount(), giftAmountGuiSession.isOpenedByCommand());
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    }
                    break;
                case "substract":
                    if(giftAmountGuiSession.getCubeletAmount() > 1) {
                        giftAmountGuiSession.setCubeletAmount(giftAmountGuiSession.getCubeletAmount() - 1);
                        openPage(p, giftAmountGuiSession.getTarget(), giftAmountGuiSession.getTargetName(), giftAmountGuiSession.getCubeletType(), giftAmountGuiSession.getCubeletAmount(), giftAmountGuiSession.isOpenedByCommand());
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    }
                    break;
                case "back":
                    main.getGiftGUI().open(p, giftAmountGuiSession.getTarget(), giftAmountGuiSession.getTargetName(), giftAmountGuiSession.isOpenedByCommand());
                    break;
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}
