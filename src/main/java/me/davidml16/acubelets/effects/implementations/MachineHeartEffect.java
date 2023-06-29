package me.davidml16.acubelets.effects.implementations;

import me.davidml16.acubelets.effects.MachineEffect;
import me.davidml16.acubelets.effects.Point3d;
import me.davidml16.acubelets.effects.SimpleParticle;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MachineHeartEffect extends MachineEffect {

    private static final int POINTS = 20;

    private boolean rotate = false;

    public MachineHeartEffect() {
        super(1L, POINTS, -10);
    }

    @Override
    public void doStep(@NotNull Location location, @NotNull SimpleParticle particle, int step) {
        boolean isX = this.rotate;
        location = location.add(0, 5, 0);

        double delta = Math.PI / POINTS;
        double angle = delta * step;
        double z = 16 * Math.pow(Math.sin(angle), 3);
        double y = 13 * Math.cos(angle) - 5 * Math.cos(2 * angle) - 2 * Math.cos(3 * angle) - Math.cos(4 * angle);
        double x = 0;

        double realX = isX ? z : x;
        double realZ = isX ? x : z;
        Point3d point = new Point3d(realX / 25, (y / 25) - 1.8, realZ / 25);
        Point3d mirrored = new Point3d(isX ? -point.x : point.x, point.y, isX ? point.z : -point.z);
        particle.play(location.clone().add(point.x, point.y, point.z), 0f, 0f, 1);
        particle.play(location.clone().add(mirrored.x, mirrored.y, mirrored.z), 0f, 0f, 1);

        if (step == 0) {
            this.rotate = !this.rotate;
        }
    }
}
