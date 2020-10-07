package me.davidml16.acubelets.animations.normal.animation12;

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

public class Animation12_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;

    public Animation12_Blocks(Location location) {
        this.location = location;
        this.step = 7;

        this.blockStates = new HashSet<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-2, -1, -2), this.location.clone().add(2, -1, 2));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr2 = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr2.getSideBlocks()) blockStates.add(block.getState());
        blockStates.add(this.location.clone().add(0, 0, 0).getBlock().getState());
        blockStates.add(this.location.clone().add(0, -1, 0).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, -2).getBlock().getState());
    }

    public void run() {
        if(step == 0) {
            cancel();
        } else if(step == 1) {
            if(XMaterial.supports(13))
                this.location.clone().add(0, 0, 0).getBlock().setType(XMaterial.CAULDRON.parseMaterial());
            else
                this.location.clone().add(0, 0, 0).getBlock().setType(Material.matchMaterial("CAULDRON"));
        } else if(step == 2) {
            placeSprucePlanks(this.location.clone().add(0, -1, 0));
            placeSprucePlanks(this.location.clone().add(1, -1, 0));
            placeSprucePlanks(this.location.clone().add(-1, -1, 0));
            placeSprucePlanks(this.location.clone().add(0, -1, 1));
            placeSprucePlanks(this.location.clone().add(0, -1, -1));
        } else if(step == 3) {
            placeDarkPlanks(this.location.clone().add(1, -1, 1));
            placeDarkPlanks(this.location.clone().add(1, -1, -1));
            placeDarkPlanks(this.location.clone().add(-1, -1, 1));
            placeDarkPlanks(this.location.clone().add(-1, -1, -1));
        } else if(step == 4) {
            placeOrientedStair(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            placeOrientedStair(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            placeOrientedStair(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            placeOrientedStair(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 5) {
            placeSpruce(this.location.clone().add(2, -1, 1));
            placeSpruce(this.location.clone().add(2, -1, -1));
            placeSpruce(this.location.clone().add(-2, -1, 1));
            placeSpruce(this.location.clone().add(-2, -1, -1));
            placeSpruce(this.location.clone().add(1, -1, 2));
            placeSpruce(this.location.clone().add(-1, -1, 2));
            placeSpruce(this.location.clone().add(1, -1, -2));
            placeSpruce(this.location.clone().add(-1, -1, -2));
        } else if(step == 6) {
            placeLog(this.location.clone().add(2, -1, 2));
            placeLog(this.location.clone().add(2, -1, -2));
            placeLog(this.location.clone().add(-2, -1, 2));
            placeLog(this.location.clone().add(-2, -1, -2));
        } else if(step == 7) {
            placeDark(this.location.clone().add(2, 0, 2));
            placeDark(this.location.clone().add(2, 0, -2));
            placeDark(this.location.clone().add(-2, 0, 2));
            placeDark(this.location.clone().add(-2, 0, -2));
        }
        step--;
    }

    public void restore() {
        for(BlockState state : blockStates) state.update(true);
    }

    public void placeOrientedStair(Location loc, BlockFace facing) {
        if(XMaterial.supports(13)) {
            AB_13.placeOrientedStair(loc, XMaterial.DARK_OAK_STAIRS.parseMaterial(), facing);
        } else {
            AB_12.placeOrientedStair(loc, XMaterial.DARK_OAK_STAIRS.parseMaterial(), facing);
        }
    }

    public void placeLog(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.SPRUCE_LOG.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.SPRUCE_LOG.parseMaterial(), Byte.parseByte("1"));
        }
    }

    public void placeDark(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.DARK_OAK_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.DARK_OAK_SLAB.parseMaterial(), Byte.parseByte("5"));
        }
    }

    public void placeSpruce(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.SPRUCE_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.SPRUCE_SLAB.parseMaterial(), Byte.parseByte("1"));
        }
    }

    public void placeSprucePlanks(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.SPRUCE_PLANKS.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.SPRUCE_PLANKS.parseMaterial(), Byte.parseByte("1"));
        }
    }

    public void placeDarkPlanks(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.DARK_OAK_PLANKS.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.DARK_OAK_PLANKS.parseMaterial(), Byte.parseByte("5"));
        }
    }

}
