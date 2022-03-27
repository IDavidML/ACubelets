package me.davidml16.acubelets.animations.animation.animation17;

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

public class Animation17_Blocks extends AnimationBlocks {

    public Animation17_Blocks(Location location) {

        super(location);

        // FIRST CROSS
        setStepFakeBlocks(1, new FakeBlock[] {
                new FakeBlock(getLocation(0), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(1), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(2), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(3), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(4), XMaterial.SNOW_BLOCK)
        });

        // CORNERS
        setStepFakeBlocks(2, new FakeBlock[] {
                new FakeBlock(getLocation(5), XMaterial.PACKED_ICE),
                new FakeBlock(getLocation(6), XMaterial.PACKED_ICE),
                new FakeBlock(getLocation(7), XMaterial.PACKED_ICE),
                new FakeBlock(getLocation(8), XMaterial.PACKED_ICE)
        });

        // STAIRS
        setStepFakeBlocks(3, new FakeBlock[] {
                new FakeBlock(getLocation(9), XMaterial.QUARTZ_STAIRS, BlockFace.WEST),
                new FakeBlock(getLocation(10), XMaterial.QUARTZ_STAIRS, BlockFace.EAST),
                new FakeBlock(getLocation(11), XMaterial.QUARTZ_STAIRS, BlockFace.NORTH),
                new FakeBlock(getLocation(12), XMaterial.QUARTZ_STAIRS, BlockFace.SOUTH)
        });

        // SIDES OF STAIRS
        setStepFakeBlocks(4, new FakeBlock[] {
                new FakeBlock(getLocation(13), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(14), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(15), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(16), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(17), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(18), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(19), XMaterial.QUARTZ_SLAB),
                new FakeBlock(getLocation(20), XMaterial.QUARTZ_SLAB)
        });

        // CORNER PILLARS 0
        setStepFakeBlocks(5, new FakeBlock[] {
                new FakeBlock(getLocation(21), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(22), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(23), XMaterial.SNOW_BLOCK),
                new FakeBlock(getLocation(24), XMaterial.SNOW_BLOCK)
        });

        // CORNER PILLARS 1
        setStepFakeBlocks(6, new FakeBlock[] {
                new FakeBlock(getLocation(25), XMaterial.OAK_FENCE),
                new FakeBlock(getLocation(26), XMaterial.OAK_FENCE),
                new FakeBlock(getLocation(27), XMaterial.OAK_FENCE),
                new FakeBlock(getLocation(28), XMaterial.OAK_FENCE)
        });

        // CORNER PILLARS 2
        setStepFakeBlocks(7, new FakeBlock[] {
                new FakeBlock(getLocation(29), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(30), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(31), XMaterial.OAK_SLAB),
                new FakeBlock(getLocation(32), XMaterial.OAK_SLAB)
        });

    }

}
