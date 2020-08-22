package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.MessageUtils;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
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
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        /*NORMAL*/
        gui.setItem(10, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(Utils.translate("&e&lNormal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº1")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº1")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(12, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº2")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(12, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº2")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(13, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº3")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº3")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation4")) {
            gui.setItem(14, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº4")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(14, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº4")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation9")) {
            gui.setItem(15, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº9")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(15, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº9")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        gui.setItem(16, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());

        /*SEASONAL*/
        gui.setItem(28, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(Utils.translate("&e&lSeasonal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("summer")) {
            gui.setItem(29, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº5 (Summer)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(29, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº5 (Summer)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("easter")) {
            gui.setItem(30, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº6 (Easter)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(30, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº6 (Easter)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("halloween")) {
            gui.setItem(31, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº7 (Halloween)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(31, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº7 (Halloween)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("christmas")) {
            gui.setItem(32, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº8 (Christmas)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(32, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº8 (Christmas)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        gui.setItem(33, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());
        gui.setItem(34, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());

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
            loadGUI(id);
            reloadGUI(id);
        }
    }

    public void reloadGUI(String id) {
        Inventory gui = guis.get(id);

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        /*NORMAL*/
        gui.setItem(10, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(Utils.translate("&e&lNormal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation1")) {
            gui.setItem(11, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº1")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(11, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº1")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation2")) {
            gui.setItem(12, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº2")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(12, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº2")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation3")) {
            gui.setItem(13, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº3")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(13, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº3")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation4")) {
            gui.setItem(14, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº4")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(14, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº4")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("animation9")) {
            gui.setItem(15, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº9")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(15, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº9")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        gui.setItem(16, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());

        /*SEASONAL*/
        gui.setItem(28, new ItemBuilder(XMaterial.OAK_SIGN.parseItem()).setName(Utils.translate("&e&lSeasonal Animations")).toItemStack());

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("summer")) {
            gui.setItem(29, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº5 (Summer)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(29, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº5 (Summer)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("easter")) {
            gui.setItem(30, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº6 (Easter)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(30, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº6 (Easter)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("halloween")) {
            gui.setItem(31, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº7 (Halloween)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(31, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº7 (Halloween)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        if(Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase("christmas")) {
            gui.setItem(32, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aAnimation Nº8 (Christmas)")).setLore("", Utils.translate("&eLeft-Click » &cEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        } else {
            gui.setItem(32, new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(Utils.translate("&cAnimation Nº8 (Christmas)")).setLore("", Utils.translate("&eLeft-Click » &aEnable animation "), Utils.translate("&eRight-Click » &aPreview animation ")).toItemStack());
        }

        gui.setItem(33, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());
        gui.setItem(34, new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setName(Utils.translate("&cComing soon")).toItemStack());

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
            if ((slot >= 11 && slot <= 15) || (slot >= 29 && slot <= 32)) {
                if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT)
                    chanceAnimationConfig(p, slot);
                else if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT)
                    sendAnimationInfo(p, slot);
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
            case 15:
                changeAnimation(config, p, "animation9", cubeletType);
                break;
            case 29:
                changeAnimation(config, p, "summer", cubeletType);
                break;
            case 30:
                changeAnimation(config, p, "easter", cubeletType);
                break;
            case 31:
                changeAnimation(config, p, "halloween", cubeletType);
                break;
            case 32:
                changeAnimation(config, p, "christmas", cubeletType);
                break;
            default:
                break;
        }
        cubeletType.saveType();
        reloadGUI(id);
    }

    private void sendAnimationInfo(Player p, int slot) {
        String id = opened.get(p.getUniqueId());
        switch (slot) {
            case 11:
                sendPreviewMessage(p, "animation1");
                break;
            case 12:
                sendPreviewMessage(p, "animation2");
                break;
            case 13:
                sendPreviewMessage(p, "animation3");
                break;
            case 14:
                sendPreviewMessage(p, "animation4");
                break;
            case 15:
                sendPreviewMessage(p, "animation9");
                break;
            case 29:
                sendPreviewMessage(p, "summer");
                break;
            case 30:
                sendPreviewMessage(p, "easter");
                break;
            case 31:
                sendPreviewMessage(p, "halloween");
                break;
            case 32:
                sendPreviewMessage(p, "christmas");
                break;
            default:
                break;
        }
    }

    private void changeAnimation(FileConfiguration config, Player player, String animation, CubeletType cubeletType) {
        if(!Objects.requireNonNull(config.getString("type.animation")).equalsIgnoreCase(animation)) {
            config.set("type.animation", animation);
            cubeletType.setAnimation(animation);
            Sounds.playSound(player, player.getLocation(), Sounds.MySound.CLICK, 100, 3);
        }
    }

    private void sendPreviewMessage(Player player, String id) {
        AnimationSettings animation = main.getAnimationHandler().getAnimationSetting(id);

        player.sendMessage("");
        player.sendMessage(MessageUtils.centeredMessage(Utils.translate("&e&l" + animation.getDisplayName())));
        player.sendMessage("");
        player.sendMessage(MessageUtils.centeredMessage(Utils.translate("&a" + animation.getPreviewURL())));
        player.sendMessage("");
    }

}