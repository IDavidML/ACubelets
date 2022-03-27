package me.davidml16.acubelets.animations.animation.animation20;

import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.utils.CuboidRegion;


import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation20_Blocks extends AnimationBlocks {

    public Animation20_Blocks(Location location) {

        super(location);

        // MACHINE
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(location.clone(), XMaterial.AIR)
        });

        // FIRST CROSS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.END_STONE),
                new FakeBlock(getLocation(1), XMaterial.END_STONE),
                new FakeBlock(getLocation(2), XMaterial.END_STONE),
                new FakeBlock(getLocation(3), XMaterial.END_STONE),
                new FakeBlock(getLocation(4), XMaterial.END_STONE)
        });

        // CORNERS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.END_STONE),
                new FakeBlock(getLocation(6), XMaterial.END_STONE),
                new FakeBlock(getLocation(7), XMaterial.END_STONE),
                new FakeBlock(getLocation(8), XMaterial.END_STONE)
        });

        // STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.BRICK_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.BRICK_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.BRICK_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.BRICK_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(14), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(15), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(16), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(17), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(18), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(19), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(20), XMaterial.BRICK_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(22), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(23), XMaterial.OBSIDIAN),
                new FakeBlock(getLocation(24), XMaterial.OBSIDIAN)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(7, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(26), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(27), XMaterial.BRICK_SLAB),
                new FakeBlock(getLocation(28), XMaterial.BRICK_SLAB)
        });

    }

}
