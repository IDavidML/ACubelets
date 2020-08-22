package me.davidml16.acubelets.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

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

    public static List<Block> getSphere(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX();
        int Y = location.getBlockY();
        int Z = location.getBlockZ();
        int radiusSquared = radius * radius;
        for (int x = X - radius; x <= X + radius; x++) {
            for (int y = Y - radius; y <= Y + radius; y++) {
                for (int z = Z - radius; z <= Z + radius; z++) {
                    if ((X - x) * (X - x) + (Z - z) * (Z - z) <= radiusSquared) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }

    public static List<Block> getCube(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        int X = location.getBlockX() - radius / 2;
        int Y = location.getBlockY() - radius / 2;
        int Z = location.getBlockZ() - radius / 2;
        for (int x = X; x < X + radius; x++) {
            for (int y = Y; y < Y + radius; y++) {
                for (int z = Z; z < Z + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
