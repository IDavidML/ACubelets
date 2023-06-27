package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.SimpleParticle;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachineSimpleEffect extends MachineEffect {

    public MachineSimpleEffect() {
        super(2L, 2);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {
        particle.play(location.add(0, 0.5D, 0), 0.3f, 0.1f, 30);
    }
}
