package me.davidml16.acubelets.menus.admin.options;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.conversation.options.RenameAnimationConversation;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OptionsAnimationMenu extends Menu {

    public OptionsAnimationMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        AnimationSettings animation = (AnimationSettings) getAttribute(AttrType.ANIMATION_SETTINGS_ATTR);

        Inventory gui = createInventory(45, "Options | " + animation.getId());
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        gui.setItem(40, new ItemBuilder(XMaterial.BOOK.parseItem()).setName(Utils.translate("&aBack to animations")).toItemStack());

        fillPage(edge);

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

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        int slot = event.getRawSlot();

        Player player = getOwner();
        AnimationSettings animationSettings = (AnimationSettings) getAttribute(AttrType.ANIMATION_SETTINGS_ATTR);

        if(animationSettings == null) return;

        if (slot == 10) {

            animationSettings.setEnabled(!animationSettings.isEnabled());

            reloadMenu();

        } else if (slot == 19) {

            animationSettings.setNeedPermission(!animationSettings.isNeedPermission());

            reloadMenu();

        } else if (slot == 12) {

            animationSettings.setOutlineParticles(!animationSettings.isOutlineParticles());

            reloadMenu();

        } else if (slot == 22) {

            animationSettings.setFloorParticles(!animationSettings.isFloorParticles());

            reloadMenu();

        } else if (slot == 14) {

            animationSettings.setAroundParticles(!animationSettings.isAroundParticles());

            reloadMenu();

        } else if (slot == 25) {

            player.closeInventory();

            new RenameAnimationConversation(getMain()).getConversation(player, animationSettings).begin();

            playSound(SoundType.ANVIL_USE);

        } else if (slot == 40) {

            new OptionsAnimationsMenu(getMain(), player).open();

        } else if (slot >= 45 && slot <= 80) {

            if (event.getCurrentItem().getType() == Material.AIR) return;

            ItemStack clickedItem = event.getCurrentItem();
            animationSettings.setDisplayItem(clickedItem);

            reloadMenu();

        }

        getMain().getAnimationHandler().saveAnimations();

    }

    @Override
    public void OnMenuClosed() { }

}