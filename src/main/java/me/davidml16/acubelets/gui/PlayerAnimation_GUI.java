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

        if (player.isOp() || player.hasPermission("acubelets.animations.animation" + animationSettings.getAnimationNumber())) {

            if (!main.getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase(animation)) {

                String name = guiLayout.getMessage("Items.Animation.Unlocked.Name").replaceAll("%animation%", animationSettings.getDisplayName());
                List<String> lore = guiLayout.getMessageList("Items.Animation.Unlocked.Lore");
                item = new ItemBuilder(item).setName(name).setLore(lore).toItemStack();
                item = NBTEditor.set(item, "unlocked", "status");

            } else {

                String name = guiLayout.getMessage("Items.Animation.Selected.Name").replaceAll("%animation%", animationSettings.getDisplayName());
                List<String> lore = guiLayout.getMessageList("Items.Animation.Selected.Lore");
                item = new ItemBuilder(item).setName(name).setLore(lore).addGlow().toItemStack();
                item = NBTEditor.set(item, "selected", "status");

            }

        } else {

            String name = guiLayout.getMessage("Items.Animation.Locked.Name").replaceAll("%animation%", animationSettings.getDisplayName());
            List<String> lore = guiLayout.getMessageList("Items.Animation.Locked.Lore");
            item = new ItemBuilder(XMaterial.GRAY_DYE.parseItem()).setName(name).setLore(lore).toItemStack();
            item = NBTEditor.set(item, "locked", "status");

        }

        item = NBTEditor.set(item, animation, "animation");

        return item;

    }

}