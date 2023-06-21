package me.davidml16.acubelets.animations.animation.animation13;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Animation13_Gwen extends BukkitRunnable {

    private final Guardian entity;

    private final List<Location> locations;

    private final Entity target;

    private int step;

    public Animation13_Gwen(Main main, Entity target, Location center, float radius, int points, int step) {
        this.locations = LocationUtils.getCircle(center, radius, points);
        this.step = step;
        this.target = target;

        Location location = locations.get(step);
        location.setDirection(target.getLocation().clone().toVector().subtract(location.toVector()));

        Guardian guardian = (Guardian)target.getWorld().spawnEntity(location, EntityType.GUARDIAN);
        guardian.setTarget(null);
        guardian.setTarget((LivingEntity) target);
        NBTEditor.set( guardian, ( byte ) 1, "Silent" );
        NBTEditor.set( guardian, ( byte ) 1, "NoGravity" );
        guardian.setGravity(false);
        guardian.setVelocity(new Vector(0, 0, 0));
        guardian.setCustomNameVisible(false);
        guardian.setRemoveWhenFarAway(false);
        guardian.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        guardian.teleport(location);
        main.getAnimationHandler().getEntities().add(guardian);

        Sounds.playSound(guardian.getLocation(), Sounds.MySound.ORB_PICKUP, 0.5F, 3);

        this.entity = guardian;
    }

    public LivingEntity getEntity() { return entity; }

    public int getStep() { return step; }
    public void setStep(int step) { this.step = step; }

    public void run() {

        Location location = locations.get(step);

        entity.setTarget(null);
        entity.setTarget((LivingEntity) target);
        entity.setVelocity(new Vector(0, 0, 0));
        location.setDirection(target.getLocation().clone().toVector().subtract(location.toVector()));
        entity.teleport(locations.get(step));

        if(step % 3 == 0) UtilParticles.display(Particles.DRIP_WATER, entity.getLocation().add(0, 1, 0), 10);

        this.step++;
        if(step >= locations.size()) step = 0;

    }
}
