package me.davidml16.acubelets.effects;

import me.davidml16.acubelets.utils.LocationUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public abstract class MachineEffect {

    protected int  step;
    protected long interval;
    protected int  duration;
    protected int  count;

    public MachineEffect(long interval, int duration) {
        this.step = 0;
        this.count = 0;
        this.interval = interval;
        this.duration = duration;
    }

    public void reset() {
        this.step = 0;
        this.count = 0;
    }

    public void addStep() {
        if (++this.step > this.getDuration()) {
            this.reset();
        }
    }

    public void step(@NotNull Location location, @NotNull SimpleParticle particle) {
        if (this.step < 0) return;

        this.doStep(LocationUtils.getCenter(location.clone(), false), particle, this.step);

        if (this.step >= this.getDuration()) {
            this.step = -10;
            this.count = 0;
        }
    }

    public abstract void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step);

    public final long getInterval() {
        return this.interval;
    }

    public final int getDuration() {
        return this.duration;
    }
}
