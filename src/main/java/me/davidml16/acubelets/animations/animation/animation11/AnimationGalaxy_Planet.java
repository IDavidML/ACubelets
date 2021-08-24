package me.davidml16.acubelets.animations.animation.animation11;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AnimationGalaxy_Planet extends BukkitRunnable {

    private final ArmorStand armorStand;

    private final List<Location> locations;

    private int step;
    private double rotSpeed;

    public AnimationGalaxy_Planet(Main main, ItemStack planet, Location center, float radius, int points, boolean small, double rotSpeed, int step) {
        this.locations = LocationUtils.getCircle(center, radius, points);
        this.rotSpeed = rotSpeed;
        this.step = step;

        ArmorStand armorStand = center.getWorld().spawn(locations.get(step), ArmorStand.class);
        NBTEditor.set( armorStand, ( byte ) 1, "Silent" );
        if(XMaterial.supports(10)) armorStand.setSilent(true);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setHelmet(planet);
        armorStand.setSmall(small);
        armorStand.setMarker(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        armorStand.teleport(locations.get(step));

        main.getAnimationHandler().getEntities().add(armorStand);
        Sounds.playSound(armorStand.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, 3);

        this.armorStand = armorStand;
    }

    public ArmorStand getArmorStand() { return armorStand; }

    public int getStep() { return step; }
    public void setStep(int step) { this.step = step; }

    public void run() {

        armorStand.teleport(locations.get(step));
        armorStand.setHeadPose(armorStand.getHeadPose().add(0, rotSpeed, 0));

        if(step % 3 == 0) UtilParticles.display(Particles.FIREWORKS_SPARK, armorStand.getLocation().add(0, 1, 0), 1);

        this.step++;
        if(step >= locations.size()) step = 0;

    }
}
