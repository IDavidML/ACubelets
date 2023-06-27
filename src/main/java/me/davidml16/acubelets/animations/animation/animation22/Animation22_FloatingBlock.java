package me.davidml16.acubelets.animations.animation.animation22;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import fr.skytasul.guardianbeam.Laser;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.ParticlesAPI.Particles;
import me.davidml16.acubelets.utils.ParticlesAPI.UtilParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.List;

public class Animation22_FloatingBlock extends BukkitRunnable {

    private Location center;
    private Location spawnLocation;

    private FallingBlock fallingBlock;
    private Location corner;

    private int stepGlobal;
    private int stepSpiral;

    private static final double yIncrement = 0.5;

    public Animation22_FloatingBlock(Main main, Location center, Location spawnLocation, Location corner) {
        this.center = center;
        this.spawnLocation = spawnLocation;
        this.stepGlobal = 0;
        this.stepSpiral = 0;
        this.corner = corner;

        FallingBlock fallingBlock = center.getWorld().spawnFallingBlock(spawnLocation, Bukkit.createBlockData(XMaterial.OBSIDIAN.parseMaterial()));
        fallingBlock.setMetadata("ACUBELETS", new FixedMetadataValue(main, Boolean.TRUE));
        fallingBlock.setGravity(false);
        fallingBlock.setDropItem(false);

        this.fallingBlock = fallingBlock;

        main.getAnimationHandler().getEntities().add(fallingBlock);
    }

    public FallingBlock getFallingBlock() {
        return fallingBlock;
    }

    public void run() {

        if (stepSpiral == 15) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.8, yIncrement, 0), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.8, yIncrement, 0), 1, 0);
        }
        if (stepSpiral == 14) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.75, yIncrement, .43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.75, yIncrement, -.43), 1, 0);
        }
        if (stepSpiral == 13) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.65, yIncrement, .65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.65, yIncrement, -.65), 1, 0);
        }
        if (stepSpiral == 12) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.43, yIncrement, .75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.43, yIncrement, -.75), 1, 0);
        }
        if (stepSpiral == 11) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(0, yIncrement, .8), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(0, yIncrement, -.8), 1, 0);
        }
        if (stepSpiral == 10) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.43, yIncrement, .75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.43, yIncrement, -.75), 1, 0);
        }
        if (stepSpiral == 9) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.65, yIncrement, .65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.65, yIncrement, -.65), 1, 0);
        }
        if (stepSpiral == 8) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.75, yIncrement, .43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.75, yIncrement, -.43), 1, 0);
        }
        if (stepSpiral == 7) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.8, yIncrement, 0), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.8, yIncrement, 0), 1, 0);
        }
        if (stepSpiral == 6) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.75, yIncrement, -.43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.75, yIncrement, .43), 1, 0);
        }
        if (stepSpiral == 5) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.65, yIncrement, -.65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.65, yIncrement, .65), 1, 0);
        }
        if (stepSpiral == 4) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.43, yIncrement, -.75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.43, yIncrement, .75), 1, 0);
        }
        if (stepSpiral == 3) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(0, yIncrement, -.8), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(0, yIncrement, .8), 1, 0);
        }
        if (stepSpiral == 2) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.43, yIncrement, -.75), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.43, yIncrement, .75), 1, 0);
        }
        if (stepSpiral == 1) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.65, yIncrement, -.65), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.65, yIncrement, .65), 1, 0);
        }
        if (stepSpiral == 0) {
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(.75, yIncrement, -.43), 1, 0);
            UtilParticles.display(Particles.SPELL_WITCH, fallingBlock.getLocation().add(-.75, yIncrement, .43), 1, 0);
            stepSpiral = 16;
        }

        if(fallingBlock != null) {
            if(stepGlobal % 2 == 0)
                UtilParticles.drawParticleLine(fallingBlock.getLocation().clone().add(0.25, 0.5, 0.25), corner, Particles.REDSTONE, 10, Color.MAGENTA);
        }

        this.stepSpiral--;

        stepGlobal++;

        if(stepGlobal >= 25) return;

        Vector vector = new Vector(0.0D, 0.75D, 0.0D);
        if(fallingBlock != null)
            fallingBlock.setVelocity(vector.multiply(0.05D));

    }
}
