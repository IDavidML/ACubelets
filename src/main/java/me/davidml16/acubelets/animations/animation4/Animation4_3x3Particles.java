package me.davidml16.acubelets.animations.animation4;

import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation4_3x3Particles extends BukkitRunnable {

    private final Location location;


    public Animation4_3x3Particles(Location location) {
        this.location = location.clone();
    }

    public void run() {
        UtilParticles.display(Particles.FLAME, 1D, 1D, 1D, this.location, 25);
    }

}
