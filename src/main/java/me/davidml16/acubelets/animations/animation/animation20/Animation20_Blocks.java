package me.davidml16.acubelets.animations.animation.animation20;

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

public class Animation20_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;

    public Animation20_Blocks(Location location) {
        this.location = location;
        this.step = 1;

        this.blockStates = new HashSet<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-2, -1, -2), this.location.clone().add(2, -1, 2));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr2 = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr2.getSideBlocks()) blockStates.add(block.getState());
        blockStates.add(this.location.clone().add(0, -1, 0).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(0, 0, 0).getBlock().getState());
    }

    public void run() {
        if(step == 1) {
            this.location.clone().getBlock().setType(XMaterial.AIR.parseMaterial());
        } else if(step == 2) {
            this.location.clone().add(0, -1, 0).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(1, -1, 0).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(-1, -1, 0).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(0, -1, 1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(0, -1, -1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
        } else if(step == 3) {
            this.location.clone().add(1, -1, 1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(1, -1, -1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(-1, -1, 1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
            this.location.clone().add(-1, -1, -1).getBlock().setType(XMaterial.END_STONE.parseMaterial());
        } else if(step == 4) {
            placeOrientedStair(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            placeOrientedStair(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            placeOrientedStair(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            placeOrientedStair(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 5) {
            place(this.location.clone().add(2, -1, 1));
            place(this.location.clone().add(2, -1, -1));
            place(this.location.clone().add(-2, -1, 1));
            place(this.location.clone().add(-2, -1, -1));
            place(this.location.clone().add(1, -1, 2));
            place(this.location.clone().add(-1, -1, 2));
            place(this.location.clone().add(1, -1, -2));
            place(this.location.clone().add(-1, -1, -2));
        } else if(step == 6) {
            this.location.clone().add(2, -1, 2).getBlock().setType(XMaterial.OBSIDIAN.parseMaterial());
            this.location.clone().add(2, -1, -2).getBlock().setType(XMaterial.OBSIDIAN.parseMaterial());
            this.location.clone().add(-2, -1, 2).getBlock().setType(XMaterial.OBSIDIAN.parseMaterial());
            this.location.clone().add(-2, -1, -2).getBlock().setType(XMaterial.OBSIDIAN.parseMaterial());
        } else if(step == 7) {
            place(this.location.clone().add(2, 0, 2));
            place(this.location.clone().add(2, 0, -2));
            place(this.location.clone().add(-2, 0, 2));
            place(this.location.clone().add(-2, 0, -2));
        } else if(step == 8) {
            cancel();
        }
        step++;
    }

    public void restore() {
        for(BlockState state : blockStates) state.update(true);
    }

    public void placeOrientedStair(Location loc, BlockFace facing) {
        if(XMaterial.supports(13)) {
            AB_13.placeOrientedStair(loc, XMaterial.BRICK_STAIRS.parseMaterial(), facing);
        } else {
            AB_12.placeOrientedStair(loc, XMaterial.BRICK_STAIRS.parseMaterial(), facing);
        }
    }

    public void place(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.BRICK_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.BRICK_SLAB.parseMaterial(), Byte.parseByte("4"));
        }
    }

}
