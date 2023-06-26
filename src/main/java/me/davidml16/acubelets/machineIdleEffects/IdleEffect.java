package me.davidml16.acubelets.machineIdleEffects;

import me.davidml16.acubelets.Main;
import me.davidml16.acubelets.machineIdleEffects.effects.NoneIdleEffect;
import me.davidml16.acubelets.objects.CubeletMachine;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class IdleEffect {

    private Main main;
    private CubeletMachine cubeletMachine;
    private IdleEffectType idleEffectType;
    private IdleEffectAnimation idleEffectAnimation;
    private Particle particle;

    public static List<BukkitTask> tasks = new ArrayList<>();

    public IdleEffect(Main main, CubeletMachine cubeletMachine, IdleEffectType idleEffectType, Particle particle) {
        this.main = main;
        this.cubeletMachine = cubeletMachine;
        this.idleEffectType = idleEffectType;
        this.particle = particle;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public CubeletMachine getCubeletMachine() {
        return cubeletMachine;
    }

    public void setCubeletMachine(CubeletMachine cubeletMachine) {
        this.cubeletMachine = cubeletMachine;
    }

    public IdleEffectType getIdleEffectType() {
        return idleEffectType;
    }

    public void setIdleEffectType(IdleEffectType idleEffectType) {
        this.idleEffectType = idleEffectType;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public IdleEffectAnimation getIdleEffectAnimation() {
        return idleEffectAnimation;
    }

    public void setIdleEffectAnimation(IdleEffectAnimation idleEffectAnimation) {
        this.idleEffectAnimation = idleEffectAnimation;
    }

    public void init() {
        IdleEffectAnimation idleEffectAnimation;

        switch (idleEffectType) {
            default:
                idleEffectAnimation = new NoneIdleEffect(this);
                break;
        }

        idleEffectAnimation.start();
        setIdleEffectAnimation(idleEffectAnimation);
    }

    public void stop() {
        if(idleEffectAnimation.getTask() != null) {
            idleEffectAnimation.getTask().cancel();
            idleEffectAnimation.setTask(null);
            setIdleEffectType(IdleEffectType.NONE);
            init();
        }
    }

}
