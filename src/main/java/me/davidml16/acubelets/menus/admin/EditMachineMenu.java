package me.davidml16.acubelets.menus.admin;

import com.cryptomorin.xseries.XMaterial;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.enums.Rotation;
import me.davidml16.acubelets.objects.CubeletMachine;
import me.davidml16.acubelets.objects.GUILayout;
import me.davidml16.acubelets.objects.Menu;
import me.davidml16.acubelets.utils.CollectionUtils;
import me.davidml16.acubelets.utils.ItemBuilder;
import me.davidml16.acubelets.utils.SkullCreator;
import me.davidml16.acubelets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EditMachineMenu extends Menu {

    public EditMachineMenu(Main main, Player player) {

        super(main, player);

    }

    @Override
    public void OnPageOpened(int page) {

        Player player = getOwner();

        player.updateInventory();

        CubeletMachine cubeletMachine = (CubeletMachine) getAttribute(AttrType.CUBELET_BOX_ATTR);

        Inventory gui = createInventory(36, "Machine editor");
        ItemStack edge = new ItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem()).setName("").toItemStack();

        ItemStack upArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19");
        ItemStack downArrow = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19");
        ItemStack remove = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVkNjc5OTE0OTc4OGI5ZTkwMTY4MTFkM2EzZDBlZDFmNTUyNTMwZDY3Zjk4Njk0NTAzMmQ2ZTQzOWZhODk5ZCJ9fX0=");

        gui.setItem(10, new ItemBuilder(XMaterial.ANVIL.parseMaterial()).setName(Utils.translate("&aChange hologram height")).setLore(
                "",
                Utils.translate(" &7Height: &6" + String.format("%.3f", cubeletMachine.getBlockHeight())),
                "",
                Utils.translate("&eMiddle Click » &aReset height"),
                Utils.translate("&eShift Click » &aSet default height to actual"),
                "",
                Utils.translate("&eLeft Click » &aIncrease height"),
                Utils.translate("&eRight Click » &aDecrease height")
        ).toItemStack());
        gui.setItem(12, new ItemBuilder(XMaterial.HEART_OF_THE_SEA.parseMaterial()).setName(Utils.translate("&aModify idle animation")).setLore(
                "",
                Utils.translate(" &7Animation: &6" + cubeletMachine.getBlockEffectModel().name()),
                Utils.translate(" &7Particle: &6" + cubeletMachine.getBlockEffectParticle().getParticle().name()),
                "",
                Utils.translate("&eLeft Click » &aChange animation"),
                Utils.translate("&eRight Click » &aChange particle")
        ).toItemStack());
        gui.setItem(14, new ItemBuilder(XMaterial.COMPASS.parseItem()).setName(Utils.translate("&aChange direction")).setLore(
                "",
                Utils.translate(" " + (cubeletMachine.getRotation() == Rotation.NORTH ? "&eNorth" : "&7North")),
                Utils.translate(" " + (cubeletMachine.getRotation() == Rotation.SOUTH ? "&eSouth" : "&7South")),
                Utils.translate(" " + (cubeletMachine.getRotation() == Rotation.EAST ? "&eEast" : "&7East")),
                Utils.translate(" " + (cubeletMachine.getRotation() == Rotation.WEST ? "&eWest" : "&7West")),
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

        CubeletMachine cubeletMachine = (CubeletMachine) getAttribute(AttrType.CUBELET_BOX_ATTR);

        if(slot == 10) {

            if(event.getClick() == ClickType.LEFT) {

                cubeletMachine.setBlockHeight(cubeletMachine.getBlockHeight() + 0.015);
                getMain().getHologramImplementation().moveHologram(cubeletMachine);

                playSound(SoundType.CLICK);

                reloadMyMenu();

            } else if(event.getClick() == ClickType.RIGHT) {

                cubeletMachine.setBlockHeight(cubeletMachine.getBlockHeight() - 0.015);
                getMain().getHologramImplementation().moveHologram(cubeletMachine);

                playSound(SoundType.CLICK);

                reloadMyMenu();

            } else if(event.getClick() == ClickType.MIDDLE) {

                cubeletMachine.setBlockHeight(cubeletMachine.getPermanentBlockHeight());

                playSound(SoundType.CHICKEN_EGG_POP);

                reloadMyMenu();

                getMain().getHologramImplementation().moveHologram(cubeletMachine);
                Bukkit.getScheduler().runTaskLater(getMain(), () -> getMain().getHologramImplementation().moveHologram(cubeletMachine), 5L);

            } else if(event.getClick() == ClickType.SHIFT_RIGHT || event.getClick() == ClickType.SHIFT_LEFT) {

                cubeletMachine.setPermanentBlockHeight(cubeletMachine.getBlockHeight());

                playSound(SoundType.CHICKEN_EGG_POP);

                reloadMyMenu();

            }

        } else if(slot == 12) {

            if(event.getClick() == ClickType.LEFT) {

                cubeletMachine.setBlockEffectModel(CollectionUtils.next(cubeletMachine.getBlockEffectModel()));
                playSound(SoundType.CLICK);
                reloadMyMenu();

            } else if(event.getClick() == ClickType.RIGHT) {

                Particle particle = CollectionUtils.next(cubeletMachine.getBlockEffectParticle().getParticle());
                SimpleParticle simpleParticle = SimpleParticle.of(particle).parseData("");

                cubeletMachine.setBlockEffectParticle(simpleParticle);
                playSound(SoundType.CLICK);
                reloadMyMenu();

            }

        } else if(slot == 14) {

            if(event.getClick() != ClickType.DOUBLE_CLICK) {

                if (cubeletMachine.getRotation() == Rotation.NORTH)
                    cubeletMachine.setRotation(Rotation.SOUTH);
                else if (cubeletMachine.getRotation() == Rotation.SOUTH)
                    cubeletMachine.setRotation(Rotation.EAST);
                else if (cubeletMachine.getRotation() == Rotation.EAST)
                    cubeletMachine.setRotation(Rotation.WEST);
                else if (cubeletMachine.getRotation() == Rotation.WEST)
                    cubeletMachine.setRotation(Rotation.NORTH);

                playSound(SoundType.CLICK);
                reloadMyMenu();

            }

        } else if(slot == 16) {

            if (getMain().getCubeletBoxHandler().getMachines().containsKey(cubeletMachine.getLocation())) {

                getMain().getCubeletBoxHandler().removeMachine(cubeletMachine.getLocation());

                playSound(SoundType.ANVIL_USE);

                player.sendMessage(Utils.translate(getMain().getLanguageHandler().getPrefix()
                        + " &aSuccesfully removed cubelet machine of" +
                        " &aX: &e" + cubeletMachine.getLocation().getBlockX() +
                        ", &aY: &e" + cubeletMachine.getLocation().getBlockY() +
                        ", &aZ: &e" + cubeletMachine.getLocation().getBlockZ()));

                String menuClassType = this.getClass().getName();

                for(Menu menu : getMain().getMenuHandler().getOpenedMenus().values()) {

                    if(!menu.getClass().getName().equalsIgnoreCase(menuClassType)) continue;

                    if(!menu.getAttribute(AttrType.CUBELET_BOX_ATTR).equals(cubeletMachine)) continue;

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

        getMain().getCubeletBoxHandler().saveMachines();

    }

}