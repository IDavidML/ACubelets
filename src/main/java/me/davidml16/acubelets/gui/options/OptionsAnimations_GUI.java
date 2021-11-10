package me.davidml16.acubelets.gui.options;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.animations.AnimationHandler;
import me.davidml16.acubelets.animations.AnimationSettings;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.objects.rewards.CommandReward;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class OptionsAnimations_GUI implements Listener {

    private HashMap<UUID, Integer> opened;
    private List<Integer> borders;

    private Main main;

    public OptionsAnimations_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, Integer>();
        this.borders = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44);
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, Integer> getOpened() {
        return opened;
    }

    public void openPage(Player p, int page) {

        p.updateInventory();

        List<AnimationSettings> animations = new ArrayList<>(main.getAnimationHandler().getAnimations().values());
        Collections.sort(animations);

        if(page < 0) {
            openPage(p, 0);
            return;
        }

        if(page > 0 && animations.size() < (page * 21) + 1) {
            openPage(p, page - 1);
            return;
        }

        if (animations.size() > 21) animations = animations.subList(page * 21, Math.min(((page * 21) + 21), animations.size()));

        GUILayout guiLayout = main.getLayoutHandler().getLayout("animations");

        Inventory gui = Bukkit.createInventory(null, 45, "Options | Animations");

        ItemStack back = new ItemBuilder(XMaterial.ARROW.parseItem()).setName(Utils.translate("&aBack to options")).toItemStack();
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

        for(AnimationSettings animation : animations) {

            ItemStack item = new ItemBuilder(animation.getDisplayItem())
                    .setName(Utils.translate((animation.isEnabled() ? "&a" : "&c") + "Animation " + animation.getAnimationNumber()))
                    .setLore(
                            "",
                            Utils.translate(" &7ID: &6" + animation.getId() + " "),
                            Utils.translate(" &7Display Name: &6" + animation.getDisplayName() + " "),
                            Utils.translate(" &7Status: " + (animation.isEnabled() ? "&aEnabled" : "&cDisabled") + " "),
                            "",
                            Utils.translate("&eLeft-Click » &aEnable/Disable "),
                            Utils.translate("&eRight-Click » &aEdit animation "))
                    .toItemStack();

            item = NBTEditor.set(item, animation.getId(), "animation");
            item = NBTEditor.set(item, "animation", "action");

            gui.addItem(item);

        }

        p.openInventory(gui);

        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), page), 1L);

    }

    public void open(Player p) {
        p.updateInventory();
        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
        openPage(p, 0);
    }

    public void reloadGUI() {
        for(UUID uuid : opened.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            openPage(p, opened.get(uuid));
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
            int actualPage = opened.get(p.getUniqueId());

            if(e.getClick() == ClickType.DOUBLE_CLICK) return;

            if(action == null) {
                e.setCancelled(true);
                return;
            }

            switch (action) {

                case "previous":
                    openPage(p, actualPage - 1);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    break;

                case "next":
                    openPage(p, actualPage + 1);
                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 10, 2);
                    break;

                case "animation":

                    String animation = NBTEditor.getString(e.getCurrentItem(), "animation");

                    p.sendMessage(animation);

                    break;

                case "back":
                    main.getOptionsMainGUI().open(p);
                    break;

            }

        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        opened.remove(p.getUniqueId());
    }

    private ItemStack getItem(AnimationSettings animationSettings, String status, ItemStack itemStack) {

        String name = Utils.translate((status.equalsIgnoreCase("Selected") ? "&a" : "&c") + animationSettings.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Utils.translate("&eLeft-Click » " + (status.equalsIgnoreCase("Unlocked") ? "&a" : "&c") + "Enable animation "));

        ItemStack item;
        if(status.equalsIgnoreCase("Selected"))
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).addGlow().toItemStack();
        else
            item = new ItemBuilder(itemStack).setName(name).setLore(lore).toItemStack();

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

}