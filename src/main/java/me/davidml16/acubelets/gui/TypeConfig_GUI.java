package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RenameMenu;
import me.davidml16.acubelets.conversation.TypeIconMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
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
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        gui.setItem(4, new ItemBuilder(type.getIcon().clone()).setName(Utils.translate(type.getName())).toItemStack());

        gui.setItem(19, new ItemBuilder(XMaterial.ARMOR_STAND.parseItem())
                .setName(Utils.translate("&aAnimations"))
                .setLore(
                        "",
                        Utils.translate(" &7Open animations gui to "),
                        Utils.translate(" &7chance cubelet animation. "),
                        Utils.translate(" &7to begin rarity setup. "),
                        "",
                        Utils.translate("&eClick to config animations! ")
                ).toItemStack());

        gui.setItem(21, new ItemBuilder(XMaterial.ITEM_FRAME.parseItem())
                .setName(Utils.translate("&aRarities"))
                .setLore(
                        "",
                        Utils.translate(" &7Open rarities gui and "),
                        Utils.translate(" &7click on new rarity "),
                        Utils.translate(" &7to begin rarity setup. "),
                        "",
                        Utils.translate(" &7Click the rarity item "),
                        Utils.translate(" &7in the GUI to remove it. "),
                        "",
                        Utils.translate("&eClick to config rarities! ")
                ).toItemStack());

        gui.setItem(23, new ItemBuilder(XMaterial.GOLD_NUGGET.parseItem())
                .setName(Utils.translate("&aRewards"))
                .setLore(
                        "",
                        Utils.translate(" &7Open rewards gui and "),
                        Utils.translate(" &7click on new reward "),
                        Utils.translate(" &7to begin reward setup. "),
                        "",
                        Utils.translate(" &7Click the rewards item "),
                        Utils.translate(" &7in the GUI to remove it. "),
                        "",
                        Utils.translate("&eClick to config rewards! ")
                ).toItemStack());

        gui.setItem(25, new ItemBuilder(XMaterial.CHEST.parseItem()).setName(Utils.translate("&aSettings"))
                .setLore(
                        "",
                        Utils.translate(" &7Click on the chest "),
                        Utils.translate(" &7to change some settings "),
                        "",
                        Utils.translate(" &7- Cubelet type name "),
                        Utils.translate(" &7- Cubelet type icon "),
                        Utils.translate(" &7- Cubelet type key "),
                        "",
                        Utils.translate("&eClick to open settings menu! ")
                )
                .toItemStack());
        
        gui.setItem(40, new ItemBuilder(XMaterial.BARRIER.parseItem())
                .setName(Utils.translate("&cReload configuration "))
                .setLore(
                        "",
                        Utils.translate(" &7Reload configuration to "),
                        Utils.translate(" &7update last changes made. "),
                        "",
                        Utils.translate("&eClick reload cubelets! ")
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
            loadGUI(id);
            reloadGUI(id);
        }
    }

    public void reloadGUI(String id) {
        Inventory gui = guis.get(id);

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        gui.setItem(4, new ItemBuilder(type.getIcon().clone()).setName(Utils.translate(type.getName())).toItemStack());

        for(HumanEntity pl : gui.getViewers()) {
            pl.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }
    }

    public void open(Player p, String id) {
        p.updateInventory();

        if(!guis.containsKey(id)) loadGUI(id);

        p.openInventory(guis.get(id));

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), id), 1L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;

            int slot = e.getRawSlot();
            String id = opened.get(p.getUniqueId());

            CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);

            if (slot == 19) {
                main.getAnimationsGUI().open(p, id);
            } else if (slot == 21) {
                main.getRaritiesGUI().open(p, id);
            } else if (slot == 23) {
                main.getRewardsGUI().open(p, id);
            } else if (slot == 25) {
                main.getTypeSettingsGUI().open(p, id);
            } else if (slot == 40) {
                main.getPluginHandler().reloadAll();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
                p.sendMessage(main.getLanguageHandler().getMessage("Commands.Reload"));
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}