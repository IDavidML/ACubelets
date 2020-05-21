package me.davidml16.acubelets.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class CuboidRegion {

    private final World world;

    private final Vector maximum;
    private final Vector minimum;

    public CuboidRegion(Location firstPoint, Location secondPoint) {
        world = firstPoint.getWorld();
        Vector firstVector = firstPoint.toVector();
        Vector secondVector = secondPoint.toVector();
        maximum = Vector.getMaximum(firstVector, secondVector);
        minimum = Vector.getMinimum(firstVector, secondVector);
    }

    public Set<Block> getSideBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
            for (int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
                blocks.add(world.getBlockAt(x, y, minimum.getBlockZ()));
                blocks.add(world.getBlockAt(x, y, maximum.getBlockZ()));
            }
            for (int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                blocks.add(world.getBlockAt(minimum.getBlockX(), y, z));
                blocks.add(world.getBlockAt(maximum.getBlockX(), y, z));
            }
        }
        return blocks;
    }

}