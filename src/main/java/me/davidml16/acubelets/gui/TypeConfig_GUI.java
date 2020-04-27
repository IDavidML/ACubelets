package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RenameMenu;
import me.davidml16.acubelets.conversation.TypeIconMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class TypeConfig_GUI implements Listener {

    private HashMap<UUID, String> opened;
    private HashMap<String, Inventory> guis;

    private Main main;

    public TypeConfig_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, String>();
        this.guis = new HashMap<String, Inventory>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, String> getOpened() {
        return opened;
    }

    public HashMap<String, Inventory> getGuis() {
        return guis;
    }

    public void loadGUI() {
        for (File file : Objects.requireNonNull(new File(main.getDataFolder(), "types").listFiles())) {
            loadGUI(file.getName().toLowerCase().replace(".yml", ""));
        }
    }

    public void loadGUI(String id) {
        if(guis.containsKey(id)) return;

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Configuration".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(Material.STAINED_GLASS_PANE, 1).setDurability((short) 7).setName("").toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        List<String> lore = new ArrayList<>();
        for(String line : type.getLore()) {
            lore.add(ColorUtil.translate(line));
        }
        gui.setItem(10, new ItemBuilder(Material.NAME_TAG, 1).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(19, new ItemBuilder(type.getIcon()).setName(ColorUtil.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7You can change the "),
                        ColorUtil.translate(" &7icon clicking this item "),
                        ColorUtil.translate(" &7and opening icon setup "),
                        "",
                        ColorUtil.translate("&eClick change skull texture! ")
                )
                .toItemStack());

        gui.setItem(21, new ItemBuilder(Material.ANVIL, 1).setName(ColorUtil.translate("&aCubelet type name"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Click on the anvil "),
                        ColorUtil.translate(" &7to start rename menu "),
                        "",
                        ColorUtil.translate(" &7Choose 1 to rename cubelet type "),
                        ColorUtil.translate(" &7Choose 2 to save and exit menu. "),
                        "",
                        ColorUtil.translate("&eClick to rename cubelet! ")
                )
                .toItemStack());

        gui.setItem(23, new ItemBuilder(Material.ARMOR_STAND, 1)
                .setName(ColorUtil.translate("&aAnimations"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Open animations gui to "),
                        ColorUtil.translate(" &7chance cubelet animation. "),
                        ColorUtil.translate(" &7to begin rarity setup. "),
                        "",
                        ColorUtil.translate("&eClick to config animations! ")
                ).toItemStack());

        gui.setItem(16, new ItemBuilder(Material.ITEM_FRAME, 1)
                .setName(ColorUtil.translate("&aRarities"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Open rarities gui and "),
                        ColorUtil.translate(" &7click on new rarity "),
                        ColorUtil.translate(" &7to begin rarity setup. "),
                        "",
                        ColorUtil.translate(" &7Click the rarity item "),
                        ColorUtil.translate(" &7in the GUI to remove it. "),
                        "",
                        ColorUtil.translate("&eClick to config rarities! ")
                ).toItemStack());

        gui.setItem(25, new ItemBuilder(Material.GOLD_NUGGET, 1)
                .setName(ColorUtil.translate("&aRewards"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Open rewards gui and "),
                        ColorUtil.translate(" &7click on new reward "),
                        ColorUtil.translate(" &7to begin reward setup. "),
                        "",
                        ColorUtil.translate(" &7Click the rewards item "),
                        ColorUtil.translate(" &7in the GUI to remove it. "),
                        "",
                        ColorUtil.translate("&eClick to config rewards! ")
                ).toItemStack());
        
        gui.setItem(40, new ItemBuilder(Material.BARRIER, 1)
                .setName(ColorUtil.translate("&cReload configuration "))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Reload configuration to "),
                        ColorUtil.translate(" &7update last changes made. "),
                        "",
                        ColorUtil.translate("&eClick reload cubelets! ")
                )
                .toItemStack());

        for (int i = 0; i < 45; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        guis.put(id, gui);
    }

    public void reloadAllGUI() {
        for(String id : main.getCubeletTypesHandler().getTypes().keySet()) {
            reloadGUI(id);
        }
    }

    public void reloadGUI(String id) {
        Inventory gui = guis.get(id);

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        List<String> lore = new ArrayList<>();
        for(String line : type.getLore()) {
            lore.add(ColorUtil.translate(line));
        }
        gui.setItem(10, new ItemBuilder(Material.NAME_TAG, 1).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(19, new ItemBuilder(type.getIcon()).setName(ColorUtil.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7You can change the icon "),
                        ColorUtil.translate(" &7clicking a skull in "),
                        ColorUtil.translate(" &7your inventory "),
                        "",
                        ColorUtil.translate("&eClick to set to default! ")
                )
                .toItemStack());

        for(HumanEntity pl : gui.getViewers()) {
            pl.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }
    }

    public void open(Player p, String id) {
        p.updateInventory();
        p.openInventory(guis.get(id));

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), id), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);

            int slot = e.getRawSlot();
            String id = opened.get(p.getUniqueId());

            CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);

            if (slot == 19) {
                p.closeInventory();
                new TypeIconMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            } else if (slot == 16 && e.getCurrentItem().getType() == Material.ITEM_FRAME) {
                main.getRaritiesGUI().open(p, id);
            } else if (slot == 23 && e.getCurrentItem().getType() == Material.ARMOR_STAND) {
                main.getAnimationsGUI().open(p, id);
            } else if (slot == 25 && e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
                main.getRewardsGUI().open(p, id);
            } else if (slot == 40 && e.getCurrentItem().getType() == Material.BARRIER) {
                main.getPluginHandler().reloadAll();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
                p.sendMessage(main.getLanguageHandler().getMessage("Commands.Reload"));
            } else if (slot == 21 && e.getCurrentItem().getType() == Material.ANVIL) {
                p.closeInventory();
                new RenameMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}