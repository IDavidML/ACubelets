package me.davidml16.acubelets.animations.animation.animation17;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.SkullCreator;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Animation17_Snowball extends BukkitRunnable {

    private final ArmorStand armorStand;

    private final List<Location> locations;

    private int step;

    private double yDif;

    public Animation17_Snowball(Main main, Location center, float radius, int points, boolean small, int step) {
        this.locations = LocationUtils.getCircle(center, radius, points);
        this.step = step;
        this.yDif = 0.0;

        ArmorStand armorStand = center.getWorld().spawn(locations.get(step), ArmorStand.class);
        NBTEditor.set( armorStand, ( byte ) 1, "Silent" );
        armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRmZDc3MjRjNjlhMDI0ZGNmYzYwYjE2ZTAwMzM0YWI1NzM4ZjRhOTJiYWZiOGZiYzc2Y2YxNTMyMmVhMDI5MyJ9fX0="));
        armorStand.setGravity(false);
        armorStand.setSmall(small);
        armorStand.setMarker(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        armorStand.teleport(locations.get(step));

        main.getAnimationHandler().getEntities().add(armorStand);

        this.armorStand = armorStand;
    }

    public ArmorStand getArmorStand() { return armorStand; }

    public int getStep() { return step; }
    public void setStep(int step) { this.step = step; }

    public void run() {

        Location loc = locations.get(step);
        Location loc2 = loc.clone().add(0, yDif, 0);

        if(step + 1 >= locations.size())
            loc2.setDirection(locations.get(0).clone().add(0, yDif, 0).toVector().subtract(loc2.toVector()));
        else
            loc2.setDirection(locations.get(step + 1).clone().add(0, yDif, 0).toVector().subtract(loc2.toVector()));

        armorStand.teleport(loc2);

        UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().add(armorStand.getLocation().getDirection()
                .normalize().multiply(-1).multiply(1)).add(0, 1, 0), 3);

        this.step++;
        if(step >= locations.size()) step = 0;

        if(yDif < 4)
            yDif += 0.095;

    }
}
