package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.*;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.TimeAPI.TimeUtils;
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

    private void openPage(Player p, String type, int page) {

        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(type);
        List<Reward> rewards = cubeletType.getAllRewards();

        GUILayout guiLayout = main.getLayoutHandler().getLayout("preview");

        if(page > 0 && rewards.size() < (page * 27) + 1) {
            openPage(p, type, page - 1);
            return;
        }

        if (rewards.size() > 27) rewards = rewards.subList(page * 27, Math.min(((page * 27) + 27), rewards.size()));

        int neededSize = getNeededSize(rewards.size());

        Inventory gui = Bukkit.createInventory(null, neededSize, guiLayout.getMessage("Title").replaceAll("%cubelet_type%", ColorUtil.removeColors(cubeletType.getName())));

        if (page > 0) {
            gui.setItem((neededSize - 10) + guiLayout.getSlot("PreviousPage"), new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack());
        }

        if (cubeletType.getAllRewards().size() > (page + 1) * 27) {
            gui.setItem((neededSize - 10) + guiLayout.getSlot("NextPage"), new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseItem())
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack());
        }

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        gui.setItem((neededSize - 10) + guiLayout.getSlot("Back"), back);

        for (int i = 0; i <= (neededSize-10); i++)
            gui.setItem(i, null);

        if(rewards.size() > 0) {
            for (Reward reward : rewards) {

                List<String> lore = new ArrayList<>();
                for (String line : guiLayout.getMessageList("Items.Reward.Lore")) {
                    lore.add(ColorUtil.translate(line.replaceAll("%reward_rarity%", reward.getRarity().getName())));
                }

                gui.addItem(new ItemBuilder(reward.getIcon())
                    .setName(ColorUtil.translate(guiLayout.getMessage("Items.Reward.Name").replaceAll("%reward_name%", reward.getName())))
                    .setLore(lore)
                    .hideAttributes()
                    .toItemStack());
            }
        } else {
            p.closeInventory();
        }

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(type, page)), 1L);
    }

    public void open(Player p, String type) {
        p.updateInventory();
        openPage(p, type, 0);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new Pair(type, 0)), 1L);
    }

    public int getNeededSize(int cubelets) {
        if(cubelets <= 9) return 18;
        else if(cubelets <= 18) return 27;
        else if(cubelets <= 27) return 36;
        return 36;
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

            if(e.getClick() != ClickType.DOUBLE_CLICK) {
                if (slot == ((size - 10) + guiLayout.getSlot("PreviousPage"))) {
                    openPage(p, opened.get(p.getUniqueId()).getCubeletType(), opened.get(p.getUniqueId()).getPage() - 1);
                } else if (slot == ((size - 10) + guiLayout.getSlot("NextPage"))) {
                    openPage(p, opened.get(p.getUniqueId()).getCubeletType(), opened.get(p.getUniqueId()).getPage() + 1);
                } else if (slot == ((size - 10) + guiLayout.getSlot("Back"))) {
                    main.getCubeletsGUI().open(p);
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
