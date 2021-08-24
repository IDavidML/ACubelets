package me.davidml16.acubelets.animations.animation.animation4;

import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation4_Arch extends BukkitRunnable {

    private final Location location;

    private final double width = 4.25;
    private final double squareRoot2 = Math.sqrt(2D);
    private final double radius;

    private int step;

    public Animation4_Arch(Location location) {
        this.location = location.clone().add(0.5, 0.25, 0.5);
        this.radius = width / 2D;
        this.step = -90;
    }

    public void run() {
        if(step < 90)step+=4;
        else step = 90;

        spawnArch(location.clone(), false);
        spawnArch(location.clone(), true);
    }

    public void spawnArch(Location loc, boolean inverted) {
        for (int deg = -90; deg <= step; ++deg) {
            double rad = Math.toRadians(deg);
            loc.setX(location.getX() + radius * Math.sin(rad));
            loc.setY(location.getY() + radius * Math.cos(rad) * squareRoot2);
            if(!inverted) loc.setZ(location.getZ() + radius * Math.sin(rad));
            else loc.setZ(location.getZ() - radius * Math.sin(rad));

            if(deg % 4 == 0) UtilParticles.display(Particles.REDSTONE, 255, 0, 0, loc);
        }
    }

}
