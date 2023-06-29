package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.utils.LocationUtils;
import me.davidml16.acubelets.utils.MathL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachineRingsEffect extends MachineEffect {

    public MachineRingsEffect() {
        super(1L, 32, 0);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {

        double ring1 = Math.PI / (getDuration() / 2D) * this.step;
        double ring2 = Math.PI / (getDuration() / 2D) * ((((this.step + this.getDuration() / 2D) % this.getDuration())));

        location = location.add(0, 0.5, 0);

        particle.play(location.clone().add(MathL.cos(ring1), MathL.sin(ring1), MathL.sin(ring1)), 0.0, 1);
        particle.play(location.clone().add(MathL.cos(ring2), MathL.sin(ring2), MathL.sin(ring2)), 0.0, 1);
        particle.play(location.clone().add(MathL.cos(ring1 + Math.PI), MathL.sin(ring1), MathL.sin(ring1 + Math.PI)), 0.0, 1);
        particle.play(location.clone().add(MathL.cos(ring2 + Math.PI), MathL.sin(ring2), MathL.sin(ring2 + Math.PI)), 0.0, 1);

    }
}
