package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Profile;
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

public class PlayerAnimation_GUI implements Listener {

    private Set<UUID> opened;
    private HashMap<String, Inventory> guis;

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
        if(!profile.getAnimation().equalsIgnoreCase("animation2")) {
            AnimationSettings animationSettings = main.getAnimationHandler().getAnimationSetting(profile.getAnimation());
            if(!p.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber()))
                profile.setAnimation("animation2");
        }

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");

        Inventory gui = Bukkit.createInventory(null, 45, guiLayout.getMessage("Title"));

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        gui.setItem(40, back);

        gui.setItem(10, getAnimationItem(p, guiLayout, "animation1"));
        gui.setItem(11, getAnimationItem(p, guiLayout, "animation2"));
        gui.setItem(12, getAnimationItem(p, guiLayout, "animation3"));
        gui.setItem(13, getAnimationItem(p, guiLayout, "animation4"));
        gui.setItem(14, getAnimationItem(p, guiLayout, "summer"));
        gui.setItem(15, getAnimationItem(p, guiLayout, "easter"));
        gui.setItem(16, getAnimationItem(p, guiLayout, "halloween"));
        gui.setItem(19, getAnimationItem(p, guiLayout, "christmas"));
        gui.setItem(20, getAnimationItem(p, guiLayout, "animation9"));

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.add(p.getUniqueId()), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (opened.contains(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            if (slot == 40) {

                main.getCubeletsGUI().open(p);

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

        if(!animation.equalsIgnoreCase("animation2"))
            if (player.isOp() || player.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber()))
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

}