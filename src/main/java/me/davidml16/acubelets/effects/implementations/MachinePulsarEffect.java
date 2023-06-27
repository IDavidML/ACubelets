package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.utils.LocationUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachinePulsarEffect extends MachineEffect {

    public MachinePulsarEffect() {
        super(2L, 38);
    }

    @Override
    public void doStep(@NotNull Location loc2, @NotNull SimpleParticle particle, int step) {
        Location loc = loc2.clone().add(0, -0.8D, 0);
        double n2 = (0.5 + step * 0.15) % 3.0;
        for (int n3 = 0; n3 < n2 * 10.0; ++n3) {
            double n4 = 6.283185307179586 / (n2 * 10.0) * n3;
            particle.play(LocationUtils.getPointOnCircle(loc.clone(), false, n4, n2, 1.0), 0.1f, 0.0f, 2);
        }
    }
}
