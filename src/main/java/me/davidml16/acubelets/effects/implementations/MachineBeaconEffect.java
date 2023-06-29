package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.SimpleParticle;
import me.davidml16.acubelets.utils.LocationUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachineBeaconEffect extends MachineEffect {

    public MachineBeaconEffect() {
        super(3L, 40, -10);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {
        double n2 = 0.8975979010256552 * step;
        for (int i = step; i > Math.max(0, step - 10); --i) {
            particle.play(LocationUtils.getPointOnCircle(location, n2, 1.5, i * 0.75), 0.0f, -0.5f, 0.0f, 0.0f, 4);
        }
    }
}
