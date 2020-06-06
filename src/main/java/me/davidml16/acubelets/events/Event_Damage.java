package me.davidml16.acubelets.events;

import me.davidml16.acubelets.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event_Damage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework) {
            Firework fw = (Firework) e.getDamager();
            if (fw.hasMetadata("nodamage")) {
                e.setCancelled(true);
            }
        } else {
            if (!e.getEntity().hasMetadata("ACUBELETS")) return;
            e.setCancelled(true);
        }
    }

}
