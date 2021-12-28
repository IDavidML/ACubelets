package me.davidml16.acubelets.menus;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.Utils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class EditBoxMenu extends Menu {

    public EditBoxMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        player.updateInventory();

        CubeletBox cubeletBox = (CubeletBox) getAttribute(AttrType.CUBELET_BOX_ATTR);

        Inventory gui = createInventory(36, "Machine editor");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        gui.setItem(10, new ItemBuilder(upArrow).setName(Utils.translate("&aIncrease height")).setLore(
                "",
                Utils.translate(" &7Actual height: &6" + String.format("%.3f", cubeletBox.getBlockHeight())),
                Utils.translate(" &7New height: &6" + String.format("%.3f", (cubeletBox.getBlockHeight() + 0.015))),
                "",
                Utils.translate("&eClick to increase height")
        ).toItemStack());
        gui.setItem(11, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(Utils.translate("&aReset height to default")).setLore(
                "",
                Utils.translate(" &7New height: &6" + String.format("%.3f", cubeletBox.getPermanentBlockHeight())),
                "",
                Utils.translate("&eClick » &aReset height"),
                Utils.translate("&eShift-Click » &aSet default height to actual")
        ).toItemStack());
        gui.setItem(12, new ItemBuilder(downArrow).setName(Utils.translate("&aDecrease height")).setLore(
                "",
                Utils.translate(" &7Actual height: &6" + String.format("%.3f", cubeletBox.getBlockHeight())),
                Utils.translate(" &7New height: &6" + String.format("%.3f", (cubeletBox.getBlockHeight() - 0.015))),
                "",
                Utils.translate("&eClick to decrease height")
        ).toItemStack());
        gui.setItem(14, new ItemBuilder(XMaterial.COMPASS.parseItem()).setName(Utils.translate("&aChange direction")).setLore(
                "",
                Utils.translate(" " + (cubeletBox.getRotation() == Rotation.NORTH ? "&eNorth" : "&7North")),
                Utils.translate(" " + (cubeletBox.getRotation() == Rotation.SOUTH ? "&eSouth" : "&7South")),
                Utils.translate(" " + (cubeletBox.getRotation() == Rotation.EAST ? "&eEast" : "&7East")),
                Utils.translate(" " + (cubeletBox.getRotation() == Rotation.WEST ? "&eWest" : "&7West")),
                "",
                Utils.translate("&eClick to change direction")
        ).toItemStack());
        gui.setItem(16, new ItemBuilder(remove).setName(Utils.translate("&cRemove cubelet machine")).setLore(
                "",
                Utils.translate("&eClick to remove this machine")
        ).toItemStack());

        fillPage(edge);

        GUILayout guiLayout = getMain().getLayoutHandler().getLayout("opencubelet");
        ItemStack back = new ItemBuilder(XMaterial.matchXMaterial(guiLayout.getMessage("Items.Close.Material")).get().parseItem())
                .setName(guiLayout.getMessage("Items.Close.Name"))
                .setLore(guiLayout.getMessageList("Items.Close.Lore"))
                .toItemStack();
        gui.setItem(31, back);

        openInventory();

    }

    @Override
    public void OnMenuClick(InventoryClickEvent event) {

        Player player = getOwner();

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        int slot = event.getRawSlot();

        CubeletBox cubeletBox = (CubeletBox) getAttribute(AttrType.CUBELET_BOX_ATTR);

        if(slot == 10) {

            cubeletBox.setBlockHeight(cubeletBox.getBlockHeight() + 0.015);
            getMain().getHologramImplementation().moveHologram(cubeletBox);

            playSound(SoundType.CLICK);

            reloadMyMenu();

        } else if(slot == 11) {

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.RIGHT) {

                cubeletBox.setBlockHeight(cubeletBox.getPermanentBlockHeight());

                playSound(SoundType.CHICKEN_EGG_POP);

                reloadMyMenu();

                getMain().getHologramImplementation().moveHologram(cubeletBox);
                Bukkit.getScheduler().runTaskLater(getMain(), () -> getMain().getHologramImplementation().moveHologram(cubeletBox), 5L);

            } else if(event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) {

                cubeletBox.setPermanentBlockHeight(cubeletBox.getBlockHeight());

                playSound(SoundType.CHICKEN_EGG_POP);

                reloadMyMenu();

            }

        } else if(slot == 12) {

            cubeletBox.setBlockHeight(cubeletBox.getBlockHeight() - 0.015);
            getMain().getHologramImplementation().moveHologram(cubeletBox);

            playSound(SoundType.CLICK);

            reloadMyMenu();

        } else if(slot == 14) {

            if(event.getClick() != ClickType.DOUBLE_CLICK) {

                if (cubeletBox.getRotation() == Rotation.NORTH)
                    cubeletBox.setRotation(Rotation.SOUTH);
                else if (cubeletBox.getRotation() == Rotation.SOUTH)
                    cubeletBox.setRotation(Rotation.EAST);
                else if (cubeletBox.getRotation() == Rotation.EAST)
                    cubeletBox.setRotation(Rotation.WEST);
                else if (cubeletBox.getRotation() == Rotation.WEST)
                    cubeletBox.setRotation(Rotation.NORTH);

                playSound(SoundType.CLICK);
                reloadMyMenu();

            }

        } else if(slot == 16) {

            if (getMain().getCubeletBoxHandler().getBoxes().containsKey(cubeletBox.getLocation())) {

                getMain().getCubeletBoxHandler().removeBox(cubeletBox.getLocation());

                playSound(SoundType.ANVIL_USE);

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aSuccesfully removed cubelet machine of" +
                        " &aX: &e" + cubeletBox.getLocation().getBlockX() +
                        ", &aY: &e" + cubeletBox.getLocation().getBlockY() +
                        ", &aZ: &e" + cubeletBox.getLocation().getBlockZ()));

                String menuClassType = this.getClass().getName();

                for(Menu menu : getMain().getMenuHandler().getOpenedMenus().values()) {

                    if(!menu.getClass().getName().equalsIgnoreCase(menuClassType)) continue;

                    if(!menu.getAttribute(AttrType.CUBELET_BOX_ATTR).equals(cubeletBox)) continue;

                    menu.getOwner().closeInventory();

                }

            } else {

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix() + " &cThis cubelet machine location no exists!"));

            }

        } else if (slot == 31) {

            player.closeInventory();

        }

    }

    @Override
    public void OnMenuClosed() {

        getMain().getCubeletBoxHandler().saveBoxes();

    }

}