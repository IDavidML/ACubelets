package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.gui.rewards.Rewards_GUI;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.objects.rewards.Reward;
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

    private HashMap<UUID, GUISession> opened;
    private List<Integer> borders;

    private Main main;

    public Animations_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, GUISession>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, GUISession> getOpened() {
        return opened;
    }

    public void openPage(Player p, CubeletType cubeletType, int page) {

        p.updateInventory();

        Profile profile = main.getPlayerDataHandler().getData(p);
        if(!profile.getAnimation().equalsIgnoreCase("random")) {
            AnimationSettings animationSetting = main.getAnimationHandler().getAnimationSetting(profile.getAnimation());
            if (animationSetting.isNeedPermission()) {
                if (!main.getAnimationHandler().haveAnimationPermission(p, animationSetting))
                    profile.setAnimation(AnimationHandler.DEFAULT_ANIMATION);
            }
        }

        List<AnimationSettings> animations = new ArrayList<>(main.getAnimationHandler().getAnimations().values());
        Collections.sort(animations);

        if(page < 0) {
            openPage(p, cubeletType, 0);
            return;
        }

        if(page > 0 && animations.size() < (page * 21) + 1) {
            openPage(p, cubeletType, page - 1);
            return;
        }

        if (animations.size() > 21) animations = animations.subList(page * 21, Math.min(((page * 21) + 21), animations.size()));

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");

        Inventory gui = Bukkit.createInventory(null, 45, "%cubelet_type% | Animations".replaceAll("%cubelet_type%", cubeletType.getId()));

        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to config")).toItemStack();
        back = NBTEditor.set(back, "back", "action");

        ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        for(int i : borders)
            gui.setItem(i, filler);

        if (page > 0) {

            ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), 1)
                    .setName(Utils.translate("&aPrevious page"))
                    .toItemStack();

            item = NBTEditor.set(item, "previous", "action");

            gui.setItem(18, item);

        }

        if (main.getAnimationHandler().getAnimations().values().size() > (page + 1) * 21) {

            ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), 1)
                    .setName(Utils.translate("&aaNext page"))
                    .toItemStack();

            item = NBTEditor.set(item, "next", "action");

            gui.setItem(26, item);

        }

        gui.setItem(40, back);

        for(AnimationSettings animation : animations)
            gui.addItem(getAnimationItem(cubeletType, animation.getId()));

        ItemStack randomAnimation = getRandomAnimationItem(cubeletType, guiLayout);
        randomAnimation = NBTEditor.set(randomAnimation, "animation", "action");
        gui.setItem(38, randomAnimation);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), new GUISession(cubeletType, page)), 1L);

    }

    public void open(Player p, CubeletType cubeletType) {
        p.updateInventory();
        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        openPage(p, cubeletType, 0);
    }

    public void reloadGUI(CubeletType cubeletType) {
        for(UUID uuid : opened.keySet()) {
            if(opened.get(uuid).getCubeletType().getId().equals(cubeletType.getId())) {
                Player p = Bukkit.getPlayer(uuid);
                openPage(p, cubeletType, opened.get(uuid).getPage());
            }
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (opened.containsKey(p.getUniqueId())) {

            e.setCancelled(true);

            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() == Material.AIR) return;
            if (e.getClick() == ClickType.DOUBLE_CLICK) return;

            String action = NBTEditor.getString(e.getCurrentItem(), "action");
            GUISession guiSession = opened.get(p.getUniqueId());
            CubeletType cubeletType = guiSession.getCubeletType();

            if(e.getClick() == ClickType.DOUBLE_CLICK) return;

            if(action == null) {
                e.setCancelled(true);
                return;
            }

            switch (action) {

                case "previous":
                    openPage(p, guiSession.getCubeletType(), guiSession.getPage() - 1);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    break;

                case "next":
                    openPage(p, guiSession.getCubeletType(), guiSession.getPage() + 1);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    break;

                case "animation":

                    String status = NBTEditor.getString(e.getCurrentItem(), "status");
                    String animation = NBTEditor.getString(e.getCurrentItem(), "animation");

                    if(status.equalsIgnoreCase("unlocked")) {

                        FileConfiguration config = main.getCubeletTypesHandler().getConfig(cubeletType.getId());

                        config.set("type.animation", animation);
                        cubeletType.setAnimation(animation);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);

                        cubeletType.saveType();
                        reloadGUI(cubeletType);

                    }

                    break;

                case "back":
                    main.getTypeConfigGUI().open(p, cubeletType.getId());
                    break;

            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private ItemStack getAnimationItem(CubeletType cubeletType, String animation) {

        AnimationSettings animationSettings = main.getAnimationHandler().getAnimationSetting(animation);

        ItemStack item = animationSettings.getDisplayItem().clone();

        if(!cubeletType.getAnimation().equalsIgnoreCase(animation))
            item = getItem(animationSettings, "Unlocked", item);
        else
            item = getItem(animationSettings, "Selected", item);

        item = NBTEditor.set(item, animation, "animation");
        item = NBTEditor.set(item, "animation", "action");

        return item;

    }

    private ItemStack getItem(AnimationSettings animationSettings, String status, ItemStack itemStack) {

        String name = Utils.translate((status.equalsIgnoreCase("Selected") ? "&a" : "&c") + animationSettings.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.translate("&eLeft-Click » " + (status.equalsIgnoreCase("Unlocked") ? "&a" : "&c") + "Enable animation "));

        ItemStack item;
        if(status.equalsIgnoreCase("Selected"))
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).addGlow().hideAttributes().toItemStack();
        else
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).hideAttributes().toItemStack();

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

    private ItemStack getRandomAnimationItem(CubeletType cubeletType, GUILayout guiLayout) {

        ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).toItemStack();

        String status;

        if(!cubeletType.getAnimation().equalsIgnoreCase("random")) {
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

        item = NBTEditor.set(item, "random", "animation");
        item = NBTEditor.set(item, status.toLowerCase(), "status");

        return item;

    }

    class GUISession {

        private CubeletType cubeletType;
        private int page;

        public GUISession(CubeletType cubeletType, int page) {
            this.cubeletType = cubeletType;
            this.page = page;
        }

        public CubeletType getCubeletType() {
            return cubeletType;
        }

        public void setCubeletType(CubeletType cubeletType) {
            this.cubeletType = cubeletType;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

    }

}