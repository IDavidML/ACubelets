package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RenameMenu;
import me.davidml16.acubelets.conversation.TypeIconMenu;
import me.davidml16.acubelets.data.CubeletType;
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

        Inventory gui = Bukkit.createInventory(null, 45, main.getLanguageHandler().getMessage("GUIs.Config.title").replaceAll("%type%", id));
        ItemStack edge = new ItemBuilder(Material.STAINED_GLASS_PANE, 1).setDurability((short) 7).setName("").toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        gui.setItem(19, new ItemBuilder(Material.ANVIL, 1).setName(ColorUtil.translate("&eCubelet type name"))
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

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        List<String> lore = new ArrayList<>();
        for(String line : type.getLore()) {
            lore.add(ColorUtil.translate(line));
        }
        gui.setItem(13, new ItemBuilder(Material.NAME_TAG, 1).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(22, new ItemBuilder(type.getIcon()).setName(ColorUtil.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7You can change the icon"),
                        ColorUtil.translate(" &7clicking this item"),
                        ColorUtil.translate(" &7and opening icon setup"),
                        "",
                        ColorUtil.translate("&eClick change skull texture")
                )
                .toItemStack());
        
        gui.setItem(40, new ItemBuilder(Material.BARRIER, 1)
                .setName(ColorUtil.translate("&cReload configuration"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7Reload configuration to"),
                        ColorUtil.translate(" &7update last changes made."),
                        "",
                        ColorUtil.translate("&eClick reload cubelets!")
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
        gui.setItem(13, new ItemBuilder(Material.NAME_TAG, 1).setName(ColorUtil.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(22, new ItemBuilder(type.getIcon()).setName(ColorUtil.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        ColorUtil.translate(" &7You can change the icon"),
                        ColorUtil.translate(" &7clicking a skull in"),
                        ColorUtil.translate(" &7your inventory"),
                        "",
                        ColorUtil.translate("&eClick to set to default")
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

            if (slot == 22) {
                CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
                p.closeInventory();
                new TypeIconMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            } else if (slot == 40) {
                if (e.getCurrentItem().getType() == Material.BARRIER) {
                    main.getPluginManager().reloadAll();
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
                    p.sendMessage(main.getLanguageHandler().getMessage("Commands.Reload"));
                }
            } else if (slot == 19 && e.getCurrentItem().getType() == Material.ANVIL) {
                CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
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