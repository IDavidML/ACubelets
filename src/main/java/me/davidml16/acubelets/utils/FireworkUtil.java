package me.davidml16.acubelets.utils;

import me.davidml16.acubelets.Main;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class FireworkUtil {

    private Main main;
    public FireworkUtil(Main main) {
        this.main = main;
    }

    public void spawn(Location loc, FireworkEffect.Type effect) {
        final Firework fw = (Firework)loc.getWorld().spawn(loc, Firework.class);
        fw.setMetadata("nodamage", new FixedMetadataValue(main, true));
        FireworkMeta data = fw.getFireworkMeta();
        data.addEffects(FireworkEffect.builder().withColor(Color.BLUE, Color.AQUA).with(effect).build());
        data.setPower(1);
        fw.setFireworkMeta(data);
        (new BukkitRunnable() {
            public void run() {
                fw.detonate();
            }
        }).runTaskLater(main, 1L);
    }

}
