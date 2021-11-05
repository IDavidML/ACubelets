package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.conversation.RenameMenu;
import me.davidml16.acubelets.conversation.TypeIconMenu;
import me.davidml16.acubelets.conversation.TypeKeyMenu;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
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

public class TypeSettings_GUI implements Listener {

    private HashMap<UUID, String> opened;
    private HashMap<String, Inventory> guis;

    private Main main;

    public TypeSettings_GUI(Main main) {
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

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Settings".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        CubeletType type = main.getCubeletTypesHandler().getTypeBydId(id);
        List<String> lore = new ArrayList<>();
        for(String line : type.getLoreAvailable()) {
            lore.add(Utils.translate(line));
        }
        gui.setItem(13, new ItemBuilder(XMaterial.NAME_TAG.parseItem()).setName(Utils.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(22, new ItemBuilder(type.getIcon()).setName(Utils.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        Utils.translate(" &7You can change the "),
                        Utils.translate(" &7icon clicking this item "),
                        Utils.translate(" &7and opening icon setup "),
                        "",
                        Utils.translate("&eClick change skull texture! ")
                )
                .toItemStack());

        gui.setItem(20, new ItemBuilder(XMaterial.ANVIL.parseItem()).setName(Utils.translate("&aCubelet type name"))
                .setLore(
                        "",
                        Utils.translate(" &7Click on the anvil "),
                        Utils.translate(" &7to start rename menu "),
                        "",
                        Utils.translate(" &7Choose 1 to rename cubelet type "),
                        Utils.translate(" &7Choose 2 to save and exit menu. "),
                        "",
                        Utils.translate("&eClick to rename cubelet! ")
                )
                .toItemStack());

        gui.setItem(24, new ItemBuilder(XMaterial.TRIPWIRE_HOOK.parseItem())
                .addGlow()
                .setName(Utils.translate("&aCubelet key"))
                .setLore(
                        "",
                        Utils.translate(" &7Open cubelets by "),
                        Utils.translate(" &7holding a key in hand, "),
                        Utils.translate(" &7a clicking a machine. "),
                        "",
                        Utils.translate("&eClick to change key item! ")
                ).toItemStack());
        
        gui.setItem(40, back);

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
        List<String> lore = new ArrayList<>();
        for(String line : type.getLoreAvailable()) {
            lore.add(Utils.translate(line));
        }
        gui.setItem(13, new ItemBuilder(XMaterial.NAME_TAG.parseItem()).setName(Utils.translate(type.getName())).setLore(lore).toItemStack());
        gui.setItem(22, new ItemBuilder(type.getIcon()).setName(Utils.translate("&aCubelet type icon"))
                .setLore(
                        "",
                        Utils.translate(" &7You can change the "),
                        Utils.translate(" &7icon clicking this item "),
                        Utils.translate(" &7and opening icon setup "),
                        "",
                        Utils.translate("&eClick change skull texture! ")
                )
                .toItemStack());

        for(HumanEntity pl : gui.getViewers()) {
            pl.getOpenInventory().getTopInventory().setContents(gui.getContents());
        }
    }

    public void open(Player p, String id) {
        p.updateInventory();

        if(!guis.containsKey(id)) loadGUI(id);

        p.openInventory(guis.get(id));

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
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

            if (slot == 20) {
                p.closeInventory();
                new RenameMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            } else if (slot == 22) {
                p.closeInventory();
                new TypeIconMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            } else if (slot == 24) {
                p.closeInventory();
                new TypeKeyMenu(main).getConversation(p, type).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            }else if (slot == 40) {
                main.getTypeConfigGUI().open(p, id);
            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}