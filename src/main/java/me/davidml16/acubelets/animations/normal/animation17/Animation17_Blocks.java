package me.davidml16.acubelets.animations.normal.animation17;

import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.MultiVersion.AB_12;
import me.davidml16.acubelets.utils.MultiVersion.AB_13;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation17_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;

    public Animation17_Blocks(Location location) {
        this.location = location;
        this.step = 0;

        this.blockStates = new HashSet<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-2, -1, -2), this.location.clone().add(2, -1, 2));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr2 = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr2.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr3 = new CuboidRegion(this.location.clone().add(-1, 0, -1), this.location.clone().add(1, 0, 1));
        for(Block block : cr3.getSideBlocks()) blockStates.add(block.getState());
        blockStates.add(this.location.clone().add(0, 0, 0).getBlock().getState());
        blockStates.add(this.location.clone().add(0, -1, 0).getBlock().getState());

        blockStates.add(this.location.clone().add(-2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, -2).getBlock().getState());

        blockStates.add(this.location.clone().add(-2, 1, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 1, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 1, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 1, -2).getBlock().getState());
    }

    public void run() {
        if(step == 0) {
            placeSnowBlock(this.location.clone().add(0, -1, 0));
            placeSnowBlock(this.location.clone().add(1, -1, 0));
            placeSnowBlock(this.location.clone().add(-1, -1, 0));
            placeSnowBlock(this.location.clone().add(0, -1, 1));
            placeSnowBlock(this.location.clone().add(0, -1, -1));
        } else if(step == 1) {
            placePackedIce(this.location.clone().add(1, -1, 1));
            placePackedIce(this.location.clone().add(1, -1, -1));
            placePackedIce(this.location.clone().add(-1, -1, 1));
            placePackedIce(this.location.clone().add(-1, -1, -1));
        } else if(step == 2) {
            placeOrientedStair(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            placeOrientedStair(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            placeOrientedStair(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            placeOrientedStair(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 3) {
            placeSlab(this.location.clone().add(2, -1, 1));
            placeSlab(this.location.clone().add(2, -1, -1));
            placeSlab(this.location.clone().add(-2, -1, 1));
            placeSlab(this.location.clone().add(-2, -1, -1));
            placeSlab(this.location.clone().add(1, -1, 2));
            placeSlab(this.location.clone().add(-1, -1, 2));
            placeSlab(this.location.clone().add(1, -1, -2));
            placeSlab(this.location.clone().add(-1, -1, -2));
        } else if(step == 4) {
            placeSnowBlock(this.location.clone().add(2, -1, 2));
            placeSnowBlock(this.location.clone().add(2, -1, -2));
            placeSnowBlock(this.location.clone().add(-2, -1, 2));
            placeSnowBlock(this.location.clone().add(-2, -1, -2));
        } else if(step == 5) {
            placeFence(this.location.clone().add(2, 0, 2));
            placeFence(this.location.clone().add(2, 0, -2));
            placeFence(this.location.clone().add(-2, 0, 2));
            placeFence(this.location.clone().add(-2, 0, -2));
        } else if(step == 6) {
            placeSlabWood(this.location.clone().add(2, 1, 2));
            placeSlabWood(this.location.clone().add(2, 1, -2));
            placeSlabWood(this.location.clone().add(-2, 1, 2));
            placeSlabWood(this.location.clone().add(-2, 1, -2));
        } else if(step == 7) {
            cancel();
        }
        step++;
    }

    public void restore() {
        for(BlockState state : blockStates) state.update(true);
    }

    public void placeOrientedStair(Location loc, BlockFace facing) {
        if(XMaterial.supports(13)) {
            AB_13.placeOrientedStair(loc, XMaterial.QUARTZ_STAIRS.parseMaterial(), facing);
        } else {
            AB_12.placeOrientedStair(loc, XMaterial.QUARTZ_STAIRS.parseMaterial(), facing);
        }
    }

    public void placeFence(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.OAK_FENCE.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.OAK_FENCE.parseMaterial(), Byte.parseByte("0"));
        }
    }

    public void placeSlab(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.QUARTZ_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.QUARTZ_SLAB.parseMaterial(), Byte.parseByte("7"));
        }
    }

    public void placeSlabWood(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.OAK_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.OAK_SLAB.parseMaterial(), Byte.parseByte("0"));
        }
    }

    public void placeSnowBlock(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.SNOW_BLOCK.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.SNOW_BLOCK.parseMaterial(), Byte.parseByte("0"));
        }
    }

    public void placePackedIce(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.PACKED_ICE.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.PACKED_ICE.parseMaterial(), Byte.parseByte("0"));
        }
    }

}
