package me.davidml16.acubelets.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathUtils {

    public static double cos(double angle) {
        return Math.cos(angle);
    }

    public static double sin(double angle) {
        return Math.sin(angle);
    }

    public static double tan(double angle) {
        return Math.tan(angle);
    }

    public static Vector rotateFunction(Vector v, Location loc) {

        double yawR = loc.getYaw() / 180.0 * Math.PI;
        double pitchR = loc.getPitch() / 180.0 * Math.PI;

        v = rotateAboutX(v, pitchR);
        v = rotateAboutY(v, -yawR);

        return v;

    }

    public static Vector rotateAboutX(Vector vect, double a) {
        double Y = cos(a) * vect.getY() - sin(a) * vect.getZ();
        double Z = sin(a) * vect.getY() + cos(a) * vect.getZ();
        return vect.setY(Y).setZ(Z);
    }

    public static Vector rotateAboutY(Vector vect, double b) {
        double X = cos(b) * vect.getX() + sin(b) * vect.getZ();
        double Z = -sin(b) * vect.getX() + cos(b) * vect.getZ();
        return vect.setX(X).setZ(Z);
    }

    public static Vector rotateAboutZ(Vector vect, double c) {
        double X = cos(c) * vect.getX() - sin(c) * vect.getY();
        double Y = sin(c) * vect.getX() + cos(c) * vect.getY();
        return vect.setX(X).setY(Y);
    }

}
