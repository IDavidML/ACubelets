package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.utils.LocationUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachineHelixEffect extends MachineEffect {

    public MachineHelixEffect() {
        super(1L, 24);
    }

    @Override
    public void doStep(@NotNull Location loc2, @NotNull SimpleParticle particle, int step) {
        Location location = loc2.add(0, 0.05D, 0);

        double n2 = 0.3141592653589793 * step;
        double n3 = step * 0.1 % 2.5;
        double n4 = 0.75;
        Location pointOnCircle = LocationUtils.getPointOnCircle(location, true, n2, n4, n3);
        Location pointOnCircle2 = LocationUtils.getPointOnCircle(location, true, n2 - 3.141592653589793, n4, n3);
        particle.play(pointOnCircle, 0.0, 1);
        particle.play(pointOnCircle2, 0.0, 1);
    }
}
