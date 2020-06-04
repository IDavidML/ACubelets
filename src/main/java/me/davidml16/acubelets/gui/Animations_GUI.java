package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
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

        Inventory gui = Bukkit.createInventory(null, 54, "%cubelet_type% | Animations".replaceAll("%cubelet_type%", id));
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(ColorUtil.translate("&aBack to config")).toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        /*NORMAL*/
        gui.setItem(10, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(ColorUtil.translate("&e&lNormal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 1")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 1")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(12, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 2")).toItemStack());
        } else {
            gui.setItem(12, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 2")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(13, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 3")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 3")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation4")) {
            gui.setItem(14, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 4")).toItemStack());
        } else {
            gui.setItem(14, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 4")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        gui.setItem(15, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(16, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());

        /*SEASONAL*/
        gui.setItem(28, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(ColorUtil.translate("&e&lSeasonal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("summer")) {
            gui.setItem(29, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aSummer Animation")).toItemStack());
        } else {
            gui.setItem(29, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cSummer Animation")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        gui.setItem(30, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(31, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(32, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(33, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(34, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());

        for (int i = 0; i < 54; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        gui.setItem(49, back);

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

        /*NORMAL*/
        gui.setItem(10, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(ColorUtil.translate("&e&lNormal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 1")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 1")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(12, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 2")).toItemStack());
        } else {
            gui.setItem(12, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 2")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(13, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 3")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 3")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation4")) {
            gui.setItem(14, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aAnimation 4")).toItemStack());
        } else {
            gui.setItem(14, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cAnimation 4")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        gui.setItem(15, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(16, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());

        /*SEASONAL*/
        gui.setItem(28, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(ColorUtil.translate("&e&lSeasonal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("summer")) {
            gui.setItem(29, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(ColorUtil.translate("&aSummer Animation")).toItemStack());
        } else {
            gui.setItem(29, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(ColorUtil.translate("&cSummer Animation")).setLore("", ColorUtil.translate("&eClick to enable!")).toItemStack());
        }

        gui.setItem(30, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(31, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(32, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(33, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());
        gui.setItem(34, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(ColorUtil.translate("&cComing soon")).toItemStack());

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

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if ((slot >= 11 && slot <= 14) || (slot == 29)) {
                chanceAnimationConfig(p, slot);
            } else if (slot == 49) {
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
            case 11:
                changeAnimation(config, p, "animation1", cubeletType);
                break;
            case 12:
                changeAnimation(config, p, "animation2", cubeletType);
                break;
            case 13:
                changeAnimation(config, p, "animation3", cubeletType);
                break;
            case 14:
                changeAnimation(config, p, "animation4", cubeletType);
                break;
            case 29:
                changeAnimation(config, p, "summer", cubeletType);
                break;
            default:
                break;
        }
        cubeletType.saveType();
        reloadGUI(id);
    }

    private void changeAnimation(FileConfiguration config, Player player, String animation, CubeletType cubeletType) {
        if(!Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase(animation)) {
            config.set("type.animation", animation);
            cubeletType.setAnimation(animation);
            Sounds.playSound(player, player.getLocation(), Sounds.MySound.CLICK, 100, 3);
        }
    }

}