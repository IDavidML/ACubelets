package me.davidml16.acubelets.gui.gifts;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.GiftGuiSession;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GiftPlayer_GUI implements Listener {

    private HashMap<UUID, Integer> opened;
    private List<Integer> borders;

    private Main main;

    public GiftPlayer_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, Integer>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    public void reloadAll() {
        for(UUID uuid : opened.keySet())
            openPage(Bukkit.getPlayer(uuid), opened.get(uuid));
    }

    private void openPage(Player p, int page) {
        List<Player> players = new ArrayList<Player>(main.getServer().getOnlinePlayers());

        if(page > 0 && players.size() < (page * 21) + 1) {
            openPage(p, page - 1);
            return;
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("giftplayer");

        Inventory gui = Bukkit.createInventory(null, 45, guiLayout.getMessage("Title"));

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        for(int i : borders)
            if(gui.getItem(i) == null)
                gui.setItem(i, edge);

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
            for (Player player : players) {
                if(!player.getUniqueId().equals(p.getUniqueId())) {
                    ItemStack item = new ItemBuilder(SkullCreator.itemFromUuid(player.getUniqueId()))
                            .setName(Utils.translate(guiLayout.getMessage("Items.Player.Name").replace("%player%",player.getName())))
                            .setLore(guiLayout.getMessageList("Items.Player.Lore"))
                            .toItemStack();
                    item = NBTEditor.set(item, "player", "action");
                    item = NBTEditor.set(item, player.getName(), "name");
                    item = NBTEditor.set(item, player.getUniqueId().toString(), "uuid");
                    gui.addItem(item);
                }
            }
        }

        for(int j : borders)
            if(gui.getItem(j).equals(edge))
                gui.setItem(j, null);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), page), 1L);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);
    }

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

            switch (action) {
                case "player":
                    String name = NBTEditor.getString(e.getCurrentItem(), "name");
                    UUID uuid = UUID.fromString(NBTEditor.getString(e.getCurrentItem(), "uuid"));
                    main.getGiftGUI().open(p, uuid, name, false);
                    break;
                case "previous":
                    openPage(p, opened.get(p.getUniqueId()) - 1);
                    break;
                case "next":
                    openPage(p, opened.get(p.getUniqueId()) + 1);
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
        opened.remove(p.getUniqueId());
    }

}