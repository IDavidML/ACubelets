package me.davidml16.acubelets.utils;

import me.davidml16.acubelets.Main;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireworkUtil {

    private Main main;
    public FireworkUtil(Main main) {
        this.main = main;
    }

    public void spawn(Location loc, FireworkEffect.Type effect, Color... colors) {
        final Firework fw = (Firework)loc.getWorld().spawn(loc, Firework.class);
        fw.setMetadata("nodamage", new FixedMetadataValue(main, true));
        FireworkMeta data = fw.getFireworkMeta();
        data.addEffects(FireworkEffect.builder().withColor(colors).with(effect).build());
        data.setPower(1);
        fw.setFireworkMeta(data);
        (new BukkitRunnable() {
            public void run() {
                fw.detonate();
            }
        }).runTaskLater(main, 1L);
    }

    public List<Color> getRandomColors() {
        int random = new Random().nextInt(4) + 1;
        List<Color> colors = new ArrayList<>();
        switch (random) {
            case 1:
                colors.add(Color.BLUE);
                colors.add(Color.AQUA);
                break;
            case 2:
                colors.add(Color.RED);
                colors.add(Color.ORANGE);
                break;
            case 3:
                colors.add(Color.GREEN);
                colors.add(Color.LIME);
                break;
            case 4:
                colors.add(Color.PURPLE);
                colors.add(Color.FUCHSIA);
                break;
        }
        return colors;
    }

}
