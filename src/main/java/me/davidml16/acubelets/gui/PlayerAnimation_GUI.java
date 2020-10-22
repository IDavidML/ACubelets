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

    private Map<UUID, Integer> opened;
    private HashMap<String, Inventory> guis;

    private static List<Integer> panels = Arrays.asList(0,1,2,3,4,5,6,7,8,9,17,18,26);

    private Main main;

    public PlayerAnimation_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public Map<UUID, Integer> getOpened() { return opened; }

    public void openPage(Player p, int page) {
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
            openPage(p, 0);
            return;
        }

        if(page > 0 && animations.size() < (page * 14) + 1) {
            openPage(p, page - 1);
            return;
        }

        if (animations.size() > 14) animations = animations.subList(page * 14, Math.min(((page * 14) + 14), animations.size()));

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");

        Inventory gui = Bukkit.createInventory(null, 45, guiLayout.getMessage("Title"));

        if (page > 0) {
            int amount = guiLayout.getBoolean("Items.PreviousPage.ShowPageNumber") ? page : 1;
            ItemStack item = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.PreviousPage.Material")).get().parseMaterial(), amount)
                    .setName(guiLayout.getMessage("Items.PreviousPage.Name"))
                    .toItemStack();
            item = NBTEditor.set(item, "previous", "action");
            if(guiLayout.getSlot("PreviousPage") >= 0)
                gui.setItem(((45 - 10) + guiLayout.getSlot("PreviousPage")), item);
        }

        if (main.getAnimationHandler().getAnimations().values().size() > (page + 1) * 14) {
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
        for(int i : panels)
            gui.setItem(i, filler);


        for(AnimationSettings animation : animations)
            gui.addItem(getAnimationItem(p, guiLayout, animation.getId()));

        ItemStack randomAnimation = getRandomAnimationItem(p, guiLayout);
        randomAnimation = NBTEditor.set(randomAnimation, "random", "action");
        gui.setItem((gui.getSize() - 10) + guiLayout.getSlot("RandomAnimation"), randomAnimation);

        for(int i : panels)
            gui.setItem(i, null);

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), page), 1L);
    }

    public void open(Player p) {
        p.updateInventory();
        openPage(p, 0);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;
        if (e.getClick() == ClickType.DOUBLE_CLICK) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);

            String action = NBTEditor.getString(e.getCurrentItem(), "action");

            if(e.getClick() == ClickType.DOUBLE_CLICK) return;

            switch (Objects.requireNonNull(action)) {
                case "previous":
                    openPage(p, opened.get(p.getUniqueId()) - 1);
                    break;
                case "next":
                    openPage(p, opened.get(p.getUniqueId()) + 1);
                    break;
                case "random":
                    String status = NBTEditor.getString(e.getCurrentItem(), "status");
                    if(status.equalsIgnoreCase("disabled")) {
                        main.getPlayerDataHandler().getData(p).setAnimation("random");
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                        openPage(p, opened.get(p.getUniqueId()));
                    }
                    break;
                case "animation":
                    String animation = NBTEditor.getString(e.getCurrentItem(), "animation");
                    status = NBTEditor.getString(e.getCurrentItem(), "status");
                    if(status.equalsIgnoreCase("unlocked")) {
                        main.getPlayerDataHandler().getData(p).setAnimation(animation);
                        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                        openPage(p, opened.get(p.getUniqueId()));
                    }
                    break;
                case "back":
                    main.getCubeletsGUI().open(p);
                    break;
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

        item = NBTEditor.set(item, "animation", "action");
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