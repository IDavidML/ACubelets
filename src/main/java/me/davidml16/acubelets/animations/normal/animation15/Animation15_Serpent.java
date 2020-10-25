package me.davidml16.acubelets.animations.normal.animation15;

import me.davidml16.acubelets.utils.MathUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import me.davidml16.acubelets.utils.Sounds;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Animation15_Serpent extends BukkitRunnable {

    private double t;

    private Location center;

    private boolean flipped;

    public Animation15_Serpent(Location center, boolean flipped) {
        this.center = center;
        this.t = 15.0;
        this.flipped = flipped;
        if(!flipped)
            Sounds.playSound(center, Sounds.MySound.ENDERDRAGON_GROWL, 0.25F, 0.0F);
    }

    public void run() {

        if(!flipped)
            t = t + Math.PI / 200;
        else
            t = t - Math.PI / 200;

        final Location loc = center.clone();

        double x; double y; double z;
        double x1; double y1; double z1;
        double x2; double y2; double z2;
        double r = 0.35;

        x2 = MathUtils.sin(2 * t);
        y2 = 2 * MathUtils.cos(t);
        z2 = MathUtils.sin(3 * t);

        if(!flipped)
            t -= Math.PI / 200;
        else
            t += Math.PI / 200;

        x1 = MathUtils.sin(2 * t);
        y1 = 2 * MathUtils.cos(t);
        z1 = MathUtils.sin(3 * t);

        if(!flipped)
            t += Math.PI / 200;
        else
            t -= Math.PI / 200;

        Vector dir = new Vector(x2 - x1, y2 - y1, z2 - z1);

        Location loc2 = new Location(center.getWorld(), 0, 0, 0).setDirection(dir.normalize());

        loc2.setDirection(dir.normalize());

        if(t % 1 < 0.1)
            if(!flipped)
                Sounds.playSound(center, Sounds.MySound.ENDERDRAGON_WINGS, 0.25F, 1.0F);

        for(double i = 0; i <= 2 * Math.PI; i = i + Math.PI/8) {
            x = 0.2 * t;
            y = r * MathUtils.sin(i) + 2 * MathUtils.sin(10 * t) + 2.8;
            z = r * MathUtils.cos(i);
            Vector v = new Vector(x,y,z);
            v = MathUtils.rotateFunction(v, loc2);
            loc.add(v.getX(), v.getY(), v.getZ());
            UtilParticles.display(Particles.FLAME, loc, 1, 0);
            if(i == 0)
                UtilParticles.display(Particles.LAVA, loc, 1, 0);
            loc.subtract(v.getX(), v.getY(), v.getZ());
        }

    }
}
