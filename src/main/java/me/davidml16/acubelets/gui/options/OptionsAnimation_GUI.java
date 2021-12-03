package me.davidml16.acubelets.gui.options;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.conversation.RenameMenu;
import me.davidml16.acubelets.conversation.options.RenameAnimationMenu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OptionsAnimation_GUI implements Listener {

    private Map<UUID, AnimationSettings> opened;

    private Main main;

    public OptionsAnimation_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public Map<UUID, AnimationSettings> getOpened() {
        return opened;
    }

    public void open(Player player, AnimationSettings animation) {

        Inventory gui = Bukkit.createInventory(null, 45, "Options | " + animation.getId());
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        gui.setItem(40, new ItemBuilder(XMaterial.BOOK.parseItem()).setName(Utils.translate("&aBack to animations")).toItemStack());

        for (int i = 0; i < 45; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        if(animation.isEnabled()) {
            gui.setItem(10, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aToggle enabled status"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation is "),
                            Utils.translate(" &7enable, and visible in menus. "),
                            "",
                            Utils.translate(" &7Status: &aEnabled "),
                            "",
                            Utils.translate("&eClick to disable! ")
                    )
                    .toItemStack());
        } else {
            gui.setItem(10, new ItemBuilder(XMaterial.RED_DYE.parseItem()).setName(Utils.translate("&aToggle enabled status"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation is "),
                            Utils.translate(" &7enable, and visible in menus. "),
                            "",
                            Utils.translate(" &7Status: &cDisabled "),
                            "",
                            Utils.translate("&eClick to enable! ")
                    )
                    .toItemStack());
        }

        if(animation.isNeedPermission()) {
            gui.setItem(19, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aToggle require permission"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7require permission to use it. "),
                            "",
                            Utils.translate(" &7Status: &aEnabled "),
                            "",
                            Utils.translate("&eClick to disable! ")
                    )
                    .toItemStack());
        } else {
            gui.setItem(19, new ItemBuilder(XMaterial.RED_DYE.parseItem()).setName(Utils.translate("&aToggle require permission"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7require permission to use it. "),
                            "",
                            Utils.translate(" &7Status: &cDisabled "),
                            "",
                            Utils.translate("&eClick to enable! ")
                    )
                    .toItemStack());
        }

        if(animation.isOutlineParticles()) {
            gui.setItem(12, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aToggle outline particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles outlining the box. "),
                            "",
                            Utils.translate(" &7Status: &aEnabled "),
                            "",
                            Utils.translate("&eClick to disable! ")
                    )
                    .toItemStack());
        } else {
            gui.setItem(12, new ItemBuilder(XMaterial.RED_DYE.parseItem()).setName(Utils.translate("&aToggle outline particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles outlining the box. "),
                            "",
                            Utils.translate(" &7Status: &cDisabled "),
                            "",
                            Utils.translate("&eClick to enable! ")
                    )
                    .toItemStack());
        }

        if(animation.isFloorParticles()) {
            gui.setItem(22, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aToggle floor particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles in the floor. "),
                            "",
                            Utils.translate(" &7Status: &aEnabled "),
                            "",
                            Utils.translate("&eClick to disable! ")
                    )
                    .toItemStack());
        } else {
            gui.setItem(22, new ItemBuilder(XMaterial.RED_DYE.parseItem()).setName(Utils.translate("&aToggle floor particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles in the floor. "),
                            "",
                            Utils.translate(" &7Status: &cDisabled "),
                            "",
                            Utils.translate("&eClick to enable! ")
                    )
                    .toItemStack());
        }

        if(animation.isAroundParticles()) {
            gui.setItem(14, new ItemBuilder(XMaterial.LIME_DYE.parseItem()).setName(Utils.translate("&aToggle around particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles around the box. "),
                            Utils.translate(" &7Only for some animations. "),
                            "",
                            Utils.translate(" &7Status: &aEnabled "),
                            "",
                            Utils.translate("&eClick to disable! ")
                    )
                    .toItemStack());
        } else {
            gui.setItem(14, new ItemBuilder(XMaterial.RED_DYE.parseItem()).setName(Utils.translate("&aToggle around particles"))
                    .setLore(
                            "",
                            Utils.translate(" &7Toggle if the animation "),
                            Utils.translate(" &7show particles around the box. "),
                            Utils.translate(" &7Only for some animations. "),
                            "",
                            Utils.translate(" &7Status: &cDisabled "),
                            "",
                            Utils.translate("&eClick to enable! ")
                    )
                    .toItemStack());
        }

        gui.setItem(16, new ItemBuilder(animation.getDisplayItem().clone()).hideAttributes().setName(Utils.translate("&aChange animation icon"))
                .setLore(
                        "",
                        Utils.translate(" &7Click an item from your "),
                        Utils.translate(" &7bottom inventory to "),
                        Utils.translate(" &7change the icon. "),
                        ""
                )
                .toItemStack());

        gui.setItem(25, new ItemBuilder(XMaterial.NAME_TAG.parseItem()).setName(Utils.translate("&aChange animation display name"))
                .setLore(
                        "",
                        Utils.translate(" &7Change the display name "),
                        Utils.translate(" &7showed in some menus. "),
                        "",
                        Utils.translate("&eClick to edit! ")
                )
                .toItemStack());

        player.openInventory(gui);

        Sounds.playSound(player, player.getLocation(), Sounds.MySound.CLICK, 10, 2);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(player.getUniqueId(), animation), 1L);

    }

    public void reloadGUI() {

        for(UUID uuid : opened.keySet()) {

            Player player = Bukkit.getPlayer(uuid);

            open(player, opened.get(uuid));

        }

    }

    public void reloadGUI(AnimationSettings animationSettings) {

        for(UUID uuid : opened.keySet()) {

            if(!opened.get(uuid).getId().equalsIgnoreCase(animationSettings.getId()))
                continue;

            Player player = Bukkit.getPlayer(uuid);

            open(player, animationSettings);

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

            int slot = e.getRawSlot();

            AnimationSettings animationSettings = opened.get(p.getUniqueId());

            if(animationSettings == null)
                return;

            if (slot == 10) {
                animationSettings.setEnabled(!animationSettings.isEnabled());
                reloadGUI(animationSettings);
            } else if (slot == 19) {
                animationSettings.setNeedPermission(!animationSettings.isNeedPermission());
                reloadGUI(animationSettings);
            } else if (slot == 12) {
                animationSettings.setOutlineParticles(!animationSettings.isOutlineParticles());
                reloadGUI(animationSettings);
            } else if (slot == 22) {
                animationSettings.setFloorParticles(!animationSettings.isFloorParticles());
                reloadGUI(animationSettings);
            } else if (slot == 14) {
                animationSettings.setAroundParticles(!animationSettings.isAroundParticles());
                reloadGUI(animationSettings);
            } else if (slot == 25) {
                p.closeInventory();
                new RenameAnimationMenu(main).getConversation(p, animationSettings).begin();
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 50, 3);
            } else if (slot == 40) {
                main.getOptionsAnimationsGUI().open(p);
            } else if (slot >= 45 && slot <= 80) {
                if (e.getCurrentItem().getType() == Material.AIR) return;
                ItemStack clickedItem = e.getCurrentItem();
                animationSettings.setDisplayItem(clickedItem);
                reloadGUI(animationSettings);
            }

            main.getAnimationHandler().saveAnimations();

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

}