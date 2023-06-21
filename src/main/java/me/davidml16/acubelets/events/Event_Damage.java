package me.davidml16.acubelets.events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
