package me.davidml16.acubelets.menus.player;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.*;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerAnimationMenu extends Menu {

    public PlayerAnimationMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        player.updateInventory();

        Profile profile = getMain().getPlayerDataHandler().getData(player);
        if(!profile.getAnimation().equalsIgnoreCase("random")) {
            AnimationSettings animationSetting = getMain().getAnimationHandler().getAnimationSetting(profile.getAnimation());
            if (animationSetting.isNeedPermission()) {
                if (!getMain().getAnimationHandler().haveAnimationPermission(player, animationSetting))
                    profile.setAnimation(AnimationHandler.DEFAULT_ANIMATION);
            }
        }

        List<AnimationSettings> animations = new ArrayList<>(getMain().getAnimationHandler().getAnimationSettings());
        Collections.sort(animations);

        if(page < 0) {
            openPage(0);
            return;
        }

        if(page > 0 && animations.size() < (page * 14) + 1) {
            openPage(getPage() - 1);
            return;
        }

        if (animations.size() > 14) animations = animations.subList(page * 14, Math.min(((page * 14) + 14), animations.size()));

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("animations");

        Inventory gui = createInventory(45, guiLayout.getMessage("Title"));

        if (page > 0) {

            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");

            if(guiLayout.getSlot("PreviousPage") >= 0)
                gui.setItem(((45 - 10) + guiLayout.getSlot("PreviousPage")), item);

        }

        if (getMain().getAnimationHandler().getAnimationSettings().size() > (page + 1) * 14) {

            int amount = guiLayout.getBoolean("Items.NextPage.ShowPageNumber") ? (page + 2) : 1;

            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.NextPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.NextPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "next", "action");

            if(guiLayout.getSlot("NextPage") >= 0)
                gui.setItem((gui.getSize() - 10) + guiLayout.getSlot("NextPage"), item);

        }

        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Back.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Back.Name"))
                .setLore(guiLayout.getMessageList("Items.Back.Lore"))
                .toItemStack();
        back = NBTEditor.set(back, "back", "action");
        gui.setItem((gui.getSize() - 10) + guiLayout.getSlot("Back"), back);

        ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        fillTopSide(filler, 3);

        for(AnimationSettings animation : animations)
            gui.addItem(getAnimationItem(player, guiLayout, animation.getId()));

        ItemStack randomAnimation = getRandomAnimationItem(player, guiLayout);
        randomAnimation = NBTEditor.set(randomAnimation, "random", "action");
        gui.setItem((gui.getSize() - 10) + guiLayout.getSlot("RandomAnimation"), randomAnimation);

        fillTopSide(null, 3);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        Player player = getOwner();

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        String action = NBTEditor.getString(event.getCurrentItem(), "action");

        if(event.getClick() == ClickType.DOUBLE_CLICK) return;

        if(action == null)
            return;

        switch (action) {

            case "previous":
                previousPage();
                break;

            case "next":
                nextPage();
                break;

            case "random":
                String status = NBTEditor.getString(event.getCurrentItem(), "status");

                if(status.equalsIgnoreCase("disabled")) {
                    getMain().getPlayerDataHandler().getData(player).setAnimation("random");
                    playSound(SoundType.CLICK);
                    reloadMyMenu();
                }
                break;

            case "animation":
                String animation = NBTEditor.getString(event.getCurrentItem(), "animation");
                status = NBTEditor.getString(event.getCurrentItem(), "status");
                if(status.equalsIgnoreCase("unlocked")) {
                    getMain().getPlayerDataHandler().getData(player).setAnimation(animation);
                    playSound(SoundType.CLICK);
                    reloadMyMenu();
                }
                break;

            case "back":
                new CubeletsMenu(getMain(), player).open();
                break;

        }

    }

    @Override
    public void OnMenuClosed() { }

    private ItemStack getAnimationItem(Player player, GUILayout guiLayout, String animation) {

        AnimationSettings animationSettings = getMain().getAnimationHandler().getAnimationSetting(animation);

        ItemStack item = animationSettings.getDisplayItem().clone();

        if(animationSettings.isNeedPermission())
            if (getMain().getAnimationHandler().haveAnimationPermission(player, animationSettings))
                if (!getMain().getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase(animation))
                    item = getItem(guiLayout, animationSettings, "Unlocked", item);
                else
                    item = getItem(guiLayout, animationSettings, "Selected", item);
            else
                item = getItem(guiLayout, animationSettings, "Locked", XMaterial.GRAY_DYE.parseItem());
        else
            if(!getMain().getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase(animation))
                item = getItem(guiLayout, animationSettings, "Unlocked", item);
            else
                item = getItem(guiLayout, animationSettings, "Selected", item);

        item = NBTEditor.set(item, "animation", "action");
        item = NBTEditor.set(item, animation, "animation");

        return item;

    }

    private ItemStack getItem(GUILayout guiLayout, AnimationSettings animationSettings, String status, ItemStack itemStack) {

        String name = guiLayout.getMessage("Items.Animation." + status + ".Name").replaceAll("%animation%", animationSettings.getFormatedDisplayName());
        List<String> lore = guiLayout.getMessageList("Items.Animation." + status + ".Lore");

        ItemStack item;
        if(status.equalsIgnoreCase("Selected"))
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).addGlow().hideAttributes().toItemStack();
        else
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).hideAttributes().toItemStack();

        return NBTEditor.set(item, status.toLowerCase(), "status");

    }

    private ItemStack getRandomAnimationItem(Player player, GUILayout guiLayout) {

        ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseItem()).toItemStack();

        String status;

        if(!getMain().getPlayerDataHandler().getData(player).getAnimation().equalsIgnoreCase("random")) {
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