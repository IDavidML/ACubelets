package me.davidml16.acubelets.animations.animation.animation11;

import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationGalaxy_Spiral extends BukkitRunnable {

    private final ArmorStand armorStand;
    private int step = 16;

    private static final double yIncrement = 1;

    public AnimationGalaxy_Spiral(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }

    public void run() {

        if (step == 15) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.8, yIncrement, 0), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.8, yIncrement, 0), 1, 0);
        }
        if (step == 14) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.75, yIncrement, .43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.75, yIncrement, -.43), 1, 0);
        }
        if (step == 13) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.65, yIncrement, .65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.65, yIncrement, -.65), 1, 0);
        }
        if (step == 12) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.43, yIncrement, .75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.43, yIncrement, -.75), 1, 0);
        }
        if (step == 11) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(0, yIncrement, .8), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(0, yIncrement, -.8), 1, 0);
        }
        if (step == 10) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.43, yIncrement, .75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.43, yIncrement, -.75), 1, 0);
        }
        if (step == 9) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.65, yIncrement, .65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.65, yIncrement, -.65), 1, 0);
        }
        if (step == 8) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.75, yIncrement, .43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.75, yIncrement, -.43), 1, 0);
        }
        if (step == 7) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.8, yIncrement, 0), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.8, yIncrement, 0), 1, 0);
        }
        if (step == 6) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.75, yIncrement, -.43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.75, yIncrement, .43), 1, 0);
        }
        if (step == 5) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.65, yIncrement, -.65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.65, yIncrement, .65), 1, 0);
        }
        if (step == 4) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.43, yIncrement, -.75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.43, yIncrement, .75), 1, 0);
        }
        if (step == 3) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(0, yIncrement, -.8), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(0, yIncrement, .8), 1, 0);
        }
        if (step == 2) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.43, yIncrement, -.75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.43, yIncrement, .75), 1, 0);
        }
        if (step == 1) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.65, yIncrement, -.65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.65, yIncrement, .65), 1, 0);
        }
        if (step == 0) {
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(.75, yIncrement, -.43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, armorStand.getLocation().add(-.75, yIncrement, .43), 1, 0);
            step = 16;
        }

        this.step--;

    }
}
