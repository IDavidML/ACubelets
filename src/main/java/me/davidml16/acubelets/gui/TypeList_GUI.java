package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
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

public class TypeList_GUI implements Listener {

    private HashMap<UUID, Integer> opened;
    private List<Integer> borders;

    private Main main;

    public TypeList_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, Integer>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    private void openPage(Player p, int page) {
        List<CubeletType> types = new ArrayList<>(main.getCubeletTypesHandler().getTypes().values());

        if(page > 0 && types.size() < (page * 21) + 1) {
            openPage(p, page - 1);
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 45, "Cubelet Types");

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack close = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aClose")).toItemStack();

        gui.setItem(40, close);

        for (Integer i : borders) {
            gui.setItem(i, edge);
        }

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

        if (!opened.containsKey(p.getUniqueId())) {
            p.openInventory(gui);
        } else {
            p.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), page), 1L);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), 0), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            int slot = e.getRawSlot();
            if ((slot >= 10 && slot <= 16) || (slot >= 19 && slot <= 25) || (slot >= 28 && slot <= 34)) {

                if (e.getCurrentItem().getType() == Material.AIR) return;

                String id = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                if (main.getCubeletTypesHandler().getTypes().size() == 0) return;

                if(main.getCubeletTypesHandler().getTypes().keySet().contains(id))
                    main.getTypeConfigGUI().open(p, id);
                else
                    p.closeInventory();

            } else if(slot == 40) {
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}
