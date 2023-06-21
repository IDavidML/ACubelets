package me.davidml16.acubelets.animations.animation.animation15;

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

public class Animation15_RotatingFlame extends BukkitRunnable {

    private final ArmorStand armorStand;

    private final List<Location> locations;

    private int step;

    public Animation15_RotatingFlame(Main main, Location center, float radius, int points, boolean small, int step) {
        this.locations = LocationUtils.getCircle(center, radius, points);
        this.step = step;

        ArmorStand armorStand = center.getWorld().spawn(locations.get(step), ArmorStand.class);
        NBTEditor.set( armorStand, ( byte ) 1, "Silent" );
        armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI2YmRlNDUwNDljN2I3ZDM0NjA1ZDgwNmEwNjgyOWI2Zjk1NWI4NTZhNTk5MWZkMzNlN2VhYmNlNDRjMDgzNCJ9fX0="));
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
        if(step + 1 >= locations.size())
            loc.setDirection(locations.get(0).clone().toVector().subtract(loc.toVector()));
        else
            loc.setDirection(locations.get(step + 1).clone().toVector().subtract(loc.toVector()));

        UtilParticles.display(Particles.FLAME, armorStand.getLocation().add(0, 1, 0), 1);

        armorStand.teleport(loc);

        this.step++;
        if(step >= locations.size()) step = 0;

    }
}
