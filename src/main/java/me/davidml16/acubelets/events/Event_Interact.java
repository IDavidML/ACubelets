package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.interfaces.CubeletDateComparator;
import me.davidml16.acubelets.objects.Cubelet;
import me.davidml16.acubelets.objects.CubeletBox;
import me.davidml16.acubelets.enums.CubeletBoxState;
import me.davidml16.acubelets.objects.CubeletType;
import me.davidml16.acubelets.objects.Profile;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Event_Interact implements Listener {

    private Main main;
    public Event_Interact(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action action = e.getAction();

        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK) {
            if(main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {
                e.setCancelled(true);

                if(!Bukkit.getVersion().contains("1.8")) {
                    if(e.getHand() == EquipmentSlot.OFF_HAND) return;
                }

                CubeletBox box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());

                if(box.getState() == CubeletBoxState.EMPTY) {
                    main.getPlayerDataHandler().getData(p).setBoxOpened(box);

                    if(!main.isNoGuiMode()) {

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

                                    main.getCubeletOpenHandler().openAnimation(p, profile.getBoxOpened(), type);

                                    try {
                                        main.getDatabaseHandler().removeCubelet(p.getUniqueId(), cubelet.getUuid());
                                    } catch (SQLException throwables) {
                                        throwables.printStackTrace();
                                    }

                                    profile.getCubelets().remove(cubelet);
                                    main.getHologramHandler().reloadHolograms(p);

                                }

                            }

                        }

                    }

                } else {
                    if(box.getPlayerOpening().getUuid() == p.getUniqueId()) {
                        p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                    } else {
                        p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other").replaceAll("%player%", box.getPlayerOpening().getName()));
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
