package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.data.CubeletBox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
            if(e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME && main.getCubeletBoxHandler().getBoxes().containsKey(e.getClickedBlock().getLocation())) {
                e.setCancelled(true);

                if(!Bukkit.getVersion().contains("1.8")) {
                    if(e.getHand() == EquipmentSlot.OFF_HAND) return;
                }

                CubeletBox box = main.getCubeletBoxHandler().getBoxByLocation(e.getClickedBlock().getLocation());
                if(!box.isUsing()) {
                    main.getPlayerDataHandler().getData(p).setBoxOpened(box);
                    main.getCubeletsGUI().open(p);
                } else {
                    if(box.getPlayerOpening().getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Me"));
                    } else {
                        p.sendMessage(main.getLanguageHandler().getMessage("Cubelet.BoxInUse.Other")
                                .replaceAll("%player%", box.getPlayerOpening().getName()));
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
