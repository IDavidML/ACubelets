package me.davidml16.acubelets.animations.animation5;

import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.MultiVersion.AB_12;
import me.davidml16.acubelets.utils.MultiVersion.AB_13;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation5_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;

    public Animation5_Blocks(Location location) {
        this.location = location;
        this.step = 1;

        this.blockStates = new HashSet<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-2, -1, -2), this.location.clone().add(2, -1, 2));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr2 = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr2.getSideBlocks()) blockStates.add(block.getState());
        blockStates.add(this.location.clone().add(-2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, -2).getBlock().getState());
    }

    public void run() {
        if(step == 1) {
            this.location.clone().add(0, -1, 0).getBlock().setType(XMaterial.SANDSTONE.parseMaterial());
            this.location.clone().add(1, -1, 0).getBlock().setType(XMaterial.SANDSTONE.parseMaterial());
            this.location.clone().add(-1, -1, 0).getBlock().setType(XMaterial.SANDSTONE.parseMaterial());
            this.location.clone().add(0, -1, 1).getBlock().setType(XMaterial.SANDSTONE.parseMaterial());
            this.location.clone().add(0, -1, -1).getBlock().setType(XMaterial.SANDSTONE.parseMaterial());
        } else if(step == 2) {
            this.location.clone().add(1, -1, 1).getBlock().setType(XMaterial.SAND.parseMaterial());
            this.location.clone().add(1, -1, -1).getBlock().setType(XMaterial.SAND.parseMaterial());
            this.location.clone().add(-1, -1, 1).getBlock().setType(XMaterial.SAND.parseMaterial());
            this.location.clone().add(-1, -1, -1).getBlock().setType(XMaterial.SAND.parseMaterial());
        } else if(step == 3) {
            placeOrientedStair(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            placeOrientedStair(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            placeOrientedStair(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            placeOrientedStair(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 4) {
            placeSandstoneSlab(this.location.clone().add(2, -1, 1));
            placeSandstoneSlab(this.location.clone().add(2, -1, -1));
            placeSandstoneSlab(this.location.clone().add(-2, -1, 1));
            placeSandstoneSlab(this.location.clone().add(-2, -1, -1));
            placeSandstoneSlab(this.location.clone().add(1, -1, 2));
            placeSandstoneSlab(this.location.clone().add(-1, -1, 2));
            placeSandstoneSlab(this.location.clone().add(1, -1, -2));
            placeSandstoneSlab(this.location.clone().add(-1, -1, -2));
        } else if(step == 5) {
            placeChiseledSandstone(this.location.clone().add(2, -1, 2));
            placeChiseledSandstone(this.location.clone().add(2, -1, -2));
            placeChiseledSandstone(this.location.clone().add(-2, -1, 2));
            placeChiseledSandstone(this.location.clone().add(-2, -1, -2));
        } else if(step == 6) {
            placeSandstoneSlab(this.location.clone().add(2, 0, 2));
            placeSandstoneSlab(this.location.clone().add(2, 0, -2));
            placeSandstoneSlab(this.location.clone().add(-2, 0, 2));
            placeSandstoneSlab(this.location.clone().add(-2, 0, -2));
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
            AB_13.placeOrientedStair(loc, XMaterial.SANDSTONE_STAIRS.parseMaterial(), facing);
        } else {
            AB_12.placeOrientedStair(loc, XMaterial.SANDSTONE_STAIRS.parseMaterial(), facing);
        }
    }

    public void placeSandstoneSlab(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.BIRCH_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, Material.matchMaterial("WOOD_STEP"), Byte.parseByte("2"));
        }
    }

    public void placeChiseledSandstone(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.CHISELED_SANDSTONE.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.CHISELED_SANDSTONE.parseMaterial(), Byte.parseByte("1"));
        }
    }

}
