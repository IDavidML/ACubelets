package me.davidml16.acubelets.animations.animation.animation5;

import me.davidml16.acubelets.animations.AnimationBlocks;
import me.davidml16.acubelets.animations.FakeBlock;
import me.davidml16.acubelets.utils.CuboidRegion;


import me.davidml16.acubelets.utils.XSeries.XMaterial;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Animation5_Blocks extends AnimationBlocks {

    public Animation5_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.SANDSTONE),
                new FakeBlock(getLocation(1), XMaterial.SANDSTONE),
                new FakeBlock(getLocation(2), XMaterial.SANDSTONE),
                new FakeBlock(getLocation(3), XMaterial.SANDSTONE),
                new FakeBlock(getLocation(4), XMaterial.SANDSTONE)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.SAND),
                new FakeBlock(getLocation(6), XMaterial.SAND),
                new FakeBlock(getLocation(7), XMaterial.SAND),
                new FakeBlock(getLocation(8), XMaterial.SAND)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.SANDSTONE_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.SANDSTONE_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.SANDSTONE_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.SANDSTONE_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(14), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(15), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(16), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(17), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(18), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(19), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(20), XMaterial.BIRCH_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.CHISELED_SANDSTONE),
                new FakeBlock(getLocation(22), XMaterial.CHISELED_SANDSTONE),
                new FakeBlock(getLocation(23), XMaterial.CHISELED_SANDSTONE),
                new FakeBlock(getLocation(24), XMaterial.CHISELED_SANDSTONE)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(26), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(27), XMaterial.BIRCH_SLAB),
                new FakeBlock(getLocation(28), XMaterial.BIRCH_SLAB)
        });

    }

}
