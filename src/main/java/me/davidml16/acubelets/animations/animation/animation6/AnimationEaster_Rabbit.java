package me.davidml16.acubelets.animations.animation.animation6;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.NBTEditor;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class AnimationEaster_Rabbit extends BukkitRunnable {

    private final Location spawnLoc;
    private final float yaw;
    private LivingEntity rabbit;

    private int tick;

    public AnimationEaster_Rabbit(Main main, Location spawnLoc, float yaw) {
        this.spawnLoc = spawnLoc;
        this.yaw = yaw;
        this.tick = 0;

        this.spawnLoc.setYaw(this.yaw);

        rabbit = (LivingEntity) spawnLoc.getWorld().spawnEntity(this.spawnLoc, EntityType.RABBIT);

        ((Rabbit) rabbit).setRabbitType(Rabbit.Type.values()[new Random().nextInt(Rabbit.Type.values().length - 1)]);

        if(XMaterial.supports(9)) rabbit.setCollidable(false);
        rabbit.setRemoveWhenFarAway(false);
        rabbit.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        rabbit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 10, true, false));

        NBTEditor.set( rabbit, ( byte ) 1, "Silent" );
        NBTEditor.set( rabbit, ( byte ) 1, "Invulnerable" );

        rabbit.teleport(this.spawnLoc);

        UtilParticles.display(Particles.FIREWORKS_SPARK, 0.35, 0.1, 0.35, spawnLoc, 10);

        main.getAnimationHandler().getEntities().add(rabbit);

        Sounds.playSound(spawnLoc, Sounds.MySound.LAVA_POP, 1F, 1);
    }

    public Entity getEntity() { return rabbit; }

    public void run() {
        if (tick == 0) {
            rabbit.setVelocity(new Vector(0, 0.35, 0));
            tick = 15;
        } else
            tick--;

        Location loc = rabbit.getLocation();
        loc.setX(spawnLoc.getX());
        loc.setZ(spawnLoc.getZ());
        loc.setYaw(yaw);
        rabbit.teleport(loc);
    }
}
