package me.davidml16.acubelets.animations.animation.animation14;

import me.davidml16.acubelets.utils.CuboidRegion;
import me.davidml16.acubelets.utils.MultiVersion.AB_12;
import me.davidml16.acubelets.utils.MultiVersion.AB_13;
import me.davidml16.acubelets.utils.Sounds;
import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation14_Blocks extends BukkitRunnable {

    private final Location location;
    private int step;

    private final Set<BlockState> blockStates;
    private final Set<BlockState> plantsStates;

    public Animation14_Blocks(Location location) {
        this.location = location;
        this.step = 1;

        this.blockStates = new HashSet<>();
        this.plantsStates = new HashSet<>();

        CuboidRegion cr = new CuboidRegion(this.location.clone().add(-2, -1, -2), this.location.clone().add(2, -1, 2));
        for(Block block : cr.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr2 = new CuboidRegion(this.location.clone().add(-1, -1, -1), this.location.clone().add(1, -1, 1));
        for(Block block : cr2.getSideBlocks()) blockStates.add(block.getState());
        CuboidRegion cr3 = new CuboidRegion(this.location.clone().add(-1, 0, -1), this.location.clone().add(1, 0, 1));
        for(Block block : cr3.getSideBlocks()) plantsStates.add(block.getState());
        blockStates.add(this.location.clone().add(0, -1, 0).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, -2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(-2, 0, 2).getBlock().getState());
        blockStates.add(this.location.clone().add(2, 0, -2).getBlock().getState());
    }

    public void run() {
        if(step == 1) {
            this.location.clone().add(0, -1, 0).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(1, -1, 0).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(-1, -1, 0).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(0, -1, 1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(0, -1, -1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
        } else if(step == 2) {
            this.location.clone().add(1, -1, 1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(1, -1, -1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(-1, -1, 1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
            this.location.clone().add(-1, -1, -1).getBlock().setType(XMaterial.GRASS_BLOCK.parseMaterial());
        } else if(step == 3) {
            placeOrientedStair(this.location.clone().add(2, -1, 0), BlockFace.WEST);
            placeOrientedStair(this.location.clone().add(-2, -1, 0), BlockFace.EAST);
            placeOrientedStair(this.location.clone().add(0, -1, 2), BlockFace.NORTH);
            placeOrientedStair(this.location.clone().add(0, -1,-2), BlockFace.SOUTH);
        } else if(step == 4) {
            placeSlab(this.location.clone().add(2, -1, 1));
            placeSlab(this.location.clone().add(2, -1, -1));
            placeSlab(this.location.clone().add(-2, -1, 1));
            placeSlab(this.location.clone().add(-2, -1, -1));
            placeSlab(this.location.clone().add(1, -1, 2));
            placeSlab(this.location.clone().add(-1, -1, 2));
            placeSlab(this.location.clone().add(1, -1, -2));
            placeSlab(this.location.clone().add(-1, -1, -2));
        } else if(step == 5) {
            placeWoodLog(this.location.clone().add(2, -1, 2));
            placeWoodLog(this.location.clone().add(2, -1, -2));
            placeWoodLog(this.location.clone().add(-2, -1, 2));
            placeWoodLog(this.location.clone().add(-2, -1, -2));
        } else if(step == 6) {
            placeSlab(this.location.clone().add(2, 0, 2));
            placeSlab(this.location.clone().add(2, 0, -2));
            placeSlab(this.location.clone().add(-2, 0, 2));
            placeSlab(this.location.clone().add(-2, 0, -2));
        } else if(step == 7) {
            this.location.clone().add(1, 0, 1).getBlock().setType(XMaterial.POPPY.parseMaterial());
            if(XMaterial.supports(13))
                this.location.clone().add(1, 0, 0).getBlock().setType(XMaterial.GRASS.parseMaterial());
            else
                AB_12.placeBlock(this.location.clone().add(1, 0, 0), Material.matchMaterial("LONG_GRASS"), Byte.parseByte("1"));
            Sounds.playSound(location, Sounds.MySound.STEP_GRASS, 0.5F, 2);
        } else if(step == 8) {
            this.location.clone().add(1, 0, -1).getBlock().setType(XMaterial.DANDELION.parseMaterial());
            if(XMaterial.supports(13))
                this.location.clone().add(0, 0, 1).getBlock().setType(XMaterial.GRASS.parseMaterial());
            else
                AB_12.placeBlock(this.location.clone().add(0, 0, 1), Material.matchMaterial("LONG_GRASS"), Byte.parseByte("1"));
            Sounds.playSound(location, Sounds.MySound.STEP_GRASS, 0.5F, 2);
        } else if(step == 9) {
            this.location.clone().add(-1, 0, -1).getBlock().setType(XMaterial.POPPY.parseMaterial());
            if(XMaterial.supports(13))
                this.location.clone().add(0, 0, -1).getBlock().setType(XMaterial.GRASS.parseMaterial());
            else
                AB_12.placeBlock(this.location.clone().add(0, 0, -1), Material.matchMaterial("LONG_GRASS"), Byte.parseByte("1"));
            Sounds.playSound(location, Sounds.MySound.STEP_GRASS, 0.5F, 2);
        } else if(step == 10) {
            this.location.clone().add(-1, 0, 1).getBlock().setType(XMaterial.DANDELION.parseMaterial());
            if(XMaterial.supports(13))
                this.location.clone().add(-1, 0, 0).getBlock().setType(XMaterial.GRASS.parseMaterial());
            else
                AB_12.placeBlock(this.location.clone().add(-1, 0, 0), Material.matchMaterial("LONG_GRASS"), Byte.parseByte("1"));
            Sounds.playSound(location, Sounds.MySound.STEP_GRASS, 0.5F, 2);
        } else if(step == 11) {
            cancel();
        }
        step++;
    }

    public void restore() {
        for(BlockState state : plantsStates) state.update(true);
        for(BlockState state : blockStates) state.update(true);
    }

    public void placeOrientedStair(Location loc, BlockFace facing) {
        if(XMaterial.supports(13)) {
            AB_13.placeOrientedStair(loc, XMaterial.OAK_STAIRS.parseMaterial(), facing);
        } else {
            AB_12.placeOrientedStair(loc, XMaterial.OAK_STAIRS.parseMaterial(), facing);
        }
    }

    public void placeSlab(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.OAK_SLAB.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.OAK_SLAB.parseMaterial(), Byte.parseByte("0"));
        }
    }

    public void placeWoodLog(Location loc) {
        if(XMaterial.supports(13)) {
            AB_13.placeBlock(loc, XMaterial.OAK_LOG.parseMaterial());
        } else {
            AB_12.placeBlock(loc, XMaterial.OAK_LOG.parseMaterial(), Byte.parseByte("0"));
        }
    }

}
