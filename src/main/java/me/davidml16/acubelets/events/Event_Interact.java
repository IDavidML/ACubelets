package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.menus.player.CubeletsMenu;
import me.davidml16.acubelets.menus.player.rewards.RewardsPreviewMenu;
import me.davidml16.acubelets.objects.*;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.utils.VersionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class Event_Interact implements Listener {

    private Main main;
    public Event_Interact(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        Player player = e.getPlayer();
        Action action = e.getAction();

        ItemStack item = e.getItem();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {

            if(item != null) {

                if (NBTEditor.contains(item, "keyType"))
                    e.setCancelled(true);

            }

        }

        if(item == null || !NBTEditor.contains(item, "keyType") || !main.isKeysEnabled()) {

            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {

                if (main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {

                    e.setCancelled(true);

                    if(!VersionUtil.isOneEight()) {
                        if (e.getHand() != EquipmentSlot.HAND) return;
                    }

                    CubeletMachine box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());

                    if (box.isWaiting()) {
                        main.getPlayerDataHandler().getData(player).setBoxOpened(box);

                        if (!main.isNoGuiMode()) {

                            new CubeletsMenu(main, player).open();

                        } else {

                            Profile profile = main.getPlayerDataHandler().getData(player.getUniqueId());
                            List<Cubelet> cubelets = profile.getCubelets();
                            cubelets.sort(new CubeletDateComparator());

                            Optional<Cubelet> optionalCubelet = cubelets.stream().findFirst();

                            if (optionalCubelet.isPresent()) {

                                Cubelet cubelet = optionalCubelet.get();

                                if (cubelet.getExpire() > System.currentTimeMillis()) {

                                    CubeletType type = main.getCubeletTypesHandler().getTypeBydId(cubelet.getType());

                                    if (type.getAllRewards().size() > 0) {

                                        main.getCubeletOpenHandler().openAnimation(player, profile.getBoxOpened(), type, false);

                                        main.getDatabaseHandler().removeCubelet(player.getUniqueId(), cubelet.getUuid());

                                        profile.getCubelets().remove(cubelet);
                                        main.getHologramImplementation().reloadHolograms(player);

                                    }

                                }

                            }

                        }

                    } else {

                        if (box.getPlayerOpening().getUuid() == player.getUniqueId())
                            player.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                        else
                            player.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", box.getPlayerOpening().getName()));

                    }

                }

            }

        } else {

            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {

                if (main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {

                    e.setCancelled(true);

                    if(!VersionUtil.isOneEight()) {
                        if (e.getHand() != EquipmentSlot.HAND) return;
                    }

                    String typeID = NBTEditor.getString(item, "keyType");

                    if(action == Action.LEFT_CLICK_BLOCK) {

                        if(main.isPreviewEnabled()) {

                            RewardsPreviewMenu rewardsPreviewMenu = new RewardsPreviewMenu(Main.get(), player);
                            rewardsPreviewMenu.setAttribute(Menu.AttrType.CUSTOM_ID_ATTR, typeID);
                            rewardsPreviewMenu.setAttribute(Menu.AttrType.OPENED_EXTERNALLY_ATTR, new Boolean(true));
                            rewardsPreviewMenu.open();

                        }

                    } else {

                        CubeletMachine box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());

                        if (box.isWaiting()) {

                            CubeletType type = main.getCubeletTypesHandler().getTypeBydId(typeID);

                            if (type.getAllRewards().size() > 0) {

                                main.getCubeletOpenHandler().openAnimation(player, box, type, true);

                                if (item.getAmount() > 1) {
                                    item.setAmount(item.getAmount() - 1);
                                } else {
                                    player.getInventory().remove(item);
                                }

                                Bukkit.getScheduler().runTaskLater(main, () -> player.updateInventory(), 20L);

                                main.getHologramImplementation().reloadHolograms(player);

                            }

                        } else {

                            if (box.getPlayerOpening().getUuid() == player.getUniqueId())
                                player.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                            else
                                player.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", box.getPlayerOpening().getName()));

                        }

                    }

                }

            }

        }

    }

    @EventHandler
    public void onArmorStand(PlayerArmorStandManipulateEvent e) {

        ArmorStand armorStand = e.getRightClicked();

        if(!armorStand.hasMetadata("ACUBELETS")) return;

        e.setCancelled(true);

    }

}
