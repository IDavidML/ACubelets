package me.davidml16.acubelets.animations.animation.animation21;

import com.cryptomorin.xseries.XItemStack;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.List;

public class Animation21_RotatingParticle extends BukkitRunnable {

    private final ArmorStand armorStand;

    private List<Location> locations;

    private int step;

    private double yDif;

    private boolean isWhite;

    private Location center;
    private double radius;
    private int points;

    public Animation21_RotatingParticle(Main main, Location center, double radius, int points, boolean small, int step, boolean isWhite) {
        this.locations = LocationUtils.getCircle(center, radius, points);
        this.step = step;
        this.yDif = 0.0;
        this.isWhite = isWhite;

        this.center = center;
        this.radius = radius;
        this.points = points;

        ArmorStand armorStand = center.getWorld().spawn(locations.get(step), ArmorStand.class);
        NBTEditor.set( armorStand, ( byte ) 1, "Silent" );
        if(isWhite)
            armorStand.setHelmet(XMaterial.WHITE_CONCRETE.parseItem());
        else
            armorStand.setHelmet(XMaterial.BLACK_CONCRETE.parseItem());
        armorStand.setSilent(true);
        armorStand.setVisible(false);
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

        if(isWhite)
            ParticleDisplay.colored(armorStand.getLocation().add(armorStand.getLocation().getDirection().normalize().multiply(-1).multiply(1)).add(0, 1, 0), Color.WHITE, 3).spawn();
        else
            ParticleDisplay.colored(armorStand.getLocation().add(armorStand.getLocation().getDirection().normalize().multiply(-1).multiply(1)).add(0, 1, 0), Color.BLACK, 3).spawn();

        this.step++;
        if(step >= locations.size()) step = 0;

        if(yDif < 4)
            yDif += 0.095;

        if(radius < 2.25) {
            radius += 0.05;
            locations = LocationUtils.getCircle(center, radius, points);
        }

    }
}
