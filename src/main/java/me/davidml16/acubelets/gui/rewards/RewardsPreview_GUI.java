package me.davidml16.acubelets.gui.rewards;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.rewards.Reward;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
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

public class RewardsPreview_GUI implements Listener {

    static class Pair {

        private String id;
        private int page;
        private boolean openedExternally;

        public Pair(String id, int page) {
            this(id, page, false);
        }

        public Pair(String id, int page, boolean openedExternally) {
            this.id = id;
            this.page = page;
            this.openedExternally = openedExternally;
        }

        public String getId() {
            return id;
        }

        public int getPage() {
            return page;
        }

        public boolean isOpenedExternally() { return openedExternally; }

        @Override
        public String toString() {
            return "Pair{" +
                    "cubeletType='" + id + '\'' +
                    ", page=" + page +
                    '}';
        }
    }


    private HashMap<UUID, Pair> opened;
    private Main main;

    public RewardsPreview_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Pair> getOpened() {
        return opened;
    }

    private void openPage(Player p, String type, int page, boolean openedExternally) {

        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
        List<Reward> rewards = cubeletType.getAllRewards();

        GUILayout guiLayout = main.getLayoutHandler().getLayout("preview");

        int pageSize = getPageSize(guiLayout);

        if(page > 0 && rewards.size() < (page * pageSize) + 1) {
            openPage(p, type, page - 1, openedExternally);
            return;
        }

        if (rewards.size() > pageSize) rewards = rewards.subList(page * pageSize, Math.min(((page * pageSize) + pageSize), rewards.size()));

        int neededSize = getNeededSize(guiLayout, rewards.size());

        Inventory gui = Bukkit.createInventory(null, neededSize, guiLayout.getMessage("Title").replaceAll("%cubelet_type%", Utils.removeColors(cubeletType.getName())));

        if (page > 0) {
            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;
            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");
            if(guiLayout.getSlot("PreviousPage") >= 0)
                gui.setItem(((neededSize - 10) + guiLayout.getSlot("PreviousPage")), item);
        }

        if (cubeletType.getAllRewards().size() > (page + 1) * pageSize) {
            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;
            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");
            if(guiLayout.getSlot("NextPage") >= 0)
                gui.setItem((neededSize - 10) + guiLayout.getSlot("NextPage"), item);
        }

        if(!openedExternally) {
            ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Back.Name"))
                    .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                    .toItemStack();
            back = NBTEditor.set(back, "back", "action");
            gui.setItem((neededSize - 10) + guiLayout.getSlot("Back"), back);
        } else {
            ItemStack close = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.Close.Name"))
                    .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                    .toItemStack();
            close = NBTEditor.set(close, "close", "action");
            gui.setItem((neededSize - 10) + guiLayout.getSlot("Close"), close);
        }

        for (int i = 0; i <= (neededSize-10); i++)
            gui.setItem(i, null);

        if(rewards.size() > 0) {
            for (Reward reward : rewards) {

                List<String> lore = new ArrayList<>();

                if(reward.getIcon().getItemMeta().hasLore()) {
                    lore.addAll(reward.getIcon().getItemMeta().getLore());
                    lore.add("");
                }

                for (String line : guiLayout.getMessageList("Items.Reward.Lore")) {
                    lore.add(Utils.translate(line.replaceAll("%reward_rarity%", reward.getRarity().getName())));
                }

                gui.addItem(new ItemBuilder(reward.getIcon().clone())
                    .setName(Utils.translate(guiLayout.getMessage("Items.Reward.Name").replaceAll("%reward_name%", reward.getName())))
                    .setLore(lore)
                    .hideAttributes()
                    .toItemStack());
            }
        } else {
            p.closeInventory();
        }

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(type, page, openedExternally)), 1L);
    }

    public void open(Player p, String type, boolean openedExternally) {
        p.updateInventory();
        openPage(p, type, 0, openedExternally);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(type, 0, openedExternally)), 1L);
    }

    private int getNeededSize(GUILayout guiLayout, int cubelets) {

        int finalRows = 0;
        int rows = guiLayout.getInteger("Size.Max-Cubelets-Rows");

        if(rows < 1) rows = 1;
        else if(rows > 5) rows = 5;

        if(guiLayout.getBoolean("Size.Dynamic")) {

            if(rows == 1) {
                finalRows = 1;
            } else if(rows == 2) {
                if(cubelets <= 9) finalRows = 1;
                else finalRows = 2;
            } else if(rows == 3) {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else finalRows = 3;
            } else if(rows == 4) {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else if(cubelets >= 18 && cubelets <= 27) finalRows = 3;
                else finalRows = 4;
            } else {
                if(cubelets >= 0 && cubelets <= 9) finalRows = 1;
                else if(cubelets >= 9 && cubelets <= 18) finalRows = 2;
                else if(cubelets >= 18 && cubelets <= 27) finalRows = 3;
                else if(cubelets >= 27 && cubelets <= 36) finalRows = 4;
                else finalRows = 5;
            }

        } else {

            finalRows = rows;

        }

        return (finalRows + 1) * 9;
    }

    private int getPageSize(GUILayout guiLayout) {

        int rows = guiLayout.getInteger("Size.Max-Cubelets-Rows");

        if(rows < 1) rows = 1;
        else if(rows > 5) rows = 5;

        return rows * 9;

    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);

            int slot = e.getRawSlot();
            int size = p.getOpenInventory().getTopInventory().getSize();
            GUILayout guiLayout = main.getLayoutHandler().getLayout("preview");

            if (slot >= (size - 9) && slot <= size) {

                Pair pair = opened.get(p.getUniqueId());
                String action = NBTEditor.getString(e.getCurrentItem(), "action");

                if(e.getClick() == ClickType.DOUBLE_CLICK) return;

                switch (Objects.requireNonNull(action)) {
                    case "previous":
                        openPage(p, pair.getId(), pair.getPage() - 1, pair.isOpenedExternally());
                        break;
                    case "next":
                        openPage(p, pair.getId(), pair.getPage() + 1, pair.isOpenedExternally());
                        break;
                    case "close":
                        p.closeInventory();
                        break;
                    case "back":
                        main.getCubeletsGUI().open(p);
                        break;
                }

            }

            p.updateInventory();
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}
