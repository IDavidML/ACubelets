package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import java.util.*;

public class Animations_GUI implements Listener {

    private HashMap<UUID, String> opened;
    private HashMap<String, Inventory> guis;
    private List<Integer> borders;

    private Main main;

    public Animations_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, String>();
        this.guis = new HashMap<String, Inventory>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44);
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
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

        String selectedAnimation = config.getString("type.animation");

        gui.setItem(40, back);

        for (Integer i : borders) {
            gui.setItem(i, edge);
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");
        ItemStack randomAnimation = getRandomAnimationItem(selectedAnimation, guiLayout);
        randomAnimation = NBTEditor.set(randomAnimation, "random", "animation");
        gui.setItem(38, randomAnimation);

        List<AnimationSettings> animationSettings = new ArrayList<>(main.getAnimationHandler().getAnimations().values());
        Collections.sort(animationSettings);

        for(AnimationSettings animation : animationSettings)
            gui.addItem(getAnimationItem(selectedAnimation, animation.getId()));

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

        String selectedAnimation = config.getString("type.animation");

        for (int i = 0; i < 44; i++) {
            gui.setItem(i, null);
        }

        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();
        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();

        gui.setItem(40, back);

        for (Integer i : borders) {
            gui.setItem(i, edge);
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");
        ItemStack randomAnimation = getRandomAnimationItem(selectedAnimation, guiLayout);
        randomAnimation = NBTEditor.set(randomAnimation, "random", "animation");
        gui.setItem(38, randomAnimation);

        List<AnimationSettings> animationSettings = new ArrayList<>(main.getAnimationHandler().getAnimations().values());
        Collections.sort(animationSettings);

        for(AnimationSettings animation : animationSettings)
            gui.addItem(getAnimationItem(selectedAnimation, animation.getId()));

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

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.AIR) return;

            int slot = e.getRawSlot();
            String id = opened.get(p.getUniqueId());

            if (slot == 40) {
                main.getTypeConfigGUI().open(p, id);
            } else if (slot == 38) {

                String animation = NBTEditor.getString(e.getCurrentItem(), "animation");
                String status = NBTEditor.getString(e.getCurrentItem(), "status");

                if (status.equalsIgnoreCase("unlocked")) {

                    CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(id);
                    FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

                    config.set("type.animation", animation);
                    cubeletType.setAnimation(animation);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);

                    cubeletType.saveType();
                    reloadGUI(id);

                }

            } else {

                if(e.getCurrentItem().equals(new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack())) return;

                String animation = NBTEditor.getString(e.getCurrentItem(), "animation");
                String status = NBTEditor.getString(e.getCurrentItem(), "status");

                if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {

                    if (status.equalsIgnoreCase("unlocked")) {

                        CubeletType cubeletType = main.getCubeletTypesHandler().getTypeBydId(id);
                        FileConfiguration config = main.getCubeletTypesHandler().getConfig(id);

                        config.set("type.animation", animation);
                        cubeletType.setAnimation(animation);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);

                        cubeletType.saveType();
                        reloadGUI(id);

                    }

                } else if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {

                    sendPreviewMessage(p, animation);

                }
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private void sendPreviewMessage(Player player, String id) {
        AnimationSettings animation = main.getAnimationHandler().getAnimationSetting(id);

        player.sendMessage("");
        player.sendMessage(MessageUtils.centeredMessage(Utils.translate("&e&l" + animation.getDisplayName())));
        player.sendMessage("");
        player.sendMessage(MessageUtils.centeredMessage(Utils.translate("&a" + animation.getPreviewURL())));
        player.sendMessage("");
    }

    private ItemStack getAnimationItem(String selected, String animation) {

        AnimationSettings animationSettings = main.getAnimationHandler().getAnimationSetting(animation);

        ItemStack item = animationSettings.getDisplayItem().clone();

        if(!selected.equalsIgnoreCase(animation))
            item = getItem(animationSettings, "Unlocked", item);
        else
            item = getItem(animationSettings, "Selected", item);

        item = NBTEditor.set(item, animation, "animation");
        return item;

    }

    private ItemStack getItem(AnimationSettings animationSettings, String status, ItemStack itemStack) {

        String name = Utils.translate((status.equalsIgnoreCase("Selected") ? "&a" : "&c") + animationSettings.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.translate("&eLeft-Click » " + (status.equalsIgnoreCase("Unlocked") ? "&a" : "&c") + "Enable animation "));
        lore.add(Utils.translate("&eRight-Click » &aPreview animation "));

        ItemStack item;
        if(status.equalsIgnoreCase("Selected"))
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).addGlow().toItemStack();
        else
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).toItemStack();

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

    private ItemStack getRandomAnimationItem(String animation, GUILayout guiLayout) {

        ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).toItemStack();

        String status;

        if(!animation.equalsIgnoreCase("random")) {
            String name = guiLayout.getMessage("Items.RandomAnimation.NoSelected.Name");
            List<String> lore = guiLayout.getMessageList("Items.RandomAnimation.NoSelected.Lore");
            item = new ItemBuilder(item).setName(name).setLore(lore).toItemStack();
            status = "Unlocked";
        } else {
            String name = guiLayout.getMessage("Items.RandomAnimation.Selected.Name");
            List<String> lore = guiLayout.getMessageList("Items.RandomAnimation.Selected.Lore");
            item = new ItemBuilder(item).setName(name).setLore(lore).addGlow().toItemStack();
            status = "Selected";
        }

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

}