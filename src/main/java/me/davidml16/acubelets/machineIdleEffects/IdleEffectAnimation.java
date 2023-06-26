package me.davidml16.acubelets.machineIdleEffects;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class IdleEffectAnimation {

    private IdleEffect idleEffect;
    private BukkitTask task;

    public IdleEffectAnimation(IdleEffect idleEffect) {
        this.idleEffect = idleEffect;
    }

    public abstract void start();

    public void stop() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    public IdleEffect getIdleEffect() {
        return idleEffect;
    }

    public void setIdleEffect(IdleEffect idleEffect) {
        this.idleEffect = idleEffect;
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }

}
