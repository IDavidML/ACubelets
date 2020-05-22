package me.davidml16.acubelets.utils.MultiVersion;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class AB_12 {

    public static void placeOrientedStair(Location loc, Material material, BlockFace facing) {
        loc.getBlock().setType(material);

        byte d = 0;

        if(facing == BlockFace.WEST){
            d = 0x1;
        }else if(facing == BlockFace.EAST){
            d = 0x0;
        }else if(facing == BlockFace.NORTH){
            d = 0x3;
        }else if(facing == BlockFace.SOUTH){
            d = 0x2;
        }

        BlockState state = loc.getBlock().getState();
        state.setRawData(d);
        state.update(true);
    }

    public static void placeSlab(Location loc, Material material, byte data) {
        loc.getBlock().setType(material);

        BlockState state = loc.getBlock().getState();
        state.setRawData(data);
        state.update(true);
    }

}
