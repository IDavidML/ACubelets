package me.davidml16.acubelets.utils;

import me.davidml16.acubelets.enums.Rotation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static List<Location> getCircle(Location location, double radius, int points) {
        List<Location> locations = new ArrayList<Location>();
        double increment = 6.283185307179586D / points;
        for (int i = 0; i < points; i++) {
            double angle = i * increment;
            double x = location.getX() + Math.cos(angle) * radius;
            double z = location.getZ() + Math.sin(angle) * radius;
            locations.add(new Location(location.getWorld(), x, location.getY(), z));
        }
        return locations;
    }

    public static List<Location> getCircleVertical(Location location, double radius, int points, Rotation rotation) {
        List<Location> locations = new ArrayList<Location>();
        double increment = 6.283185307179586D / points;
        for (int i = 0; i < points; i++) {
            double angle = i * increment;
            if(rotation == Rotation.EAST) {
                double z = location.getZ() + Math.cos(angle) * radius;
                double y = location.getY() + Math.sin(angle) * radius;
                locations.add(new Location(location.getWorld(), location.getX(), y, z));
            } else if(rotation == Rotation.WEST) {
                double z = location.getZ() - Math.cos(angle) * radius;
                double y = location.getY() + Math.sin(angle) * radius;
                locations.add(new Location(location.getWorld(), location.getX(), y, z));
            } else if(rotation == Rotation.NORTH) {
                double x = location.getX() + Math.cos(angle) * radius;
                double y = location.getY() + Math.sin(angle) * radius;
                locations.add(new Location(location.getWorld(), x, y, location.getZ()));
            } else if(rotation == Rotation.SOUTH) {
                double x = location.getX() - Math.cos(angle) * radius;
                double y = location.getY() + Math.sin(angle) * radius;
                locations.add(new Location(location.getWorld(), x, y, location.getZ()));
            }
        }
        return locations;
    }

    @NotNull
    public static String getWorldName(@NotNull Location location) {
        World world = location.getWorld();
        return world == null ? "null" : world.getName();
    }

    @NotNull
    public static Location getFirstGroundBlock(@NotNull Location loc) {
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();

        Block under = loc.getBlock();
        while ((under.isEmpty() || !under.getType().isSolid()) && under.getY() > 0) {
            under = under.getRelative(BlockFace.DOWN);
        }

        loc = under.getRelative(BlockFace.UP).getLocation();
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        return loc;
    }

    @NotNull
    public static Location getCenter(@NotNull Location location) {
        return getCenter(location, true);
    }

    @NotNull
    public static Location getCenter(@NotNull Location location, boolean doVertical) {
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        double x = getRelativeCoord(location.getBlockX());
        double y = doVertical ? getRelativeCoord(location.getBlockY()) : location.getBlockY();
        double z = getRelativeCoord(location.getBlockZ());

        location = new Location(location.getWorld(), x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }

    private static double getRelativeCoord(double cord) {
        return cord < 0 ? cord + 0.5 : cord + 0.5;
    }

    @NotNull
    public static Location getPointOnCircle(@NotNull Location loc, double n, double n2, double n3) {
        return getPointOnCircle(loc, true, n, n2, n3);
    }

    @NotNull
    public static Location getPointOnCircle(@NotNull Location loc, boolean doCopy, double n, double n2, double n3) {
        return (doCopy ? loc.clone() : loc).add(Math.cos(n) * n2, n3, Math.sin(n) * n2);
    }

    public static BlockFace getDirection(@NotNull Entity entity) {
        float yaw = Math.round(entity.getLocation().getYaw() / 90F);

        if ((yaw == -4.0F) || (yaw == 0.0F) || (yaw == 4.0F)) {
            return BlockFace.SOUTH;
        }
        if ((yaw == -1.0F) || (yaw == 3.0F)) {
            return BlockFace.EAST;
        }
        if ((yaw == -2.0F) || (yaw == 2.0F)) {
            return BlockFace.NORTH;
        }
        if ((yaw == -3.0F) || (yaw == 1.0F)) {
            return BlockFace.WEST;
        }
        return null;
    }

    @NotNull
    public static Vector getDirectionTo(@NotNull Location from, @NotNull Location to) {
        Location origin = from.clone();
        Vector target = to.clone().toVector();
        origin.setDirection(target.subtract(origin.toVector()));

        return origin.getDirection();
    }

}
