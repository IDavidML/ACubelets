package me.davidml16.acubelets.menus.admin.options;

import com.cryptomorin.xseries.XMaterial;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionsAnimationsMenu extends Menu {

    public OptionsAnimationsMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        player.updateInventory();

        List<AnimationSettings> animations = new ArrayList<>(getMain().getAnimationHandler().getAnimations().values());
        Collections.sort(animations);

        if(page < 0) {
            openPage(0);
            return;
        }

        if(page > 0 && animations.size() < (page * 21) + 1) {
            openPage(getPage() - 1);
            return;
        }

        if (animations.size() > 21) animations = animations.subList(page * 21, Math.min(((page * 21) + 21), animations.size()));

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("animations");

        Inventory gui = createInventory(45, "Options | Animations");

        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to options")).toItemStack();
        back = NBTEditor.set(back, "back", "action");

        ItemStack filler = XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
        fillBorders(filler);

        if (page > 0) {

            ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), 1)
                    .setName(Utils.translate("&aPrevious page"))
                    .toItemStack();

            item = NBTEditor.set(item, "previous", "action");

            gui.setItem(18, item);

        }

        if (getMain().getAnimationHandler().getAnimations().values().size() > (page + 1) * 21) {

            ItemStack item = new ItemBuilder(XMaterial.ENDER_PEARL.parseMaterial(), 1)
                    .setName(Utils.translate("&aaNext page"))
                    .toItemStack();

            item = NBTEditor.set(item, "next", "action");

            gui.setItem(26, item);

        }

        gui.setItem(40, back);

        for(AnimationSettings animation : animations) {

            ItemStack item = new ItemBuilder(animation.getDisplayItem())
                    .hideAttributes()
                    .setName(Utils.translate((animation.isEnabled() ? "&a" : "&c") + "Animation " + animation.getAnimationNumber()))
                    .setLore(
                            "",
                            Utils.translate(" &7ID: &6" + animation.getId() + " "),
                            Utils.translate(" &7Display Name: &6" + animation.getFormatedDisplayName() + " "),
                            Utils.translate(" &7Status: " + (animation.isEnabled() ? "&aEnabled" : "&cDisabled") + " "),
                            Utils.translate(" &7Default: " + (getMain().getAnimationHandler().DEFAULT_ANIMATION.equalsIgnoreCase(animation.getId()) ? "&atrue" : "&cfalse") + " "),
                            "",
                            Utils.translate("&eClick to edit settings "))
                    .toItemStack();

            item = NBTEditor.set(item, animation.getId(), "animation");
            item = NBTEditor.set(item, "animation", "action");

            gui.addItem(item);

        }

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getClick() == ClickType.DOUBLE_CLICK) return;

        String action = NBTEditor.getString(event.getCurrentItem(), "action");

        if(action == null) return;

        Player player = getOwner();

        switch (action) {

            case "previous":
                previousPage();
                break;

            case "next":
                nextPage();
                break;

            case "animation":

                String animation = NBTEditor.getString(event.getCurrentItem(), "animation");

                AnimationSettings animationSettings = getMain().getAnimationHandler().getAnimationSetting(animation);

                if(animationSettings == null) return;

                OptionsAnimationMenu optionsAnimationMenu = new OptionsAnimationMenu(getMain(), player);
                optionsAnimationMenu.setAttribute(AttrType.ANIMATION_SETTINGS_ATTR, animationSettings);
                optionsAnimationMenu.open();

                break;

            case "back":
                new OptionsMainMenu(getMain(), player).open();
                break;

        }

    }

    @Override
    public void OnMenuClosed() { }

}