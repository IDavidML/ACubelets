package me.davidml16.acubelets.enums;

import org.bukkit.util.Vector;

public enum Rotation {
    NORTH(180.0f, new Vector(0.5, 0, 1.5)),
    SOUTH(0.0f, new Vector(0.5, 0, -0.5)),
    EAST(270.0f, new Vector(-0.5, 0, 0.5)),
    WEST(90.0f, new Vector(1.5, 0, 0.5)),
    NONE(0.0f, new Vector(0.5, 0, 1.5));

    public final float value;
    public final Vector relative;

    private Rotation(float value, Vector relative) {
        this.value = value;
        this.relative = relative;
    }

}