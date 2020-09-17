package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerAnimation_GUI implements Listener {

    private Set<UUID> opened;
    private HashMap<String, Inventory> guis;

    private static List<Integer> panels = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26);

    private Main main;

    public PlayerAnimation_GUI(Main main) {
        this.main = main;
        this.opened = new HashSet<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public Set<UUID> getOpened() { return opened; }

    public void open(Player p) {
        p.updateInventory();

        Profile profile = main.getPlayerDataHandler().getData(p);
        if(!profile.getAnimation().equalsIgnoreCase("random")) {
            AnimationSettings animationSetting = main.getAnimationHandler().getAnimationSetting(profile.getAnimation());
            if (animationSetting.isNeedPermission()) {
                if (!main.getAnimationHandler().haveAnimationPermission(p, animationSetting))
                    profile.setAnimation(AnimationHandler.DEFAULT_ANIMATION);
            }
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");

        Inventory gui = Bukkit.createInventory(null, 45, guiLayout.getMessage("Title"));

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        gui.setItem(40, back);

        ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        for(int i : panels)
            gui.setItem(i, filler);

        List<AnimationSettings> animationSettings = new ArrayList<>(main.getAnimationHandler().getAnimations().values());
        Collections.sort(animationSettings);

        for(AnimationSettings animation : animationSettings)
            gui.addItem(getAnimationItem(p, guiLayout, animation.getId()));

        gui.setItem(38, getRandomAnimationItem(p, guiLayout));

        for(int i : panels)
            gui.setItem(i, null);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(p.getUniqueId()), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;
        if (e.getClick() == ClickType.DOUBLE_CLICK) return;

        if (opened.contains(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (slot == 40) {

                main.getCubeletsGUI().open(p);

            } else if (slot == 38) {

                String status = NBTEditor.getString(e.getCurrentItem(), "status");

                if(status.equalsIgnoreCase("disabled")) {
                    main.getPlayerDataHandler().getData(p).setAnimation("random");
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                    open(p);
                }

            } else {

                String animation = NBTEditor.getString(e.getCurrentItem(), "animation");
                String status = NBTEditor.getString(e.getCurrentItem(), "status");

                if(status.equalsIgnoreCase("unlocked")) {

                    main.getPlayerDataHandler().getData(p).setAnimation(animation);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                    open(p);

                }

            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private ItemStack getAnimationItem(Player player, GUILayout guiLayout, String animation) {

        AnimationSettings animationSettings = main.getAnimationHandler().getAnimationSetting(animation);

        ItemStack item = animationSettings.getDisplayItem().clone();

        if(animationSettings.isNeedPermission())
            if (main.getAnimationHandler().haveAnimationPermission(player, animationSettings))
                if (!main.getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase(animation))
                    item = getItem(guiLayout, animationSettings, "Unlocked", item);
                else
                    item = getItem(guiLayout, animationSettings, "Selected", item);
            else
                item = getItem(guiLayout, animationSettings, "Locked", XMaterial.GRAY_DYE.parseItem());
        else
            if(!main.getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase(animation))
                item = getItem(guiLayout, animationSettings, "Unlocked", item);
            else
                item = getItem(guiLayout, animationSettings, "Selected", item);

        item = NBTEditor.set(item, animation, "animation");
        return item;

    }

    private ItemStack getItem(GUILayout guiLayout, AnimationSettings animationSettings, String status, ItemStack itemStack) {

        String name = guiLayout.getMessage("Items.Animation." + status + ".Name").replaceAll("%animation%", animationSettings.getDisplayName());
        List<String> lore = guiLayout.getMessageList("Items.Animation." + status + ".Lore");

        ItemStack item;
        if(status.equalsIgnoreCase("Selected"))
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).addGlow().toItemStack();
        else
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).toItemStack();

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

    private ItemStack getRandomAnimationItem(Player player, GUILayout guiLayout) {

        ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).toItemStack();

        String status;

        if(!main.getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase("random")) {
            String name = guiLayout.getMessage("Items.RandomAnimation.NoSelected.Name");
            List<String> lore = guiLayout.getMessageList("Items.RandomAnimation.NoSelected.Lore");
            item = new ItemBuilder(item).setName(name).setLore(lore).toItemStack();
            status = "disabled";
        } else {
            String name = guiLayout.getMessage("Items.RandomAnimation.Selected.Name");
            List<String> lore = guiLayout.getMessageList("Items.RandomAnimation.Selected.Lore");
            item = new ItemBuilder(item).setName(name).setLore(lore).addGlow().toItemStack();
            status = "enabled";
        }

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

}