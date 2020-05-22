package me.davidml16.acubelets.utils.MultiVersion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;

public class AB_13 {

    public static void placeOrientedStair(Location loc, Material material, BlockFace facing) {
        loc.getBlock().setType(material);
        if (loc.getBlock().getBlockData() instanceof Directional) {
            Directional dir = (Directional) loc.getBlock().getBlockData();
            dir.setFacing(facing);
            loc.getBlock().setBlockData(dir);
        }
    }

    public static void placeSlab(Location loc, Material material) {
        loc.getBlock().setType(material);
    }

}
