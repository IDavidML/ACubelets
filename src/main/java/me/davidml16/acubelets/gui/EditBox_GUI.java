package me.davidml16.acubelets.gui;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.utils.ColorUtil;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class EditBox_GUI implements Listener {

    private HashMap<UUID, CubeletBox> opened;

    private Main main;

    public EditBox_GUI(Main main) {
        this.main = main;
        this.opened = new HashMap<UUID, CubeletBox>();
        this.main.getServer().getPluginManager().registerEvents(this, this.main);
    }

    public HashMap<UUID, CubeletBox> getOpened() {
        return opened;
    }

    public void reloadGUI(CubeletBox box) {
        Inventory gui = Bukkit.createInventory(null, 36, "Machine editor");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        gui.setItem(10, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aIncrease height")).setLore(
                "",
                ColorUtil.translate(" &7Actual height: &6" + String.format("%.3f", box.getBlockHeight())),
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", (box.getBlockHeight() + 0.015))),
                "",
                ColorUtil.translate("&eClick to increase height")
        ).toItemStack());
        gui.setItem(11, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(ColorUtil.translate("&aReset height to default")).setLore(
                "",
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", box.getPermanentBlockHeight())),
                "",
                ColorUtil.translate("&eClick to reset height")
        ).toItemStack());
        gui.setItem(12, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aDecrease height")).setLore(
                "",
                ColorUtil.translate(" &7Actual height: &6" + String.format("%.3f", box.getBlockHeight())),
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", (box.getBlockHeight() - 0.015))),
                "",
                ColorUtil.translate("&eClick to decrease height")
        ).toItemStack());
        gui.setItem(14, new ItemBuilder(XMaterial.COMPASS.parseItem()).setName(ColorUtil.translate("&aChange direction")).setLore(
                "",
                ColorUtil.translate(" " + (box.getRotation() == Rotation.NORTH ? "&eNorth" : "&7North")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.SOUTH ? "&eSouth" : "&7South")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.EAST ? "&eEast" : "&7East")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.WEST ? "&eWest" : "&7West")),
                "",
                ColorUtil.translate("&eClick to change direction")
        ).toItemStack());
        gui.setItem(16, new ItemBuilder(remove).setName(ColorUtil.translate("&cRemove cubelet machine")).setLore(
                "",
                ColorUtil.translate("&eClick to remove this machine")
        ).toItemStack());

        for (int i = 0; i < 36; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        ItemStack back = new ItemBuilder(XMaterial.BOOK.parseItem())
                .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.Close.Name"))
                .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.Close.Lore"))
                .toItemStack();
        gui.setItem(31, back);

        for(UUID uuid : opened.keySet()) {
            if(opened.get(uuid) == box) {
                Objects.requireNonNull(Bukkit.getPlayer(uuid)).getOpenInventory().getTopInventory().setContents(gui.getContents());
            }
        }
    }

    public void open(Player p, CubeletBox box) {
        p.updateInventory();

        Inventory gui = Bukkit.createInventory(null, 36, "Machine editor");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        gui.setItem(10, new ItemBuilder(upArrow).setName(ColorUtil.translate("&aIncrease height")).setLore(
                "",
                ColorUtil.translate(" &7Actual height: &6" + String.format("%.3f", box.getBlockHeight())),
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", (box.getBlockHeight() + 0.015))),
                "",
                ColorUtil.translate("&eClick to increase height")
        ).toItemStack());
        gui.setItem(11, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(ColorUtil.translate("&aReset height to default")).setLore(
                "",
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", box.getPermanentBlockHeight())),
                "",
                ColorUtil.translate("&eClick to reset height")
        ).toItemStack());
        gui.setItem(12, new ItemBuilder(downArrow).setName(ColorUtil.translate("&aDecrease height")).setLore(
                "",
                ColorUtil.translate(" &7Actual height: &6" + String.format("%.3f", box.getBlockHeight())),
                ColorUtil.translate(" &7New height: &6" + String.format("%.3f", (box.getBlockHeight() - 0.015))),
                "",
                ColorUtil.translate("&eClick to decrease height")
        ).toItemStack());
        gui.setItem(14, new ItemBuilder(XMaterial.COMPASS.parseItem()).setName(ColorUtil.translate("&aChange direction")).setLore(
                "",
                ColorUtil.translate(" " + (box.getRotation() == Rotation.NORTH ? "&eNorth" : "&7North")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.SOUTH ? "&eSouth" : "&7South")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.EAST ? "&eEast" : "&7East")),
                ColorUtil.translate(" " + (box.getRotation() == Rotation.WEST ? "&eWest" : "&7West")),
                "",
                ColorUtil.translate("&eClick to change direction")
        ).toItemStack());
        gui.setItem(16, new ItemBuilder(remove).setName(ColorUtil.translate("&cRemove cubelet machine")).setLore(
                "",
                ColorUtil.translate("&eClick to remove this machine")
        ).toItemStack());

        for (int i = 0; i < 36; i++) {
            if(gui.getItem(i) == null) {
                gui.setItem(i, edge);
            }
        }

        ItemStack back = new ItemBuilder(XMaterial.BOOK.parseItem())
                .setName(main.getLanguageHandler().getMessage("GUI.Opening.Items.Close.Name"))
                .setLore(main.getLanguageHandler().getMessageList("GUI.Opening.Items.Close.Lore"))
                .toItemStack();
        gui.setItem(31, back);

        p.openInventory(gui);

        Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> opened.put(p.getUniqueId(), box), 1L);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem() == null) return;

        if (opened.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            CubeletBox box = opened.get(p.getUniqueId());

            if(slot == 10) {
                box.setBlockHeight(box.getBlockHeight() + 0.015);
                main.getHologramHandler().moveHologram(box);
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                reloadGUI(box);
            } else if(slot == 11) {
                box.setBlockHeight(box.getPermanentBlockHeight());
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CHICKEN_EGG_POP, 100, 3);
                reloadGUI(box);

                main.getHologramHandler().moveHologram(box);
                Bukkit.getScheduler().runTaskLater(main, () -> main.getHologramHandler().moveHologram(box), 5L);
            } else if(slot == 12) {
                box.setBlockHeight(box.getBlockHeight() - 0.015);
                main.getHologramHandler().moveHologram(box);
                Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                reloadGUI(box);
            } else if(slot == 14) {
                if(e.getClick() != ClickType.DOUBLE_CLICK) {
                    if (box.getRotation() == Rotation.NORTH)
                        box.setRotation(Rotation.SOUTH);
                    else if (box.getRotation() == Rotation.SOUTH)
                        box.setRotation(Rotation.EAST);
                    else if (box.getRotation() == Rotation.EAST)
                        box.setRotation(Rotation.WEST);
                    else if (box.getRotation() == Rotation.WEST)
                        box.setRotation(Rotation.NORTH);

                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.CLICK, 100, 3);
                    reloadGUI(box);
                }
            } else if(slot == 16) {
                if (main.getCubeletBoxHandler().getBoxes().containsKey(box.getLocation())) {
                    main.getCubeletBoxHandler().removeBox(box.getLocation());

                    Sounds.playSound(p, p.getLocation(), Sounds.MySound.ANVIL_USE, 10, 3);

                    p.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix()
                            + " &aSuccesfully removed cubelet machine of" +
                            " &aX: &e" + box.getLocation().getBlockX() +
                            ", &aY: &e" + box.getLocation().getBlockY() +
                            ", &aZ: &e" + box.getLocation().getBlockZ()));

                    for(Player player : Bukkit.getOnlinePlayers()) {
                        if(opened.get(player.getUniqueId()).getLocation().equals(box.getLocation())) {
                            p.closeInventory();
                        }
                    }
                } else {
                    p.sendMessage(ColorUtil.translate(main.getLanguageHandler().getPrefix() + " &cThis cubelet machine location no exists!"));
                }
            } else if (slot == 31) {
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void InventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(opened.containsKey(p.getUniqueId())) {
            opened.remove(p.getUniqueId());
            main.getCubeletBoxHandler().saveBoxes();
        }
    }

}