package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Event_Interact implements Listener {

    private Main main;
    public Event_Interact(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();

        ItemStack item = e.getItem();

        if(action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR ||
                action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            if(item != null) {
                if (NBTEditor.contains(item, "keyType")) {
                    e.setCancelled(true);
                }
            }
        }

        if(item == null || !NBTEditor.contains(item, "keyType") || !main.isKeysEnabled()) {
            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
                if (main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {
                    e.setCancelled(true);

                    if (!Bukkit.getVersion().contains("1.8")) {
                        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
                    }

                    CubeletBox box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());

                    if (box.isWaiting()) {
                        main.getPlayerDataHandler().getData(p).setBoxOpened(box);

                        if (!main.isNoGuiMode()) {

                            main.getCubeletsGUI().open(p);

                        } else {

                            Profile profile = main.getPlayerDataHandler().getData(p.getUniqueId());
                            List<Cubelet> cubelets = profile.getCubelets();
                            cubelets.sort(new CubeletDateComparator());

                            Optional<Cubelet> optionalCubelet = cubelets.stream().findFirst();

                            if (optionalCubelet.isPresent()) {

                                Cubelet cubelet = optionalCubelet.get();

                                if (cubelet.getExpire() > System.currentTimeMillis()) {

                                    CubeletType type = main.getCubeletTypesHandler().getTypeBydId(cubelet.getType());

                                    if (type.getAllRewards().size() > 0) {

                                        main.getCubeletOpenHandler().openAnimation(p, profile.getBoxOpened(), type, false);

                                        main.getDatabaseHandler().removeCubelet(p.getUniqueId(), cubelet.getUuid());

                                        profile.getCubelets().remove(cubelet);
                                        main.getHologramImplementation().reloadHolograms(p);

                                    }

                                }

                            }

                        }

                    } else {
                        if (box.getPlayerOpening().getUuid() == p.getUniqueId()) {
                            p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                        } else {
                            p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", box.getPlayerOpening().getName()));
                        }
                    }

                }
            }
        } else {

            if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
                if (main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {

                    e.setCancelled(true);

                    String typeID = NBTEditor.getString(item, "keyType");

                    if(action == Action.LEFT_CLICK_BLOCK) {

                        if(main.isPreviewEnabled()) main.getRewardsPreviewGUI().open(p, typeID, true);

                    } else {

                        CubeletBox box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());

                        if (box.isWaiting()) {

                            CubeletType type = main.getCubeletTypesHandler().getTypeBydId(typeID);

                            if (type.getAllRewards().size() > 0) {

                                main.getCubeletOpenHandler().openAnimation(p, box, type, true);

                                if (item.getAmount() > 0) {
                                    item.setAmount(item.getAmount() - 1);
                                }

                                main.getHologramImplementation().reloadHolograms(p);

                            }

                        } else {

                            if (box.getPlayerOpening().getUuid() == p.getUniqueId()) {
                                p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                            } else {
                                p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", box.getPlayerOpening().getName()));
                            }

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
