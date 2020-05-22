package me.davidml16.acubelets.animations.animation4;

import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Animation4_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;

    public Animation4_Blocks(Location location) {
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
            this.location.clone().add(0, -1, 0).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(1, -1, 0).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(-1, -1, 0).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(0, -1, 1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(0, -1, -1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
        } else if(step == 2) {
            this.location.clone().add(1, -1, 1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(1, -1, -1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(-1, -1, 1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
            this.location.clone().add(-1, -1, -1).getBlock().setType(XMaterial.NETHERRACK.parseMaterial());
        } else if(step == 3) {
            this.location.clone().add(2, -1, 0).getBlock().setType(XMaterial.NETHER_BRICK_STAIRS.parseMaterial());
            orientBlock(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            this.location.clone().add(-2, -1, 0).getBlock().setType(XMaterial.NETHER_BRICK_STAIRS.parseMaterial());
            orientBlock(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            this.location.clone().add(0, -1, 2).getBlock().setType(XMaterial.NETHER_BRICK_STAIRS.parseMaterial());
            orientBlock(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            this.location.clone().add(0, -1, -2).getBlock().setType(XMaterial.NETHER_BRICK_STAIRS.parseMaterial());
            orientBlock(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 4) {
            this.location.clone().add(2, -1, 1).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(2, -1, -1).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-2, -1, 1).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-2, -1, -1).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(1, -1, 2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-1, -1, 2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(1, -1, -2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-1, -1, -2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
        } else if(step == 5) {
            this.location.clone().add(2, -1, 2).getBlock().setType(XMaterial.SOUL_SAND.parseMaterial());
            this.location.clone().add(2, -1, -2).getBlock().setType(XMaterial.SOUL_SAND.parseMaterial());
            this.location.clone().add(-2, -1, 2).getBlock().setType(XMaterial.SOUL_SAND.parseMaterial());
            this.location.clone().add(-2, -1, -2).getBlock().setType(XMaterial.SOUL_SAND.parseMaterial());
        } else if(step == 6) {
            this.location.clone().add(2, 0, 2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(2, 0, -2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-2, 0, 2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
            this.location.clone().add(-2, 0, -2).getBlock().setType(XMaterial.NETHER_BRICK_SLAB.parseMaterial());
        } else if(step == 7) {
            cancel();
        }
        step++;
    }

    public void restore() {
        for(BlockState state : blockStates) state.update(true);
    }

    public void orientBlock(Location loc, BlockFace facing) {
        if(loc.getBlock().getBlockData() instanceof Directional) {
            Directional dir = (Directional)loc.getBlock().getBlockData();
            dir.setFacing(facing);
            loc.getBlock().setBlockData(dir);
        }
    }

}
