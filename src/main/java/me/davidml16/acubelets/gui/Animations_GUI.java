package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ACMaterial;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Animations_GUI implements Listener {

    private HashMap<UUID, String> opened;
    private HashMap<String, Inventory> guis;

    private Main main;

    public Animations_GUI(Main main) {
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

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Animations".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(ACMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(ACMaterial.ARROW.parseItem()).setName(ColorUtil.translate("&aBack to config")).toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(20, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 1")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(20, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 1")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(13, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(22, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 2")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(22, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 2")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(15, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(24, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 3")).toItemStack());
        } else {
            gui.setItem(15, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(24, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 3")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        for (int i = 0; i < 45; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        gui.setItem(40, back);

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

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(20, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 1")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(20, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 1")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(13, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(22, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 2")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(22, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 2")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(15, new ItemBuilder(ACMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&a&l[+]")).toItemStack());
            gui.setItem(24, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&aAnimation 3")).toItemStack());
        } else {
            gui.setItem(15, new ItemBuilder(ACMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&c&l[-]")).toItemStack());
            gui.setItem(24, new ItemBuilder(ACMaterial.ARMOR_STAND.parseItem()).setName(ColorUtil.translate("&cAnimation 3")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

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

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot == 20 || slot == 22 || slot == 24) {
                chanceAnimationConfig(p, slot);
            } else if (slot == 40) {
                String id = opened.get(p.getUniqueId());
                main.getTypeConfigGUI().open(p, id);
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private void chanceAnimationConfig(Player p, int slot) {
        String id = opened.get(p.getUniqueId());
        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(id);
        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);
        switch (slot) {
            case 20:
                if(!Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
                    config.set("type.animation", "animation1");
                    cubeletType.setAnimation("animation1");
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                }
                break;
            case 22:
                if(!Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
                    config.set("type.animation", "animation2");
                    cubeletType.setAnimation("animation2");
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                }
                break;
            case 24:
                if(!Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
                    config.set("type.animation", "animation3");
                    cubeletType.setAnimation("animation3");
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                }
                break;
            default:
                break;
        }
        cubeletType.saveType();
        reloadGUI(id);
    }

}