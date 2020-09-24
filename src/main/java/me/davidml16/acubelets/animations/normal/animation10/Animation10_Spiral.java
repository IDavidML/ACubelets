package me.davidml16.acubelets.animations.normal.animation10;

import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class Animation10_Spiral extends BukkitRunnable {

    private final ArmorStand armorStand;
    private int step = 16;

    private static final double yIncrement = 1;

    public Animation10_Spiral(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public void run() {

        if (step == 15) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.8, yIncrement, 0), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.8, yIncrement, 0), 1, 0, 0, 0, 0);
        }
        if (step == 14) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.75, yIncrement, .43), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.75, yIncrement, -.43), 1, 0, 0, 0, 0);
        }
        if (step == 13) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.65, yIncrement, .65), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.65, yIncrement, -.65), 1, 0, 0, 0, 0);
        }
        if (step == 12) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.43, yIncrement, .75), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.43, yIncrement, -.75), 1, 0, 0, 0, 0);
        }
        if (step == 11) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(0, yIncrement, .8), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(0, yIncrement, -.8), 1, 0, 0, 0, 0);
        }
        if (step == 10) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.43, yIncrement, .75), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.43, yIncrement, -.75), 1, 0, 0, 0, 0);
        }
        if (step == 9) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.65, yIncrement, .65), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.65, yIncrement, -.65), 1, 0, 0, 0, 0);
        }
        if (step == 8) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.75, yIncrement, .43), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.75, yIncrement, -.43), 1, 0, 0, 0, 0);
        }
        if (step == 7) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.8, yIncrement, 0), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.8, yIncrement, 0), 1, 0, 0, 0, 0);
        }
        if (step == 6) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.75, yIncrement, -.43), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.75, yIncrement, .43), 1, 0, 0, 0, 0);
        }
        if (step == 5) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.65, yIncrement, -.65), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.65, yIncrement, .65), 1, 0, 0, 0, 0);
        }
        if (step == 4) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.43, yIncrement, -.75), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.43, yIncrement, .75), 1, 0, 0, 0, 0);
        }
        if (step == 3) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(0, yIncrement, -.8), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(0, yIncrement, .8), 1, 0, 0, 0, 0);
        }
        if (step == 2) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.43, yIncrement, -.75), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.43, yIncrement, .75), 1, 0, 0, 0, 0);
        }
        if (step == 1) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.65, yIncrement, -.65), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.65, yIncrement, .65), 1, 0, 0, 0, 0);
        }
        if (step == 0) {
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(.75, yIncrement, -.43), 1, 0, 0, 0, 0);
            armorStand.getWorld().spawnParticle(Particle.FLAME, armorStand.getLocation().add(-.75, yIncrement, .43), 1, 0, 0, 0, 0);
            step = 16;
        }

        this.step--;

    }
}
