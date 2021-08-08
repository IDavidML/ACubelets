package me.davidml16.acubelets.gui.gifts;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
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

public class Gift_GUI implements Listener {

    private HashMap<UUID, GiftGuiSession> opened;
    private List<Integer> borders;

    private Main main;

    public Gift_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, GiftGuiSession> getOpened() {
        return opened;
    }

    public void reloadGui(Player player) {
        if(opened.containsKey(player.getUniqueId())) {
            GiftGuiSession session = opened.get(player.getUniqueId());
            openPage(player, session.getTarget(), session.getTargetName(), session.getPage(), session.isOpenedByCommand());
        }
    }

    private void openPage(Player player, UUID target, String targetName, int page, boolean openedByCommand) {
        List<CubeletType> cubeletTypes = getCubeletTypesAvailable(player);

        if(page > 0 && cubeletTypes.size() < (page * 14) + 1) {
            openPage(player, target, targetName, page - 1, openedByCommand);
            return;
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("gift");

        Profile profile = main.getPlayerDataHandler().getData(player);

        Inventory gui = Bukkit.createInventory(null, 45, guiLayout.getMessage("Title"));
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        if (page > 0) {
            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;
            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");
            gui.setItem((45 - 10) + guiLayout.getSlot("PreviousPage"), item);
        }

        if (cubeletTypes.size() > (page + 1) * 14) {
            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;
            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");
            gui.setItem((45 - 10) + guiLayout.getSlot("NextPage"), item);
        }

        if(openedByCommand) {
            ItemStack close = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Close.Name"))
                    .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                    .toItemStack();
            close = NBTEditor.set(close, "close", "action");
            gui.setItem((45 - 10) + guiLayout.getSlot("Close"), close);
        } else {
            ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Back.Name"))
                    .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                    .toItemStack();
            back = NBTEditor.set(back, "back", "action");
            gui.setItem((45 - 10) + guiLayout.getSlot("Back"), back);
        }

        for(int i : borders)
            if(gui.getItem(i) == null)
                gui.setItem(i, edge);

        if (cubeletTypes.size() > 14) cubeletTypes = cubeletTypes.subList(page * 14, ((page * 14) + 14) > cubeletTypes.size() ? cubeletTypes.size() : (page * 14) + 14);

        if(cubeletTypes.size() > 0) {
            for (CubeletType cubeletType : cubeletTypes) {

                long amount = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(cubeletType.getId())).count();

                List<String> lore = new ArrayList<>();
                for (String line : guiLayout.getMessageList("Items.Cubelet.Lore")) {
                    lore.add(Utils.translate(line.replaceAll("%cubelets_available%", String.valueOf(amount))));
                }

                ItemStack item = new ItemBuilder(cubeletType.getIcon().clone())
                        .setName(Utils.translate(guiLayout.getMessage("Items.Cubelet.Name").replace("%cubelet_name%", cubeletType.getName())))
                        .setLore(lore)
                        .toItemStack();

                item = NBTEditor.set(item, "send", "action");
                item = NBTEditor.set(item, cubeletType.getId(), "typeID");

                gui.addItem(item);
            }
        }

        for(int j : borders)
            if(gui.getItem(j).equals(edge))
                gui.setItem(j, null);

        player.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(player.getUniqueId(), new GiftGuiSession(player.getUniqueId(), target, targetName, page, openedByCommand)), 1L);
    }

    public void open(Player p, UUID target, String targetName, boolean openedByCommand) {
        p.updateInventory();
        openPage(p, target, targetName, 0, openedByCommand);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            String action = NBTEditor.getString(e.getCurrentItem(), "action");

            if(e.getClick() == ClickType.DOUBLE_CLICK) return;

            if(action == null) {
                e.setCancelled(true);
                return;
            }

            GiftGuiSession session = opened.get(p.getUniqueId());

            switch (action) {
                case "send":
                    String type = NBTEditor.getString(e.getCurrentItem(), "typeID");
                    CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
                    main.getGiftAmountGUI().open(p, session.getTarget(), session.getTargetName(), cubeletType, session.isOpenedByCommand());
                    break;
                case "previous":
                    openPage(p, session.getTarget(), session.getTargetName(),session.getPage() - 1, session.isOpenedByCommand());
                    break;
                case "next":
                    openPage(p, session.getTarget(), session.getTargetName(), session.getPage() + 1, session.isOpenedByCommand());
                    break;
                case "close":
                    p.closeInventory();
                    break;
                case "back":
                    main.getCubeletsGUI().open(p);
                    break;
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (opened.containsKey(p.getUniqueId())) {
            opened.remove(p.getUniqueId());
        }
    }


    private List<CubeletType> getCubeletTypesAvailable(Player player) {
        Profile profile = main.getPlayerDataHandler().getData(player);
        List<CubeletType> cubeletTypeList = new ArrayList<>();

        for (CubeletType type : main.getCubeletTypesHandler().getTypes().values()) {
            long amount = profile.getCubelets().stream().filter(cubelet -> cubelet.getType().equalsIgnoreCase(type.getId())).count();
            if (amount > 0)
                cubeletTypeList.add(type);
        }

        return cubeletTypeList;
    }


}
